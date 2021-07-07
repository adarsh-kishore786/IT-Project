/**
 * Will handle the functions of the admin of 
 * the library. 
 * 
 * It is a child class from Person.java
 */
import java.util.*;

public class Admin extends Person
{
    // These are pretty self-explanatory names    
    private int m_numBooksOnRent;
    private int m_numBooksSold;
    private double m_revenue;
    private final int m_numBooksBorrowLimit; // maximum books which a Customer can borrow
    private static final double m_fineRate = 100.0; // this value can be decided later

    public Admin(String name, int age, String username, String password)
    {
        // All this is handled by the Person constructor
        super(name, age, username, password); 

        // Initially, set everything to 0 when library opens up
        m_numBooksOnRent = 0;
        m_numBooksSold = 0;
        m_revenue = 0.0;
        
        // Define the constants
        m_numBooksBorrowLimit = 10; 
    }

    public static Admin getAdmin()
    {
        // Load all info from .dat file
        return null;
    }

    public double getRevenue() { return m_revenue; }
    public int getNumBooksOnRent() { return m_numBooksOnRent; }
    public int getNumBooksSold() { return m_numBooksSold; }
    public int getNumBooksBorrowLimit() { return m_numBooksBorrowLimit; }

    public void sellBook(Book b) 
    {
        int numCopies = getNumCopies(b);
        if (numCopies == 0) // if no copies, then return
            return;

        // add it in revenue and update number of copies
        b.setNumCopies(b.getNumCopies() - numCopies);   
        m_revenue += b.getPrice() * numCopies;   
    }

    public void rentBook(Book b) 
    {
        int numCopies = getNumCopies(b);
        if (numCopies == 0)
            return;

        // Update borrower list
        // b.setNumCopies(b.getNumCopies() - numCopies);
    }

    public void getBackBook(Book b)
    {

    }
    // public void takeFine(double fine) {}
    // public String getHistory(Customer cust) {}

    public static double getFineRate() { return m_fineRate; }

    private int getNumCopies(Book b)
    {
        // Check if book is available
        if (!b.isAvailable())
        {
            System.out.println("This book is not available currently.");
            return 0;
        }
        // How many copies are there
        System.out.println("There are " + b.getNumCopies() + " copies of this book available.");
        int numCopies = 1;

        // How many copies Customer wants to buy
        System.out.println("Take 1 copy? (Y/N)");
        Scanner sc = new Scanner(System.in);
        char c = sc.nextLine().charAt(0);

        if (c != 'y' && c != 'Y') numCopies = 0;
        return numCopies;
    }
} 
