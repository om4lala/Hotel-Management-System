package CSCI24000_Spring24_FinalProject;
import java.io.*;

public class Authentication extends User {
    public Authentication(String username, String password) {
        super(username, password);
        
    }

    public static boolean isRoomOccupied(String roomNumber, String hotelName, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].trim().equals(hotelName) && parts[2].trim().equals(roomNumber)) {
                    return true; 
                }
            }
        } catch (IOException e) {
            
        }
        return false;
    }

    public static boolean roomIsCleanAndAvailable(String roomNumber, String hotelName, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[1].trim().equals(roomNumber)) {
                    String cleanStatus = parts[4].trim();
                    if (!cleanStatus.equalsIgnoreCase("clean")) {
                        return false; 
                    }
                   
                    if (parts.length >= 3 && parts[0].trim().equals(hotelName) && parts[2].trim().equals(roomNumber)) {
                        return false; 
                    }
                    return true; 
                }
            }
        } catch (IOException e) {
            
        }
        return false; 
    }

    public static boolean managerOwnsHotel(String username, String hotelName, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String csvUsername = parts[0].trim();
                    String csvHotelName = parts[2].trim();
                    if (csvUsername.equals(username) && csvHotelName.equals(hotelName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            
        }
        return false; 
    }

    public static boolean userExists(String username, String password, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String typedUsername = parts[0];
                String typedPassword = parts[1];
                if ((typedUsername.equals(username)) && (typedPassword.equals(password))) {
                    return true;
                }
            }
        } catch (IOException e) {
            
        }
        return false;
    }

    public static boolean guestIDExists(String guestID, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[2].trim().equals(guestID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            
        }
        return false; 
    }

    public static boolean hotelNameExists(String hotelName, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].equals(hotelName)) {
                    return true; 
                }
            }
        } catch (IOException e) {
            
        }
        return false; 
    }

    public static boolean usernameExists(String username, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingUsername = parts[0];
                if (existingUsername.equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            
        }
        return false;
    }

    public static boolean roomExists(String roomNumber, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equals(roomNumber)) {
                    return true;
                }
            }
        } catch (IOException e) {
            
        }
        return false;
    }
}
