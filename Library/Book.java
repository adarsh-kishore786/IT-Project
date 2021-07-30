import java.io.*;
import java.util.*;

public class Book implements Serializable
{
    //instance variables
    private String m_title, m_ISBN, m_author;
    private double m_price;
    private int m_numCopies;
    private ArrayList<Customer> m_borrowersList, m_buyersList;
    private ArrayList<String> m_genre;

    //static variable containing a list of all the books in the library
    private static ArrayList<Book> booksList = new ArrayList<Book>();

    //default constructor with null/default values
    public Book()
    {
        // try
        // {
        //     Book.booksInfo();
        // }
        // catch (IOException ie) { System.out.println(ie); }
    }

    //constructor taking in 5 characteristic inputs; others are updated with time
    public Book(String title, String author, ArrayList<String> genre, double price, String ISBN)
    {
        m_title=title;
        m_author=author;
        m_genre=new ArrayList<>(genre);
        m_price=price;
        m_ISBN=ISBN;
        //10 copies of each book 
        m_numCopies=10;
        m_buyersList=new ArrayList<Customer>();
        m_borrowersList=new ArrayList<Customer>(); 
    }

    // private static void booksInfo() throws IOException
    // {
    // }    
        
    public String toString()
    {
        //String representation of the book object with all of its characteristic details
        String s="\nTitle  : "+m_title+"\nAuthor : "+m_author+"\nGenre  : "+showGenre()+"\nPrice  : "+m_price+"\nISBN   : "+m_ISBN
            + "\n";
        return s;
    }

    //getter functions
    public String getTitle()
    {
        return m_title;
    }

    public String getISBN()
    {
        return m_ISBN;
    }

    public String getAuthor()
    {
        return m_author;
    }

    public double getPrice()
    {
        return m_price;
    }

    public int getNumCopies()
    {
        return m_numCopies;
    }

    public boolean isAvailable()
    {
        return (m_numCopies!=0);
    }

    public ArrayList<String> getGenre()
    {
        return m_genre;
    }

    public ArrayList<Customer> getBorrowers()
    {
        return m_borrowersList;
    }

    public ArrayList<Customer> getBuyers()
    {
        return m_buyersList;
    }

    //returns an arraylist of all the books
    public static ArrayList<Book> getBooks() 
    {
        ObjectInputStream in = null;
        try 
        {
            in = new ObjectInputStream(new BufferedInputStream(
                new FileInputStream("src/booksFile.dat")));
            
            Object obj = in.readObject();
            if (obj instanceof ArrayList<?>)
                booksList = (ArrayList<Book>) obj; 
        }
        catch (ClassNotFoundException cnfe) { System.err.println(cnfe); }
        catch (IOException ie) { System.err.println(ie); }
        return booksList;
    }

    //private function to print genre in a nice manner
    private String showGenre()
    {
        String s=m_genre.get(0);
        for(int i=1;i<m_genre.size();i++)
            s+=", "+m_genre.get(i);
        return s;
    }

    //setter functions
    public void setTitle(String title)
    {
        m_title=title;
    }

    public void setAuthor(String author)
    {
        m_author=author;
    }

    public void setISBN(String ISBN)
    {
        m_ISBN=ISBN;
    }

    public void setPrice(double price)
    {
        m_price=price;
    }

    public void setGenre(ArrayList<String> genre)
    {
        m_genre.addAll(genre);
    }

    public void setNumCopies(int numCopies)
    {
        m_numCopies=numCopies;
    }

    public void setBorrowers(Customer borrower) throws IOException
    {
        m_borrowersList.add(borrower);
        saveBooks();
    }

    public void setBuyers(Customer buyer) throws IOException
    {
        m_buyersList.add(buyer);
        saveBooks();
    }

    public static boolean checkISBN(String ISBN)
    {
        //checks for the input ISBN in the bookslist by linear search
        for (int i=0;i<booksList.size();i++) 
            if(booksList.get(i).getISBN().equals(ISBN))
                return true;
        
        //returns false when the whole arraylist is traversed through and input ISBN is not found
        return false;
    }

    public static void saveBooks() throws IOException
    {
        //streams objects required
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;

        //writing the booksList into the file with all the updated information
        try
        {
            //file containing books arraylist-booksFile.dat passed to the stream
            fos=new FileOutputStream("src/booksFile.dat");
            oos=new ObjectOutputStream(fos);
            oos.writeObject(booksList);
        }
        catch (FileNotFoundException fnfe) 
        { 
            System.err.println(fnfe); 
        }
        catch (IOException ie) 
        { 
            System.err.println(ie); 
        }
        finally 
        {
            if(fos!=null)
                fos.close();
            if(oos!=null)
                oos.close();
        }
    }

    public static void saveBooks(ArrayList<Book> books) throws IOException
    {
        //updating the static books list with the new books in the arraylist
        booksList.addAll(books);

        //streams objects required
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;

        //writing the booksList into the file
        try
        {
            //file containing books arraylist-booksFile.dat passed to the stream
            fos=new FileOutputStream("src/booksFile.dat");
            oos=new ObjectOutputStream(fos);
            oos.writeObject(booksList);
            System.out.println("All the books have been saved!");
        }
        catch (FileNotFoundException fnfe) 
        { 
            System.err.println(fnfe); 
        }
        catch (IOException ie) 
        { 
            System.err.println(ie); 
        }
        finally 
        {
            if(fos!=null)
                fos.close();
            if(oos!=null)
                oos.close();
        }

    }

}
