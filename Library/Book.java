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
        String s="\nTitle  : "+m_title+"\nAuthor : "+showAuthor()+"\nGenre  : "+showGenre()+"\nPrice  : "+String.format("%.2f", m_price)+"\nISBN   : "+m_ISBN
            + "\n";
        return s;
    }

    public String display()
    {
        String s="\nTitle  : "+m_title+"\nAuthor : "+showAuthor()+"\nGenre  : "+showGenre()+"\nPrice  : "+String.format("%.2f", m_price)+"\nISBN   : "+m_ISBN+ "\n";
        s+="\nNumber of Copies: "+m_numCopies+"\nBorrowers: \n"+showBorrowers()+"\nBuyers: \n"+showBuyers()+"\n";
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

    private String showBorrowers()
    {
        String s="";
        int i=1;
        for(Customer cust:m_borrowersList)
        {
            s=s+"\n"+i+"\n"+cust.toString();
            s=s+"Returned :";
            for(Customer c:cust.getCustomers())
            {
                if(cust.m_username.equalsIgnoreCase(c.m_username))
                {
                    if(c.transaction.isReturned(this))
                        s+=" Yes"+"\n";
                    else
                        s+=" No"+"\n";
                break;        
                }
            }
            i++;
        }
        if(s.equalsIgnoreCase(""))
            s="No Borrowers!";
        return s;
    }

    private String showBuyers()
    {
        String s="";
        int i=1;
        for(Customer c:m_buyersList)
        {
            s=s+"\n"+i+"\n"+c.toString();
            i++;
        }
        if(s.equalsIgnoreCase(""))
            s="No Buyers!";
        return s;
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
        Catalogue.saveBooks();
    }

    public void setAuthor(ArrayList<String> author)
    {
        m_author.addAll(author);
        Catalogue.saveBooks();
    }

    public void setISBN(String ISBN)
    {
        if(Catalogue.checkISBN(ISBN))
        {
            m_ISBN=ISBN;
            Catalogue.saveBooks();
        }
        else
            System.out.println("Inavlid ISBN input! Please try again.");
        Catalogue.saveBooks();
    }

    public void setPrice(double price)
    {
        m_price=price;
        Catalogue.saveBooks();
    }

    public void setGenre(ArrayList<String> genre)
    {
        m_genre.addAll(genre);
        Catalogue.saveBooks();
    }

    public void setNumCopies(int numCopies)
    {
        m_numCopies=numCopies;
        Catalogue.saveBooks();
    }

    public void setBorrowers(Customer borrower)
    {
        m_borrowersList.add(borrower);
        Catalogue.saveBooks();
    }

    public void setBuyers(Customer buyer)
    {
        m_buyersList.add(buyer);
        Catalogue.saveBooks();
    }

}
