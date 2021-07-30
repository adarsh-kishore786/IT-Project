import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.time.LocalDate;
public class Main 
{
    public static void callAdmin() {
        Scanner inputManage = new Scanner(System.in);
        String password;
        char choiceAdmin;
        do
        {
            System.out.println("Welcome to the Administration section");
            System.out.println("Please enter the pssword to continue: ");
            password=inputManage.nextLine();
            if (password.compareTo("supersecretpassword")==0) //Better implementation of this part xD
            {
                //Admin related things
                System.out.println("\n\nEnter your option (1/2/3) according to the menu: ");
                System.out.println("1. Manage books");
                System.out.println("2. ");
                System.out.println("3. Go back to main");
                int check=inputManage.nextInt();
                switch(check)
                {
                    case 1:
                    //Manage books function
                    break;
                    case 2:
                    //Whatever functionalities available in Admin, expand the menu
                    break;
                    case 3:
                    System.out.println("Going back to main");
                    //Main.main();
                    break;
                    default:
                    System.out.println("Wrong choice entered!");
                }
            }
            else
            System.out.println("Wrong password!");
            System.out.println("Do you want to continue in Admin? Enter Y if yes else press any key: ");
            choiceAdmin=inputManage.next().charAt(0);
        } while (choiceAdmin=='Y');
        
        inputManage.close();

    }
    public static void callCustomer() {

    }
    public static void main(String[] args) throws IOException {
        //Testing Commands: 
        Admin a = new Admin("Andrew", 35, "al.andrew@vivlio.org",
                "Adm1n_paSs");
        a.saveAdminDetails();

         a = Admin.getAdmin();
       System.out.println(a);

        new Book();
        //System.out.println(Book.getBooks());

        new Customer();
        // for (int i = 0; i < 4; i++)
        //     System.out.println(Customer.getCustomer(i));
        // Customer c1=  new Customer("Harry", 19, "hg@gmail.com", "Hero");
        // c1.saveCustomer();
        // System.out.println(c1);
        Customer c1 = Customer.getCustomer(0);
        System.out.println(c1);
         //System.out.println(c1.getBookUnderPrice(400));
         //System.out.println(c1.getBookWithAuthor(new String[] { "J K Rowling", "Cassandra Clare"}));
         //System.out.println(c1.getBookWithGenre(new String[] { "Fiction" } ));
         //System.out.println(c1.getIfAvailable());
         Book b=null;
        try {
            b = Book.getBooks().get(0);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c1.buyBook(b);
        c1.saveCustomer();
        System.out.println(c1.getTransaction().getBoughtBooks());
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