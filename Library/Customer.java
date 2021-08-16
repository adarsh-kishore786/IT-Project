import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.nio.file.*;

public class Customer extends Person {

  private static Admin admin = Admin.getAdmin();
  private static ArrayList<Customer> customerList = new ArrayList<>(); // contains all customer objects; note that it is
                                                                       // static

  private static int borrowLimit = Admin.getNumBooksBorrowLimit();
  private int numBooksBorrowed;
  private int numBooksBought;
  HashMap<Book, ArrayList<LocalDate>> history; // planning to make hisory an array list at a later stage
  ArrayList<Book> booksBorrowed; // initialized in constructor
  ArrayList<Book> booksBought; // initialized in constructor
  Transaction transaction; // contains history of borrow/return dates with fine; unique to every customer
  static String customerFile =
    FileSystems.getDefault().getPath(System.getProperty("user.dir"), "dat/customer.dat").toString();

  public Customer() {
    // if (customerList.size() > 0)
    // return;
    // Customer c1 = new Customer("Harish", 25, "harry123@vivlio.org", "harRy-P0t");
    // Customer c2 = new Customer("Adarsh", 24, "ash786@vivlio.org", "C=(acrossb)");
    // Customer c3 = new Customer("Poorna", 25, "poornah06@vivlio.org", "A=2piR");
    // Customer c4 = new Customer("Ranjana", 24, "ranjanak45@vivlio.org",
    // "B1n@ry-M");
    //
    // customerList.add(c1);
    // customerList.add(c2);
    // customerList.add(c3);
    // customerList.add(c4);
    //
    // //System.out.println("!" + customerList.size());
    // saveCustomer();
    initCustomerList();
  }

  Customer(String name, int age, String userName, String password) {
    super(name, age, userName, password);
    this.booksBorrowed = new ArrayList<Book>();
    this.booksBought = new ArrayList<Book>();
    this.transaction = new Transaction();
    this.history = new HashMap<Book, ArrayList<LocalDate>>();
    // this.booksBorrowed= new Book[numBooksBorrowed];
    // this.booksBought=new Book[numBooksBought];
  }

  int getBorrowLimit() {
    return borrowLimit;
  }

  int getNumBooksBorrowed() {
    return this.numBooksBorrowed;
  }

  int getNumBooksBought() {
    return this.numBooksBought;
  }

  ArrayList<Customer> getCustomers() {
    return customerList;
  }

  int addCustomer(Customer c) {
    for (Customer c1 : customerList) {
      if (c1.getUsername().equals(c.getUsername())) {
        System.out.println("This username is already taken. Try another\n");
        return 2;
      }
    }

    String ch = "";
    do {
      System.out.print("One-time membership is Rs. 100.00. Pay amount (Y/N): ");
      ch = Main.sc.nextLine();
      System.out.println();
      if (ch.equalsIgnoreCase("Y")) {
        boolean b = Transaction.custAdd();
        if (b)
          break;
        return 1;
      } else if (ch.equalsIgnoreCase("N"))
        return 1;
      else
        System.out.println("Invalid input. Try again.\n");
    } while (true);

    customerList.add(c);
    Customer.saveCustomer();
    return 0;
  }

  void buyBook(Book book) {
    // currently no restriction on buying
    // Date purchaseDate=new Date(); //returns current date

    // reduce number of copies of book

    if (admin.sellBook(transaction, book)) {
      // int n=book.getNumCopies();
      // book.setNumCopies(n-1);
      booksBought.add(book); // update array list
      numBooksBought = booksBought.size(); // update number of books bought

      // updating book list
      book.setBuyers(this);
      // Book.saveBook(); already called in Book.java

      // update dat file
      Customer.saveCustomer(this);

      System.out.println("Bought Successfully!");

      ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
      dates.add(this.getTransaction().getDateOfPurchase(book));
      history.put(book, dates);
    }
    // set transaction purchase date
    // this.transaction.setDateOfPurchase(purchaseDate);
  }

  void borrowBook(Book book) {
    // return current date
    // Date borrowDate=new Date();

    // check if limit is respected
    if (booksBorrowed.size() < borrowLimit) {

      if (admin.rentBook(transaction, book)) {
        book.setBorrowers(this);
        Catalogue.saveBooks();

        booksBorrowed.add(book); // add book to customer list of borrowed books
        numBooksBorrowed = booksBorrowed.size();

        // update dat file
        Customer.saveCustomer(this);

        System.out.println("\nCongrats! You have borrowed a new book!");
      }

    } else
      System.out.println("Borrow Limit Reached! Please return a book to continue"); // response to limit breach
  }

  void returnBook(Book book) {
    if (admin.getBackBook(transaction, book)) {

      // update history by adding the book and list of dates to hashmap
      ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
      dates.add(this.getTransaction().getDateOfBorrow(book));
      dates.add(this.getTransaction().getDateOfReturn(book));
      history.put(book, dates);

      // remove book from list
      for (Book b : booksBorrowed) {
        if (b.getTitle().equals(book.getTitle())) {
          booksBorrowed.remove(b);
          numBooksBorrowed = booksBorrowed.size();
          break;
        }
      }

      // update dat file
      Customer.saveCustomer(this);
      System.out.println("\nSuccessfully Returned!");
    }
    // initializeReturnTransaction(return Date);
  }

  HashMap<Book, ArrayList<LocalDate>> getHistory() {
    return this.history;
  }

  // return transaction object
  Transaction getTransaction() {
    return transaction;
  }

  // write customer object to dat file
  static void saveCustomer() {

    // ObjectOutputStream os=null; //stream that writes object to file
    try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(customerFile))) {
      os.writeObject(customerList); // write array list to file
      initCustomerList();

    } catch (IOException e) {
      System.err.println(e);
      // customerList.remove(this);
    }
  }

  static void saveCustomer(Customer cust) {

    // ObjectOutputStream os=null; //stream that writes object to file
    try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(customerFile))) {
      if (cust != null) {
        for (Customer c : customerList) {
          if (c.getUsername().equals(cust.getUsername())) {
            int index = customerList.indexOf(c);
            customerList.set(index, cust); // update customer object to list
            break;
          }
        }
      }
      os.writeObject(customerList); // write array list to file
      initCustomerList();

    } catch (IOException e) {
      System.err.println(e);
      // customerList.remove(this);
    }
  }

  // get customer object from list
  static Customer getCustomer(int n) {
    // initCustomerList();
    return customerList.get(n);
  }

  @SuppressWarnings("unchecked")
  private static void initCustomerList() {
    // ObjectInputStream in=null;
    // try with resources block doesnt need resource closing
    try (ObjectInputStream in = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream(customerFile)))) {

      customerList = (ArrayList<Customer>) in.readObject(); // write array list to file

    } catch (IOException e) {
      System.err.println(e);
    } catch (ClassNotFoundException e) {
      System.err.println(e);
    }
  }
}
