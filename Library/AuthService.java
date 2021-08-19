import java.util.*;
import java.util.concurrent.TimeUnit;

public class AuthService
{
    private static Scanner sc = null;
    private static Customer c = new Customer();
    private CustomerOptions custOptions = null;
    private AdminOptions adOptions = null;

    AuthService(Scanner sc1)
    {
        sc = sc1;
    }

    public void error()
    {
        System.out.println("That's an invalid input. Please try again.\n");
    }

    public void login()
    {
        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        if (intersects(username, "0123456789"))
            cust_login(username);
        else
            admin_login(username);
    }

    public void admin_login(String username)
    {
        Admin admin = Admin.getAdmin();

        String password = null;

        System.out.print("Enter your password: ");
        password = sc.nextLine();

        if (!password.equals(admin.getPassword()) || !username.equals(admin.getUsername()))
        {
            System.err.println("Invalid username/password.\n");
            password = null;
            return;
        }

        System.out.println("\nWelcome administrator " + admin.getName() +
            ", logging you in...\n");
        password = null;
        sleep(1);
        adOptions = new AdminOptions(admin, this, sc);
        adOptions.showFunctions();
    }

    public void cust_login(String username)
    {
        Customer cust = new Customer();

        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        int index = getCustIndex(cust, username, password);

        if (index == -1)
            System.out.println("Invalid username/password.\n");
        else
        {
            c = Customer.getCustomer(index);
            System.out.println("\nWelcome customer " + c.getName() +
                ", logging you in...\n");
            sleep(1);
            custOptions = new CustomerOptions(c, this, sc);
            custOptions.showOptions();
        }
    }

    public void create_account(Customer cust)
    {
        String username = null;
        String name = null;
        int age = 0;
        Customer c = null;
        double fees = 0;

        do
        {
            System.out.print("Enter name: ");
            name = sc.nextLine();

            System.out.print("Enter age: ");
            try
            {
                age = Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("That's not a valid age. Taking 25 as default.");
                age = 25;
            }

            System.out.print("Enter username (without the @vivlio.org): ");
            username = sc.nextLine();
            if (!intersects(username, "0123456789"))
                username += age;
            username += "@vivlio.org";

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            c = new Customer(name, age, username, password);

            int state = cust.addCustomer(c);
            if (state == 0)
            {
                fees = 100;
                break;
            }
            if (state == 1)
                return;
        }
        while (true);
        sleep(1);

        System.out.println("\nNew user created:");
        System.out.println(c);

        CustomerOptions.viewManual();

        if (fees == 0)
            return;

        Admin a = Admin.getAdmin();
        a.setRevenue(a.getRevenue() + fees);
        a.saveAdminDetails();
    }

    public void logout()
    {
        System.out.println("Logging you out...");
        sleep(1);
        custOptions = null;
        c = null;
        Main.main(null);
    }

    private static boolean intersects(String s1, String s2)
    {
        for (int i = 0; i < s2.length(); i++)
        {
            if (s1.contains("" + s2.charAt(i)))
                return true;
        }
        return false;
    }

    private static int getCustIndex(Customer cust, String username, String passwd)
    {
        for (int i = 0; i < cust.getCustomers().size(); i++)
        {
            Customer c = Customer.getCustomer(i);

            if (c.getUsername().equals(username))
                if (c.getPassword().equals(passwd))
                    return i;
                else
                    return -1;
        }
        return -1;
    }

    public static void sleep(int seconds)
    {
        try { TimeUnit.SECONDS.sleep(seconds); }
        catch (InterruptedException ie) { System.out.println(ie); }
    }
}
