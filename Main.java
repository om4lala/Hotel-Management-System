package CSCI24000_Spring24_FinalProject;
import java.util.*;

public class Main {
    public static void main(String [] args){
        Main.mainPage();
    }

    public static void mainPage(){
        Scanner input = new Scanner(System.in);
    
        System.out.println("-----Hotel Manager------");
        System.out.println("1. Admin\n2. Manager\n3. Exit");
        
        while(true){
            try{
                System.out.print("Enter Choice: ");
                int choice = input.nextInt();
                if(choice == 1){
                    Admin.adminLogin();
                    break;
                }else if(choice == 2){
                    Manager.managerLogin();
                    break;
                }else if(choice == 3){
                    System.exit(0);
                }else{
                    System.out.println("Invalid Choice! Try Again.");
                }
            }catch(InputMismatchException e){
                System.out.println("Invalid Choice! Try Again.");
                input.next();
            }
        }
        input.close();
    }
}
