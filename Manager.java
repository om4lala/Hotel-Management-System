package CSCI24000_Spring24_FinalProject;
import java.util.*;
import java.io.*;

public class Manager extends User{
    public Manager(String username, String password) {
        super(username, password);
    }

    public static void managerDirectory(String username, String hotelName){
        System.out.println("\n-----Manager Directory-----");
        Scanner input = new Scanner(System.in);
    
        System.out.println("1. Check-Inn Guests\n2. Check-Out Guests\n3. Add Rooms\n4. View Guests\n5. View Available Rooms\n6. View Total Income\n7. Logout");
    
        while(true){
            try{
                System.out.print("Enter Choice: ");
                int choice = input.nextInt();
                input.nextLine(); 
                
                if(choice == 1){
                    checkInnGuests(hotelName);
                    managerDirectory(username, hotelName);
                } else if(choice == 2){
                    checkOutGuests(hotelName);
                    managerDirectory(username, hotelName);
                } else if(choice == 3){
                    while(true){
                        Room.addRooms(hotelName, username);
                        System.out.print("\nWould you like to keep adding rooms? (Yes/No): ");
                        String option = input.nextLine().toLowerCase();
    
                        if(option.equals("no")){
                            managerDirectory(username, hotelName);
                            break; 
                        } else if(!option.equals("yes")){
                            System.out.println("Invalid option. Please enter 'Yes' or 'No'.");
                        }
                    }
                } else if(choice == 4){
                    viewGuests(hotelName);
                    managerDirectory(username, hotelName);

                } else if(choice == 5){
                    Room.viewAvailableRooms(hotelName);
                    managerDirectory(username, hotelName);
                } else if(choice == 6){
                    viewTotalIncome(hotelName);
                    managerDirectory(username, hotelName);
                } else if(choice == 7){
                    Main.mainPage();
                } else {
                    System.out.println("Invalid Choice! Try Again.");
                }
            } catch(Exception e){
                System.out.println("Invalid Choice! Try Again.");
                input.next(); 
            }
        }
    } 
    
