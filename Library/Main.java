import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.*;

public class Main
{
    public static void error()
    {
        System.out.println("That's an invalid input. Please try again.\n");
    }

    public static void welcome()
    {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("                 WELCOME TO VIVLIO LIBRARY!                   ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        Scanner sc = new Scanner(System.in);
        AuthService auth=new AuthService(sc);
        String ch = null;
        do
        {
            System.out.print("(L)ogin, (S)ign up or (E)xit: ");
            ch = sc.nextLine();

            if (ch.equalsIgnoreCase("l"))
              auth.login();  //login();
            else if (ch.equalsIgnoreCase("s"))
              auth.create_account(new Customer()); //create_account(new Customer());
            else if (ch.equalsIgnoreCase("e"))
              System.exit(0);
            else
                error();

            ch = "";
        } while (!ch.equalsIgnoreCase("l") && !ch.equalsIgnoreCase("s") && !ch.equalsIgnoreCase("e"));
        sc.close();
    }

    public static void main(String[] args)
    {
        // Customer cust = new Customer();
        // // cust.saveCustomer();
        // for (Customer c : cust.getCustomers())
        //     System.out.println(c);
        //Catalogue.getBooks();
        
        welcome();
    }

    public static void sleep(int seconds)
    {
        try
        {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch (InterruptedException ie)
        {
            System.out.println(ie);
        }
    }

    private static void showCustomers()
    {
        Customer cust = new Customer();
        for (Customer c : cust.getCustomers())
            System.out.println(c);
        System.out.println("---------------------------------");
    }


}
