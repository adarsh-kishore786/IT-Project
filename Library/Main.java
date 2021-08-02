import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.time.LocalDate;
public class Main
{

    public static void main(String[] args)
    {
        //Testing Commands:
        // Admin a = new Admin("Andrew", 35, "al.andrew@vivlio.org",
        //         "Adm1n_paSs");
        // a.saveAdminDetails();

        Admin a = Admin.getAdmin();
        System.out.println(a);

        new Book();
        //System.out.println(Book.getBooks());

        new Customer();
        // for (int i = 0; i < 4; i++)
        //     System.out.println(Customer.getCustomer(i));

        Customer c1=  new Customer("Harry", 19, "hg@gmail.com", "Hero");
        c1.saveCustomer();
        System.out.println(c1);

        Book.saveBooks();
        //Customer c1 = Customer.getCustomer(0);
        //System.out.println(c1);
         //System.out.println(c1.getBookUnderPrice(400));
         //System.out.println(c1.getBookWithAuthor(new String[] { "J K Rowling", "Cassandra Clare"}));
         //System.out.println(c1.getBookWithGenre(new String[] { "Fiction" } ));
         //System.out.println(c1.getIfAvailable());
         String s = new String();
         Scanner check = new Scanner(System.in);
         s= check.nextLine();
         Book b=null;
         try {
            b = Book.getBookWithTitle(s);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //  try {
        //     //Book.saveBooks();

        //     //System.out.println(Book.getBooks());
        // } catch (ClassNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        c1.buyBook(b);
        c1.saveCustomer();
        System.out.println(c1.getTransaction().getBoughtBooks());
        check.close();

        // c1.buyBook(b);
        // c1.saveCustomer();
        // System.out.println(c1.getTransaction().getBoughtBooks());
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
