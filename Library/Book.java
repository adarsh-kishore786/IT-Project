public class Book 
{
    private final String m_name;
    private final String m_ISBN;
    private final String m_genre;
    private final String m_author;
    
    private double m_price;
    
    public Book(String name, String ISBN, String genre, String author,
                double price)
    {
        m_name = name;
        m_ISBN = ISBN;
        m_genre = genre;
        m_author = author;

        // price < 0 ?
        m_price = price;
    }

    // Getter functions for each and setter for m_price
    // toString() function for book details
}
