package CSCI24000_Spring24_FinalProject;
import java.util.*;
import java.io.*;

public class Room extends User{
    private String roomNumber;
    private String type;
    private String status;

    public Room(String roomNumber, String type, String status, String username, String password) {
        super(username, password);
        this.roomNumber = roomNumber;
        this.type = type;
        this.status = status;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Room Number: " + roomNumber + ", Type: " + type + ", Status: " + status;
    }

    public static void addRooms(String hotelName, String username) {
        System.out.println("\n-----Add Rooms-----");
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Room Number: ");
        String roomNumber = input.nextLine();
        if (Authentication.roomExists(roomNumber, "rooms.csv")) {
            System.out.println("Room already exists.");
            Manager.managerDirectory(username, hotelName);
            return;
        }
        System.out.print("Enter Room Type: ");
        String roomType = input.nextLine();
        System.out.print("Enter Price per Night: ");
        double pricePerNight = input.nextDouble();
        input.nextLine(); 
        
        System.out.print("Is the room clean? (yes/no): ");
        String isCleanInput = input.nextLine().toLowerCase();
        boolean isClean = isCleanInput.equalsIgnoreCase("yes");

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.csv", true))) {
            writer.write(hotelName + "," + roomNumber + "," + roomType + "," + pricePerNight + "," + (isClean ? "clean" : "not clean") + "\n");
            System.out.println("Room added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void updateRoomStatus(String roomNumber, String status, String filename) {
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder inputBuffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[1].trim().equals(roomNumber)) {
                    parts[4] = status;
                }
                inputBuffer.append(String.join(",", parts)).append("\n");
            }
            reader.close();
            FileWriter writer = new FileWriter(file);
            writer.write(inputBuffer.toString());
            writer.close();
        } catch (IOException e) {
            
        }
    }

    public static void viewAvailableRooms(String hotelName) {
        System.out.println("\n-----Available Clean Rooms in " + hotelName + "-----");
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equalsIgnoreCase(hotelName) && parts[4].trim().equalsIgnoreCase("clean")) {
                    String roomNumber = parts[1].trim();
                    String roomType = parts[2].trim();
                    double pricePerNight = Double.parseDouble(parts[3].trim());
                    System.out.println("Room Number: " + roomNumber + ", Room Type: " + roomType + ", Price Per Night: $" + pricePerNight);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("There are no available rooms in: " + hotelName);
        }
    }
}

