import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.*;

public class AuthService {
  private static Scanner sc=null;
  private Customer c=null;
  private CustomerOptions custOptions=null;

  AuthService(Scanner sc1){
    sc=sc1;
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
      //saveAdmin();
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
          c = Customer.getCustomer(index);
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
            System.out.println("2. See list of all books along with their copies");
            System.out.println("3. Modify the details of a book or add a book");
            System.out.println("4. Remove a book");
            System.out.println("5. See the history of a customer");
            System.out.println("6. Log out");
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
                case 2: seeBookList();
                        break;
                case 3: modifyDetails();
                        break;
                case 4: removeBook();
                        break;
                case 5: seeHistory();
                        break;
                case 6: System.out.println();
                        return;
                default: System.out.println("That's an invalid option. Try again.\n");
            }
        }
    }

  public void seeHistory()
  {
      Customer cust = null;
      do
      {
            System.out.print("\nEnter the username of customer: ");
            String uname = sc.nextLine();

            cust = findCustomer(uname);
            if (cust == null)
            {
                System.out.println("No such customer exists! Try again.");
                continue;
            }
      } while (cust == null);

      custOptions=new CustomerOptions(cust,this,sc);
      System.out.println(cust);
      custOptions.showHistory();
  }

  public Customer findCustomer(String uname)
  {
      c = new Customer();
      for (Customer customer : c.getCustomers())
        if (customer.getUsername().equals(uname))
            return customer;
      return null;
  }

  public void logout()
  {
      System.out.println("Logging you out...");
      custOptions=null;
      c=null;
      Main.main(null);
  }

  private void seeBookList()
  {
      ArrayList<Book> bookList = Catalogue.getBooks();
      for (Book b : bookList)
      {
          System.out.println(b + "Copies : " + b.getNumCopies());
      }
      System.out.println("\n---------------------------------");
  }

  private void modifyDetails()
  {
      System.out.print("\nEnter title: ");
      String title = sc.nextLine().trim();

      System.out.println("Enter authors (write \"exit\" when done):");
      ArrayList<String> authorList = new ArrayList<>();
      CustomerOptions.addToList(authorList, sc);
      System.out.println("Authors: " + authorList + "\n");

      System.out.println("Enter genre (write \"exit\" when done):");
      ArrayList<String> genreList = new ArrayList<>();
      CustomerOptions.addToList(genreList, sc);
      System.out.println("Genre: " + genreList + "\n");

      double price = 0;
      do
      {
          try
          {
              System.out.print("Enter price: ");
              price = Double.parseDouble(sc.nextLine());
          }
          catch (NumberFormatException e)
          {
              System.out.println("That's an invalid price. Try again.\n");
              continue;
          }
          break;
      } while (true);

      String ISBN = "";
      do
      {
          System.out.print("Enter ISBN: ");
          ISBN = sc.nextLine();
          if (!Catalogue.checkISBN(ISBN))
          {
            System.out.println("Invalid ISBN. Try again.\n");
            continue;
          }
          break;
      }
      while (true);

      sleep(1);
      Book b = new Book(title, authorList, genreList, price, ISBN);
      System.out.println("Book:\n" + b);
      if (hasBook(ISBN))
      {
          String choice;
          do
          {
              System.out.print("This ISBN already exists in the database. Update book (Y/N)? ");
              choice = sc.nextLine().trim();
              if (choice.equalsIgnoreCase("y"))
              {
                  addBook(b);
                  break;
              }
              else if (choice.equalsIgnoreCase("n"))
                return;
              else
                System.out.println("Invalid input. Try again.\n");
          } while (true);
      }
      else addBook(b);

      System.out.println("---------------------------------");
  }

  private void removeBook()
  {
      ArrayList<String> isbnList = new ArrayList<>();
      System.out.println("Enter ISBNs of books to remove (\"exit\" to stop):");
      CustomerOptions.addToList(isbnList, sc);

      ArrayList<Book> removeBooksList = new ArrayList<>();

      ArrayList<Book> booksList = Catalogue.getBooks();
      Iterator itr = booksList.iterator();
      while (itr.hasNext())
      {
          Book b = (Book)itr.next();
          for (String ISBN : isbnList)
            if (b.getISBN().equals(ISBN))
            {
                itr.remove();
                removeBooksList.add(b);
            }
      }

      if (removeBooksList.size() == 0)
        System.out.println("No books match these ISBN values!\n");
      else
      {
          System.out.println("Removed books:\n");
          for (Book b : removeBooksList)
            System.out.println(b);
          Catalogue.saveBooks(booksList);
      }
      System.out.println("---------------------------------");
  }

  private boolean hasBook(String ISBN)
  {
      for (Book b : Catalogue.getBooks())
        if (b.getISBN().equals(ISBN))
            return true;
      return false;
  }

  private void addBook(Book book)
  {
      boolean found = false;
      ArrayList<Book> booksList = Catalogue.getBooks();
      for (int i = 0; i < booksList.size(); i++)
      {
          Book b = booksList.get(i);
          if (b.getISBN().equals(book.getISBN()))
          {
            sleep(1);
            found = true;
            booksList.set(i, book);
            System.out.println("Updated!\n");
            break;
          }
      }
      if (!found)
      {
        sleep(1);
        booksList.add(book);
        System.out.println("Added new book!\n");
      }
      Catalogue.saveBooks(booksList);
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
          Customer c = Customer.getCustomer(i);
          // System.out.println(c);
          if (c.getUsername().equals(username))
              if (c.getPassword().equals(passwd))
                  return i;
              else
                  return -1;
      }

      return -1;
  }

  public static void sleep(int seconds){
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
