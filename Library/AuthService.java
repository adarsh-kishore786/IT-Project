import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.*;

public class AuthService {
  private static Scanner sc=null;
  private Customer c=null;
  private CustomerOptions custOptions=null;

  AuthService(Scanner sc){
    this.sc=sc;
  }

  public void error(){
    System.out.println("That's an invalid input. Please try again.\n");
  }

  public void login(){
    System.out.print("Enter your username: ");
    String username = sc.nextLine();

    if (intersects(username, "0123456789"))
      cust_login(username);
    else
      admin_login(username);
      }

  public  void admin_login(String username){
      // saveAdmin();
      Admin admin = Admin.getAdmin();

      // Console c = System.console();
      // if (c == null)
      // {
      //     System.err.println("No console");
      //     System.exit(1);
      // }
      String password = null;

      // password = getString(c.readPassword("Enter your password: "));
      System.out.print("Enter your password: ");
      password = sc.nextLine();

      if (!password.equals(admin.getPassword()) || !username.equals(admin.getUsername()))
      {
          System.err.println("Invalid username/password.\n");
          return;
      }

      System.out.println("Welcome administrator " + admin.getName() + ", logging you in...\n");
      sleep(1);
      adminFunctions(admin);
  }

  public void cust_login(String username){
      Customer cust = new Customer();

      System.out.print("Enter your password: ");
      String password = sc.nextLine();
      int index = getCustIndex(cust, username, password);
      // System.out.println("index = " + index);

      if (index == -1)
          System.out.println("Invalid username/password.\n");
      else
      {
          c = cust.getCustomer(index);
          System.out.println("Welcome customer " + c.getName() + ", logging you in...\n");
          sleep(1);
          custOptions=new CustomerOptions(c,this,sc);
          custOptions.showOptions();
      }
  }

  public void create_account(Customer cust){
      String username = null;
      String name = null;
      int age = 0;
      Customer c = null;

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

          if (cust.addCustomer(c))
              break;
      }
      while (true);
      sleep(1);

      System.out.println("\nNew user created:");
      System.out.println(c);
  }

  public void adminFunctions(Admin admin){
        while (true)
        {
            System.out.println(admin + "\n");
            System.out.println("What would you like to do?");
            System.out.println("1. See list of all customers");
            System.out.println("2. Log out");
            System.out.print("Enter option number: ");
            int choice = 0;
            try
            {
                choice = Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e)
            {
                error();
                continue;
            }

            switch (choice)
            {
                case 1: showCustomers();
                        break;
                case 2: System.out.println();
                        return;
                default: System.out.println("That's an invalid option. Try again.\n");
            }
        }
    }

  public void logout(){
      System.out.println("Logging you out..."); 
      custOptions=null;
      c=null;
      Main.main(null);
    }

  private static boolean intersects(String s1, String s2){
      for (int i = 0; i < s2.length(); i++)
      {
          if (s1.contains("" + s2.charAt(i)))
              return true;
      }
      return false;
  }

  private static void saveAdmin(){
      Admin a = new Admin("Andrew Jarvis", 35, "andrewj@vivlio.org",
              "aDm1n-PasS");
      a.saveAdminDetails();
  }

  private static String getString(char[] arr){
      String nstr = "";
      for (char c : arr)
          nstr += c + "";
      return nstr;
  }

  private static int getCustIndex(Customer cust, String username, String passwd){
      for (int i = 0; i < cust.getCustomers().size(); i++)
      {
          Customer c = cust.getCustomer(i);
          // System.out.println(c);
          if (c.getUsername().equals(username))
              if (c.getPassword().equals(passwd))
                  return i;
              else
                  return -1;
      }

      return -1;
  }

  private static void sleep(int seconds){
      try
      {
          TimeUnit.SECONDS.sleep(seconds);
      }
      catch (InterruptedException ie)
      {
          System.out.println(ie);
      }
  }

  private static void showCustomers(){
      Customer cust = new Customer();
      for (Customer c : cust.getCustomers())
          System.out.println(c);
      System.out.println("---------------------------------");
  }
}
