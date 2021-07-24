import java.io.IOException;
import java.util.ArrayList;

public class Main 
{
    public static void main(String[] args) throws IOException {
        // Admin a = new Admin("Andrew", 35, "al.andrew@vivlio.org",
        //         "Adm1n_paSs");
        // a.saveAdminDetails();

        Admin a = Admin.getAdmin();
        System.out.println(a);

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
    }    
}
