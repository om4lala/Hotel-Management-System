package CSCI24000_Spring24_FinalProject;
import java.util.*;
import java.io.*;

public class Admin extends User{
    private String hotelName;

    public Admin(String username, String password, String hotelName) {
        super(username, password);
        this.hotelName = hotelName;
    }

    public static void adminLogin() {
        System.out.println("\n-----Admin Login-----");
        Scanner input = new Scanner(System.in);
    
        int attempts = 0;
        final int maxAttempts = 5;
    
        while (attempts < maxAttempts) {
            System.out.print("Enter Username: ");
            String username = input.nextLine();
            System.out.print("Enter Password: ");
            String password = input.nextLine();
    
            if ((username.equals("admin")) && (password.equals("admin"))) {
                System.out.println("You're logged in");
                adminDirectory();
                return;
            } else {
                System.out.println("Incorrect Username or Password!");
                attempts++;
                System.out.println("You have " + (maxAttempts - attempts) + " attempts remaining.");
            }
        }
        System.out.println("Exceeded maximum login attempts. Returning to main menu.");
        Main.mainPage();
    }
    

    public static void adminDirectory() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n-----Admin Directory-----");
        System.out.println("1. Add Manager\n2. Remove Manager\n3. Add Hotel for Manager\n4. Remove Hotel for Manager\n5. View All Managers\n6. Logout");

        while(true){
            try{
                System.out.print("Enter Choice: ");
                int choice = input.nextInt();
                if(choice == 1){
                    createManagerAccount();
                    adminDirectory();
                    break;
                }else if(choice == 2){
                    deleteManagerAccount();
                    break;
                }else if(choice == 3){
                    addHotelForManager();
                    adminDirectory();
                }else if(choice == 4){
                    deleteHotelForManager();
                    adminDirectory();
                }else if(choice == 5){
                    viewAllUsers();
                    adminDirectory();
                }else if(choice == 6){
                    Main.main(null);
                }else{
                    System.out.println("Invalid Choice! Try Again.");
                }
            }catch(InputMismatchException e){
                System.out.println("Invalid Choice! Try Again.");
                input.next();
            }
        }
    }

    public static void viewAllUsers(){
        System.out.println("\n------All Managers-----");
        try{
            BufferedReader reader = new BufferedReader(new FileReader("managers.csv"));
            String managerInfo;
            while((managerInfo = reader.readLine()) != null){
                String[] managers = managerInfo.split(",");

                for(String manager: managers){
                    System.out.printf("%-20s", manager);
                }
                System.out.println();
                
            }
        }catch(Exception e){
            System.out.println("There are no managers in the database!");
        }
    }

    public static void deleteHotelForManager() {
        Scanner input = new Scanner(System.in);
    
        viewAllUsers();
    
        System.out.print("Enter Manager Username: ");
        String manager = input.nextLine();
    
        try {
            List<String[]> rows = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("managers.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3 && parts[0].equals(manager)) {
                        parts[2] = ""; 
                    }                    
                    rows.add(parts);
                }
            }
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("managers.csv"))) {
                for (String[] row : rows) {
                    writer.write(String.join(",", row) + "\n");
                }
                System.out.println("Hotel removed for manager successfully.");
            }
        } catch (IOException e) {
            System.out.println("There are no managers in the database!");
        }
    }    

    public static void addHotelForManager() {
        System.out.println("\n-----Add Hotel for Manager-----");
        Scanner input = new Scanner(System.in);
    
        System.out.print("Enter Manager Username: ");
        String manager = input.nextLine();
        System.out.print("Enter Hotel Name to be added: ");
        String hotelName = input.nextLine();

        if (!Authentication.usernameExists(manager, "managers.csv")) {
            System.out.println("The manager, " + manager + ", does not exist.");
            adminDirectory();
            return; 
        }
    
        if (Authentication.hotelNameExists(hotelName, "managers.csv")) {
            System.out.println("Hotel name already exists. Please choose a different one.");
            return;
        }
    
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("managers.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[0].equals(manager)) {
                        parts = Arrays.copyOf(parts, 3); 
                        parts[2] = hotelName; 
                    }
                    lines.add(String.join(",", parts));
                }
            }
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("managers.csv"))) {
                for (String line : lines) {
                    writer.write(line + "\n");
                }
                System.out.println("Hotel added for manager successfully.");
            }
        } catch (IOException e) {
            System.out.println("The manager,  " + manager + ", does not exist.");
        }
    }

    public static void createManagerAccount() {
        System.out.println("\n-----Create Manager Account-----");
        Scanner input = new Scanner(System.in);
    
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();
    
        if (Authentication.usernameExists(username, "managers.csv")) {
            System.out.println("Username already exists. Please choose a different one.");
            createManagerAccount();
            return; 
        }
    
        try (BufferedWriter file = new BufferedWriter(new FileWriter("managers.csv", true))) {
            BufferedReader reader = new BufferedReader(new FileReader("managers.csv"));
            if (reader.readLine() == null) {
                file.write("username,password,hotel\n");
            }
            file.write(username + ',' + password + "\n"); 
            System.out.println("Manager account created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void deleteManagerAccount() {
        System.out.println("\n-----Delete Manager Account-----");
        
        ArrayList<String> activeManagers = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("managers.csv"));
            Scanner input = new Scanner(System.in);
            viewAllUsers();
            System.out.print("\nEnter Username of Manager: ");
            String manager = input.nextLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(manager)) {
                    activeManagers.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("There are no managers in database!");
            adminDirectory();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("managers.csv"));

            for (String line : activeManagers) {
                writer.write(line + "\n");
            }
            writer.close();
            System.out.println("Manager removed");
            adminDirectory();

        } catch (IOException e) {
            System.out.println("There are no managers in database!");
            adminDirectory();
        }
    }
}