    public static void viewTotalIncome(String hotelName) {
        System.out.println("\n-----Total Income for " + hotelName + "-----");

        double grossIncome = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader("guests.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(hotelName)) {
                    double pricePerNight = Double.parseDouble(parts[4].trim());
                    grossIncome += pricePerNight;
                }
            }
            System.out.println("Total Income: $" + grossIncome);
        } catch (IOException e) {
            System.out.println("Total Income: $" + grossIncome);
        }
    }

    public static void checkOutGuests(String hotelName) {
        System.out.println("\n-----Check-Out Guests-----");
        Scanner input = new Scanner(System.in);
    
        System.out.print("Enter Room Number: ");
        String roomNumber = input.nextLine();
    
        if (!Authentication.roomExists(roomNumber, "rooms.csv")) {
            System.out.println("Room does not exist.");
            return;
        }
    
        if (!Authentication.isRoomOccupied(roomNumber, hotelName, "guests.csv")) {
            System.out.println("No guest is currently checked-in to this room.");
            return;
        }
    
        removeGuest(roomNumber, "guests.csv");
    
        Room.updateRoomStatus(roomNumber, "clean", "rooms.csv");
    
        System.out.println("Guest checked-out successfully.");
    }
    
    private static void removeGuest(String roomNumber, String filename) {
        try {
            File inputFile = new File(filename);
            File tempFile = new File("temp.csv");
    
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
    
            String currentLine;
    
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts.length >= 4 && parts[2].trim().equals(roomNumber)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
    
            if (!inputFile.delete()) {
                System.out.println("Failed to delete the original file.");
                return;
            }
    
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Failed to rename the temporary file.");
            }
    
        } catch (IOException e) {
            ;
        }
    }
    
    public static void viewGuests(String hotelName) {
        System.out.println("\n-----Guests in " + hotelName + "-----");
        try (BufferedReader reader = new BufferedReader(new FileReader("guests.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equals(hotelName)) {
                    String guestId = parts[2].trim();
                    String guestName = parts[1].trim();
                    String roomNumber = parts[3].trim();
                    String price = parts[4].trim();
                    System.out.println("Guest ID: " + guestId + ", Guest Name: " + guestName + ", Room Number: " + roomNumber + ", Price: " + price);
                }
            }
        } catch (IOException e) {
            System.out.println("There are no guests in: " + hotelName);
        }
    }

    
    public static void checkInnGuests(String hotelName) {
        System.out.println("\n-----Check-In Guests-----");
        Scanner input = new Scanner(System.in);
    
        System.out.print("Enter Guest First Name: ");
        String guestFirstName = input.nextLine();
        System.out.print("Enter Guest Last Name: ");
        String guestLastName = input.nextLine();
        System.out.print("Enter Guest ID Number: ");
        String guestID = input.nextLine();
        if (Authentication.guestIDExists(guestID, "guests.csv")) {
            System.out.println("Guest ID already exists. Cannot check-in guest.");
            managerDirectory(username, hotelName);
        }
        System.out.print("Enter Room Number: ");
        String roomNumber = input.nextLine();
    
        if (!Authentication.roomExists(roomNumber, "rooms.csv")) {
            System.out.println("Room does not exist.");
            return;
        }
    
        if (!Authentication.roomIsCleanAndAvailable(roomNumber, hotelName, "rooms.csv")) {
            System.out.println("Room is not clean or is already occupied. Cannot check-in guest.");
            return;
        }
    
        double pricePerNight = getPricePerNight(roomNumber, hotelName, "rooms.csv");
        if (pricePerNight == -1) {
            System.out.println("Error retrieving room price. Cannot check-in guest.");
            return;
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("guests.csv", true))) {
            writer.write(hotelName + "," + guestFirstName + " " + guestLastName + "," + guestID + "," + roomNumber + "," + pricePerNight + "\n");
            System.out.println("Guest checked-in successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return;
        }
    
        Room.updateRoomStatus(roomNumber, "dirty", "rooms.csv");
    }
    
    private static double getPricePerNight(String roomNumber, String hotelName, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(hotelName) && parts[1].trim().equalsIgnoreCase(roomNumber)) {
                    return Double.parseDouble(parts[3].trim()); 
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void managerLogin() {
        System.out.println("\n-----Welcome Manager-----");
        Scanner input = new Scanner(System.in);
        int attempts = 0;
        final int maxAttempts = 5;
    
        while (attempts < maxAttempts) {
            try {
                System.out.print("Enter Username: ");
                String username = input.nextLine();
                System.out.print("Enter Password: ");
                String password = input.nextLine();
        
                if (Authentication.userExists(username, password, "managers.csv")) {
                    System.out.println("You're logged in\n");
        
                    viewOwnedHotels(username, "managers.csv");
        
                    System.out.print("Enter the hotel you want to manage: ");
                    String hotelName = input.nextLine();
                    if (Authentication.managerOwnsHotel(username, hotelName, "managers.csv")) {
                        managerDirectory(username, hotelName);
                        return;
                    } else {
                        System.out.println("You are not assigned to " + hotelName + ". ");
                        Main.main(null); 
                        return;
                    }
                } else {
                    System.out.println("Incorrect Username or Password!");
                    attempts++;
                    System.out.println("You have " + (maxAttempts - attempts) + " attempts remaining.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Choice! Try Again.");
                input.nextLine();
                attempts++;
                System.out.println("You have " + (maxAttempts - attempts) + " attempts remaining.");
            }
        }
        System.out.println("Exceeded maximum login attempts. Returning to main menu.");
        Main.main(null);
    }    

    public static void viewOwnedHotels(String username, String filename) {
        System.out.println("\n-----Hotels Owned by " + username + "-----");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].trim().equals(username)) {
                    String hotelName = parts[2].trim();
                    System.out.println(hotelName);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
}