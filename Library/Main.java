import java.io.IOException;
//import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) throws IOException {
        char choiceMain;
        Scanner charIn = new Scanner(System.in);
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("                 WELCOME TO VIVLIO LIBRARY!                   ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        do
        {
            System.out.println("\n\nEnter your option (1/2/3) according to the menu: ");
            System.out.println("1. Customer");
            System.out.println("2. Administration");
            System.out.println("3. Exit");
            int check=charIn.nextInt();
            switch(check)
            {
                case 1:
                //Call Customer
                break;
                case 2:
                //Call Admin
                break;
                case 3:
                System.out.println("Exiting the program!");
                System.exit(0);
                break;
                default:
                System.out.println("Wrong choice entered!");
            }
            System.out.println("Do you want to continue in Main? Enter Y if yes else press any key: ");
            choiceMain=charIn.next().charAt(0);
        } while (choiceMain=='Y');

        charIn.close();
        
    }    
}

//Testing Commands: 
// Admin a = new Admin("Andrew", 35, "al.andrew@vivlio.org",
        //         "Adm1n_paSs");
        // a.saveAdminDetails();

        //Admin a = Admin.getAdmin();
       // System.out.println(a);

        // new Book();
        // //System.out.println(Book.getBooks());

        // new Customer();
        // for (int i = 0; i < 4; i++)
        //     System.out.println(Customer.getCustomer(i));

        // Customer c1 = Customer.getCustomer(1);
        // //System.out.println(c1.getBookUnderPrice(400));
        // //System.out.println(c1.getBookWithAuthor(new String[] { "J K Rowling", "Cassandra Clare"}));
        // //System.out.println(c1.getBookWithGenre(new String[] { "Fiction" } ));
        // System.out.println(c1.getIfAvailable());