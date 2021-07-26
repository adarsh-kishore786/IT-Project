import java.io.*;
import java.util.*;

public class Book implements Serializable
{
    //instance variables
    private String m_title, m_ISBN;
    private double m_price;
    private int m_numCopies;
    private ArrayList<Customer> m_borrowersList, m_buyersList;
    private ArrayList<String> m_genre, m_author;

    //static variable containing a list of all the books in the library
    private static ArrayList<Book> booksList = new ArrayList<Book>();

    //default constructor with null/default values
    public Book()
    {}

    //constructor taking in 5 characteristic inputs; others are updated with time
    public Book(String title, ArrayList<String> author, ArrayList<String> genre, double price, String ISBN)
    {
        m_title=title;
        m_author=new ArrayList<>(author);
        m_genre=new ArrayList<>(genre);
        m_price=price;
        m_ISBN=ISBN;
        //10 copies of each book 
        m_numCopies=10;
        m_buyersList=new ArrayList<Customer>();
        m_borrowersList=new ArrayList<Customer>(); 
    }
        
    public String toString()
    {
        //String representation of the book object with all of its characteristic details
        String s="\nTitle  : "+m_title+"\nAuthor : "+showAuthor()+"\nGenre  : "+showGenre()+"\nPrice  : "+m_price+"\nISBN   : "+m_ISBN
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

    public ArrayList<String> getAuthor()
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

    //private function to print genre in a nice manner
    private String showGenre()
    {
        String s=m_genre.get(0);
        for(int i=1;i<m_genre.size();i++)
            s+=", "+m_genre.get(i);
        return s;
    }

    //private function to print authors in a nice manner
    private String showAuthor()
    {
        String s=m_author.get(0);
        for(int i=1;i<m_author.size();i++)
            s+=", "+m_author.get(i);
        return s;
    }

    //setter functions
    public void setTitle(String title)
    {
        m_title=title;
    }

    public void setAuthor(ArrayList<String> author)
    {
        m_author.addAll(author);
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

    public void setBorrowers(Customer borrower) throws IOException, ClassNotFoundException
    {
        m_borrowersList.add(borrower);
        saveBooks();
    }

    public void setBuyers(Customer buyer) throws IOException, ClassNotFoundException
    {
        m_buyersList.add(buyer);
        saveBooks();
    }

    public static boolean checkISBN(String ISBN)
    {
        ISBN=ISBN.trim();

        //length of book ISBNs is 13 
        if(ISBN.length()!=13)
            return false;

        //weighted sum must be divisible by 10
        //digits are multiplied with 1 and 3 alternatively
        int sum=0;
        for(int i=0;i<13;i++)
        {
            if(i%2==0)
                sum+=Character.valueOf(ISBN.charAt(i));
            else
                sum=sum+(3*(Character.valueOf(ISBN.charAt(i))));
        }
        
        if(sum%10==0)
            return true;
        return false;
    }

    //returns an arraylist of all the books
    public static ArrayList<Book> getBooks() throws IOException, ClassNotFoundException
    {
        initBooks();
        return booksList;
    }

    //initializes the books list with all the books in the file
    private static void initBooks() throws IOException, ClassNotFoundException
    {
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        booksList.clear();

        try
        {
            fis=new FileInputStream("Library/src/booksFile.dat");
            ois=new ObjectInputStream(fis);
            booksList = (ArrayList<Book>) ois.readObject();
            // while ((object =  ois.readObject()) instanceof Book)
            // { 
            //     if(object instanceof Book) 
            //     {
            //         Book book = (Book) object;
            //         booksList.add(book);
            //     }
            // }
            
        }
        catch(EOFException eofe)
        {
            System.out.println(eofe);
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
            if(fis!=null)
                fis.close();
            if(ois!=null)
                ois.close();
        }
    }

    public static void saveBooks() throws IOException, ClassNotFoundException
    {
        //streams objects required
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;

        //writing the booksList into the file with all the updated information
        try
        {
            //file containing books arraylist-booksFile.dat passed to the stream
            fos=new FileOutputStream("Library/src/booksFile.dat");
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

    public static void saveBooks(ArrayList<Book> books) throws IOException, ClassNotFoundException
    {
        //if the arraylist is null, then all the books already in the file are stored in the list
        if(booksList==null)
            initBooks();

        //updating the static books list with the new books in the arraylist
        booksList.addAll(books);

        saveBooks();
    }

    // private static void booksInfo() throws IOException, ClassNotFoundException
    // {
    //     ArrayList<Book> books=new ArrayList<Book>();
    //     ArrayList<String> genre=new ArrayList<String>();
    //     genre.add("Fiction");
    //     genre.add("Suspense");
    //     genre.add("Thriller");
    //     genre.add("Romance");
        
    //     //System.out.println(genre);
    //     Book obj1=new Book("Angels and Demons", "Dan Brown", genre, 258.00, "9781416524793");
        
    //     Book obj2=new Book("The Da Vinci Code", "Dan Brown", genre, 246.00, "9780375432309");
    //     //System.out.println(obj2);
    //     //obj2.setAuthor("Dan Brown");
        
    //     System.out.println(obj1.getGenre());
    //     genre.clear();
    //     books.add(obj1);
    //     books.add(obj2);
    //     genre.add("Fiction");
    //     genre.add("Historical");

    //     books.add(new Book("The Forest of Vanishing Stars", "Kristin Harmel", 
    //                 genre, 450.0, "9781982158934"));

    //     genre.clear();
    //     genre.add("Mystery");
    //     genre.add("Detective");
    //     genre.add("Fiction");
    //     genre.add("Historical");

    //     books.add(new Book("The Devil and the Dark Water", "Stuart Turton",
    //                 genre, 548.48, "9781728234298"));

    //     genre.clear();
    //     genre.add("Non-Fiction");
    //     genre.add("Contemporary");
    //     genre.add("Spiritual");

    //     books.add(new Book("Transcedental Kingdom", "Yaa Gyasi",
    //                 genre, 767.36, "9781984899767"));

    //     genre.clear();
    //     genre.add("Historical");
    //     genre.add("Fiction");

    //     books.add(new Book("The Exiles", "Christina Baker Kline",
    //                 genre, 815.40, "9780062356338"));

    //     genre.clear();
    //     genre.add("Non-fiction");
    //     genre.add("Mathematics");
        
    //     books.add(new Book("Contemporary Abstract Algebra", "Joseph Gallian",
    //                 genre, 490.00, "9789353502522"));

    //     genre.clear();
    //     genre.add("Fiction");
    //     genre.add("Science Fiction");

    //     books.add(new Book("The End of Eternity", "Isaac Asimov",
    //                 genre, 195.00, "9780449237045"));

    //     genre.clear();
    //     genre.add("Fiction");
    //     genre.add("Fantasy");
    //     genre.add("Young Adult Fiction");
    //     genre.add("Drama");
    //     genre.add("Mystery");
    //     books.add(new Book("Harry Potter and the Philosopher's Stone","J K Rowling",genre,284.00,"9780439362139"));
    //     books.add(new Book("Harry Potter and the Chamber of Secrets","J K Rowling",genre,285.00,"9780439064873"));
    //     books.add(new Book("Harry Potter and the Prisoner of Azkaban","J K Rowling",genre,300.00,"9780545582933"));
    //     books.add(new Book("Harry Potter and the Goblet of Fire","J K Rowling",genre,320.00,"9781338299175"));
    //     books.add(new Book("Harry Potter and the Order of the Phoenix","J K Rowling",genre,340.00,"9781439520024"));
    //     books.add(new Book("Harry Potter and the Half-Blood Prince","J K Rowling",genre,330.00,"9780439785969"));
    //     books.add(new Book("Harry Potter and the Deathly Hallows","J K Rowling",genre,350.00,"9780545010221"));

    //     genre.clear();
    //     genre.add("Fiction");
    //     genre.add("Young Adult Fantasy");
    //     genre.add("Contemporary Fantasy");
    //     genre.add("Urban Fantasy");
    //     books.add(new Book("The Mortal Instruments: City of Bones","Cassandra Clare",genre,310.00,"9781534406254"));
    //     books.add(new Book("The Mortal Instruments: City of Ashes","Cassandra Clare",genre,320.00,"9780606106405"));
    //     books.add(new Book("The Mortal Instruments: City of Glass","Cassandra Clare",genre,323.00,"9780606107235"));
    //     books.add(new Book("The Mortal Instruments: City of Fallen Angels","Cassandra Clare",genre,320.00,"9780606269087"));
    //     books.add(new Book("The Mortal Instruments: City of Lost Souls","Cassandra Clare",genre,340.00,"9781406332940"));
    //     books.add(new Book("The Mortal Instruments: City of Heavenly Fire","Cassandra Clare",genre,350.00,"9780606371384"));

    //     genre.clear();
    //     genre.add("Fiction");
    //     genre.add("Science Fiction");
    //     books.add(new Book("Skyward","Brandon Sanderson",genre,290.00,"9780399555770"));
    //     books.add(new Book("Starsight","Brandon Sanderson",genre,290.00,"9780399555817"));
    //     Book.saveBooks(books);
    // }
}
    

    
