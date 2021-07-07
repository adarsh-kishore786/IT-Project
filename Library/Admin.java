/**
 * Will handle the functions of the admin of 
 * the library. 
 * 
 * It is a child class from Person.java
 */

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

    // public void sellBook(Book b) {}
    // public void rentBook(Book b) {}
    // public Book[] getBooks() {}
    // public void takeFine(double fine) {}
    // public String getHistory(Customer cust) {}

    public static double getFineRate() { return m_fineRate; }
} 
