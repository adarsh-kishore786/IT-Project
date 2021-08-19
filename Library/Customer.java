import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.nio.file.*;

/**
  * Customer.java
  *
  * This class represents a Customer object, who
  * can interact with the library and use its features.
  * This class is a child class of Person.java.
  */
public class Customer extends Person
{
    private static Admin admin = Admin.getAdmin();
    private static ArrayList<Customer> customerList = new ArrayList<>();
    // contains all customer objects; note that it is static

    private static int borrowLimit = Admin.getNumBooksBorrowLimit();
    private int numBooksBorrowed;
    private int numBooksBought;
    HashMap<Book, ArrayList<LocalDate>> history;

    ArrayList<Book> booksBorrowed; // initialized in constructor
    ArrayList<Book> booksBought; // initialized in constructor
    Transaction transaction; // contains history of borrow/return dates with
                            // fine; unique to every customer

    // Location of the dat file to store everything
    static String customerFile =
    FileSystems.getDefault().getPath(System.getProperty("user.dir"), "dat/customer.dat").toString();

    public Customer() { initCustomerList(); }

    Customer(String name, int age, String userName, String password)
    {
        super(name, age, userName, password);
        this.booksBorrowed = new ArrayList<Book>();
        this.booksBought = new ArrayList<Book>();
        this.transaction = new Transaction();
        this.history = new HashMap<Book, ArrayList<LocalDate>>();
    }

    // getter functions
    int getBorrowLimit() { return borrowLimit; }
    int getNumBooksBorrowed() { return this.numBooksBorrowed; }
    int getNumBooksBought() { return this.numBooksBought; }

    ArrayList<Customer> getCustomers() { return customerList; }

    // add a new customer with membership fees
    int addCustomer(Customer c)
    {
        for (Customer c1 : customerList)
        {
            if (c1.getUsername().equals(c.getUsername()))
            {
                System.out.println("This username is already taken. Try another\n");
                return 2;
            }
        }

        String ch = "";
        do
        {
            System.out.print("One-time membership is Rs. 100.00. Pay amount (Y/N): ");

            ch = Main.sc.nextLine();
            System.out.println();
            if (ch.equalsIgnoreCase("Y"))
            {
                boolean b = Transaction.custAdd();
                if (b) break;

                return 1;
            }
            else if (ch.equalsIgnoreCase("N"))
                return 1;
            else
                System.out.println("Invalid input. Try again.\n");
        }
        while (true);

        customerList.add(c);
        Customer.saveCustomer();
        return 0;
    }

    // buy a book
    void buyBook(Book book)
    {
        if (admin.sellBook(transaction, book))
        {
            booksBought.add(book); // update array list
            numBooksBought = booksBought.size();
            // update number of book bought

            // updating book list
            book.setBuyers(this);
            Catalogue.saveBooks();

            // set transaction purchase date
            // this.transaction.setDateOfPurchase(purchaseDate);
            ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
            dates.add(this.getTransaction().getDateOfPurchase(book));
            history.put(book, dates);

            // update dat file
            Customer.saveCustomer(this);
        }
    }

    // borrow a book
    void borrowBook(Book book)
    {
        // check if limit is respected
        if (booksBorrowed.size() < borrowLimit)
        {
            if (admin.rentBook(transaction, book))
            {
                book.setBorrowers(this);
                Catalogue.saveBooks();

                booksBorrowed.add(book); // add book to customer list of
                                         // borrowed books
                numBooksBorrowed = booksBorrowed.size();

                // update dat file
                Customer.saveCustomer(this);

                System.out.println("\nCongrats! You have borrowed a new book!");
            }
        }
        else
            System.out.println("Borrow Limit Reached! Please return a book to continue"); // response to limit breach
    }

    void returnBook(Book book)
    {
        if (admin.getBackBook(transaction, book))
        {
            // update history by adding the book and list of dates to hashmap
            ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
            dates.add(this.getTransaction().getDateOfBorrow(book));
            dates.add(this.getTransaction().getDateOfReturn(book));
            history.put(book, dates);

            // remove book from list
            for (Book b : booksBorrowed)
            {
                if (b.getTitle().equals(book.getTitle()))
                {
                    booksBorrowed.remove(b);
                    numBooksBorrowed = booksBorrowed.size();
                    break;
                }
            }

            // update dat file
            Customer.saveCustomer(this);
            System.out.println("\nSuccessfully Returned!");
        }
    }

    HashMap<Book, ArrayList<LocalDate>> getHistory() { return this.history; }
    Transaction getTransaction() { return transaction; }

    // write customer object to dat file
    static void saveCustomer()
    {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(customerFile)))
        {
            os.writeObject(customerList); // write array list to file
            initCustomerList();
        }
        catch (IOException e) { System.err.println(e); }
    }

    // an overloaded function that modifies a particular customer's
    // details also
    static void saveCustomer(Customer cust)
    {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(customerFile)))
        {
            if (cust != null)
            {
                for (Customer c : customerList)
                {
                    if (c.getUsername().equals(cust.getUsername()))
                    {
                        int index = customerList.indexOf(c);
                        customerList.set(index, cust); // update customer object to list
                        break;
                    }
                }
            }
            os.writeObject(customerList); // write array list to file
            initCustomerList();
        }
        catch (IOException e) { System.err.println(e); }
    }

    // get customer object from list
    static Customer getCustomer(int n) { return customerList.get(n); }

    @SuppressWarnings("unchecked")
    private static void initCustomerList()
    {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(customerFile))))
        {
            customerList = (ArrayList<Customer>) in.readObject();
            // write array list to file
        }
        catch (IOException e) { System.err.println(e); }
        catch (ClassNotFoundException e) { System.err.println(e); }
    }
}
