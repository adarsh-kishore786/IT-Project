/**
 * Will handle the functions of the admin of 
 * the library. 
 * 
 * It is a child class from Person.java
 */
import java.util.*;
import java.io.*;

public class Admin extends Person
{
    // These are pretty self-explanatory names    
    private int m_numBooksOnRent;
    private int m_numBooksSold;
    private double m_revenue;
    private final int m_numBooksBorrowLimit; // maximum books which a Customer can borrow
    private static final double m_fineRate = 100.0; // this value can be decided later
    private static final String m_url = "src/admin.dat";
    private static final int m_maxBorrowDays = 14;
    public Admin(String name, int age, String username, String password)
    {
        // All this is handled by the Person constructor
        super(name, age, username, password); 

        // Initially, set everything to 0 when library opens up
        m_numBooksOnRent = 0;
        m_numBooksSold = 0;
        m_revenue = 0.0;
        
        // Define the constants
        m_numBooksBorrowLimit = 5; 
    }

    public static Admin getAdmin()
    {
        ObjectInput in = null;
        Admin admin = null;

        try 
        {
            in = new ObjectInputStream(new 
                    BufferedInputStream(new FileInputStream(m_url)));
            admin = (Admin) in.readObject();
        }
        catch (ClassNotFoundException cnfe) { System.err.println(cnfe); }
        catch (FileNotFoundException fnfe) { System.err.println(fnfe); }
        catch (IOException ie) { System.err.println(ie); }
        finally 
        {
            if (in != null)
            {
                try { in.close(); }
                catch (IOException ie) { System.err.println(ie); }
            }
        }
        return admin;
    }

    public void saveAdminDetails()
    {
        ObjectOutputStream out = null;

        try 
        {
            out = new ObjectOutputStream(new
                    BufferedOutputStream(new FileOutputStream(m_url)));

            out.writeObject(this);
            out.flush();
        }
        catch (FileNotFoundException fnfe) { System.err.println(fnfe); }
        catch (IOException ie) { System.err.println(ie); }
        finally 
        {
            if (out != null)
            {
                try { out.close(); }
                catch (IOException ie) { System.err.println(ie); }
            }
        }
    }

    public double getRevenue() { return m_revenue; }
    public int getNumBooksOnRent() { return m_numBooksOnRent; }
    public int getNumBooksSold() { return m_numBooksSold; }
    public int getNumBooksBorrowLimit() { return m_numBooksBorrowLimit; }
    public static int getMaxBorrowDays() { return m_maxBorrowDays; }
    @Override
    public String toString()
    {
        String str = super.toString() + "\n";

        str += "Number of books on rent: " + m_numBooksOnRent + "\n";
        str += "Number of books sold   : " + m_numBooksSold + "\n";
        str += "Total revenue earned   : Rs. " + m_revenue + "\n";

        return str;
    }

    public boolean sellBook(Transaction trans, Book b) 
    {
        int numCopies = getNumCopiesAvailable(b);
        if (numCopies == 0) // if no copies, then return
        {
            System.out.println("Sorry, this book has no copies!");
            return false;
        }

        if (!trans.sellBookTransaction(b))
        {
            System.out.println("Transaction failed!!!");
            return false; 
        }    
        m_revenue+=b.getPrice();
        return true;
    }

    public boolean rentBook(Transaction trans, Book b) 
    {
        int numCopies = getNumCopiesAvailable(b);
        if (numCopies == 0)
        {
            System.out.println("Sorry, this book has no copies!");
            return false;
        }

        // If customer has 1 copy already, then transaction fails
        if (!trans.rentBookTransaction(b))
        {
            System.out.println("You have borrowed this book already." + 
                        " Return that copy to borrow another one.");
            return false;
        }
        return true;
    }

    public boolean getBackBook(Transaction trans, Book book)
    {
        int index = -1;
        ArrayList<Book> books = trans.getBorrowedBooks();

        // checks the index of book in borrowed list
        for (int i = 1; i < books.size(); i++)
        {
            if (!trans.getIsReturned().get(i) && books.get(i).getISBN().equals(book.getISBN()))
            {
                index = i;
                break;
            }
        }
        if (index == -1)
        {
            System.out.println("Customer never borrowed the book.");
            return false;
        }
        
        double fine = trans.returnBookTransaction(index);
        if (fine == -1)
             System.out.println("Transaction failed!!! Exiting...");
        else
            m_revenue += fine;
        return true;
    }

    public String getHistory(Customer cust) 
    {
        return cust.getHistory();
    }

    public static double getFineRate() { return m_fineRate; }

    private int getNumCopiesAvailable(Book b)
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
        sc.close();

        if (c != 'y' && c != 'Y') numCopies = 0;
        return numCopies;
    }

    private void listOfBooks()
    {
        ArrayList<Book> books = new ArrayList<>();
        ArrayList<String> genre = new ArrayList<>();
        genre.add("Fiction");
        genre.add("Historical");

        books.add(new Book("The Forest of Vanishing Stars", "Kristin Harmel", 
                    genre, 450.0, "9781982158934"));

        genre.clear();
        genre.add("Mystery");
        genre.add("Detective");
        genre.add("Fiction");
        genre.add("Historical");

        books.add(new Book("The Devil and the Dark Water", "Stuart Turton",
                    genre, 548.48, "9781728234298"));

        genre.clear();
        genre.add("Non-Fiction");
        genre.add("Contemporary");
        genre.add("Spiritual");

        books.add(new Book("Transcedental Kingdom", "Yaa Gyasi",
                    genre, 767.36, "9781984899767"));

        genre.clear();
        genre.add("Historical");
        genre.add("Fiction");

        books.add(new Book("The Exiles", "Christina Baker Kline",
                    genre, 815.40, "9780062356338"));

        genre.clear();
        genre.add("Non-fiction");
        genre.add("Mathematics");
        
        books.add(new Book("Contemporary Abstract Algebra", "Joseph Gallian",
                    genre, 490.00, "9789353502522"));

        genre.clear();
        genre.add("Fiction");
        genre.add("Science Fiction");

        books.add(new Book("The End of Eternity", "Isaac Asimov",
                    genre, 195.00, "9780449237045"));
    }

    // public static void main(String[] args) {
    //     // Admin a = new Admin("Andrew", 35, "al.andrew@vivlio.org",
    //     //         "Adm1n_paSs");
    //     // a.saveAdminDetails();

    //     Admin a = getAdmin();
    //     System.out.println(a);
    // }
} 