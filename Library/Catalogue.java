import java.io.*;
import java.util.*;

public class Catalogue 
{
    // static variable containing a list of all the books in the library
    private static ArrayList<Book> booksList = new ArrayList<Book>();
    static String booksFile="booksFile.dat";

    public static boolean checkISBN(String ISBN) 
    {
        ISBN = ISBN.trim();

        // length of book ISBNs is 13
        if (ISBN.length() != 13)
            return false;

        // weighted sum must be divisible by 10
        // digits are multiplied with 1 and 3 alternatively
        int sum = 0;
        for (int i = 0; i < 13; i++) 
        {
            if (i % 2 == 0)
                sum += Character.valueOf(ISBN.charAt(i));
            else
                sum = sum + (3 * (Character.valueOf(ISBN.charAt(i))));
        }

        if (sum % 10 == 0)
            return true;
        return false;
    }

    // returns an arraylist of all the books
    public static ArrayList<Book> getBooks() throws IOException, ClassNotFoundException 
    {
        //booksInfo();
        initBooks();
        return booksList;
    }

    // returns a book object with the argument as its title
    public static Book getBookWithTitle(String title) throws ClassNotFoundException, IOException 
    {
        initBooks();

        for (int i = 0; i < booksList.size(); i++)
            if (booksList.get(i).getTitle().equalsIgnoreCase(title.trim()))
                return booksList.get(i);

        return null;
    }

    // initializes the books list with all the books in the file
    private static void initBooks() throws IOException, ClassNotFoundException 
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        booksList.clear();

        try {
            fis = new FileInputStream(booksFile);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            // if(o instanceof ArrayList<?> )
            booksList = (ArrayList<Book>) o;
            // while ((object = ois.readObject()) instanceof Book)
            // {
            // if(object instanceof Book)
            // {
            // Book book = (Book) object;
            // booksList.add(book);
            // }
            // }

        } 
        catch (EOFException eofe) {
            System.out.println(eofe);
        } 
        catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } 
        catch (IOException ie) {
            System.err.println(ie);
        } 
        catch (Exception e) {
            System.err.println(e);
        } 
        finally {
            if (fis != null)
                fis.close();
            if (ois != null)
                ois.close();
        }
    }

    public static void saveBooks() throws IOException, ClassNotFoundException 
    {
        // streams objects required
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        // writing the booksList into the file with all the updated information
        if (booksList == null)
            initBooks();

        try {
            // file containing books arraylist-booksFile.dat passed to the stream
            fos = new FileOutputStream(booksFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(booksList);
        } 
        catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } 
        catch (IOException ie) {
            System.err.println(ie);
        } 
        finally {
            if (fos != null)
                fos.close();
            if (oos != null)
                oos.close();
        }

    }

    public static void saveBooks(ArrayList<Book> books) throws IOException, ClassNotFoundException 
    {
        // if the arraylist is null, then all the books already in the file are stored
        // in the list
        if (booksList == null)
            initBooks();

        // updating the static books list with the new books in the arraylist
        booksList.addAll(books);

        saveBooks();
    }

    
    // TODO format returned book list
    static ArrayList<Book> getBookUnderPrice(double price) 
    {
        try {
            initBooks();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        for (Book b : booksList) {
            if (b.getPrice() < price)
                filteredList.add(b);
        }
        return filteredList;
    }

    // TODO trim author parameter
    static ArrayList<Book> getBookWithAuthor(String[] authors) {
        try {
            initBooks();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        List<String> authorList = Arrays.asList(authors);
        for (Book b : booksList) {
            ArrayList<String> bookAuthorList = b.getAuthor();
            for (String author : authorList) {
                if (bookAuthorList.contains(author)) {
                    filteredList.add(b);
                    break;
                }
            }
        }
        return filteredList;
    }

    static ArrayList<Book> getBookWithGenre(String[] genres) {
        try {
            initBooks();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        List<String> genreList = Arrays.asList(genres);

        for (Book b : booksList) {

            ArrayList<String> bookGenreList = b.getGenre();

            for (String genre : genreList) {
                if (bookGenreList.contains(genre)) {
                    filteredList.add(b);
                    break;
                }
            }
        }
        return filteredList;
    }

    static ArrayList<Book> getIfAvailable() {
        try {
            initBooks();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        for (Book b : booksList) {
            if (b.isAvailable())
                filteredList.add(b);
        }
        return filteredList;
    }

    static ArrayList<Book> searchByISBN(String[] ISBN) {
        try {
            initBooks();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        List<String> isbnList = Arrays.asList(ISBN);
        for (Book b : booksList) {
            if (isbnList.contains(b.getISBN()))
                filteredList.add(b);
        }
        return filteredList;
    }

    // private static void booksInfo() throws IOException, ClassNotFoundException
    // {
    //     ArrayList<Book> books=new ArrayList<Book>();
    //     ArrayList<String> genre=new ArrayList<String>();
    //     ArrayList<String> author=new ArrayList<String>();
    //     genre.add("Fiction");
    //     genre.add("Suspense");
    //     genre.add("Thriller");
    //     genre.add("Romance");
    //     author.add("Dan Brown");

    //     Book obj1=new Book("Angels and Demons",author , genre, 258.00,
    //     "9781416524793");

    //     Book obj2=new Book("The Da Vinci Code", author, genre, 246.00,
    //     "9780375432309");

    //     genre.clear();
    //     author.clear();
    //     books.add(obj1);
    //     books.add(obj2);
    //     genre.add("Fiction");
    //     genre.add("Historical");
    //     author.add("Kristin Harmel");
    //     books.add(new Book("The Forest of Vanishing Stars", author,
    //     genre, 450.0, "9781982158934"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Mystery");
    //     genre.add("Detective");
    //     genre.add("Fiction");
    //     genre.add("Historical");
    //     author.add("Stuart Turton");
    //     books.add(new Book("The Devil and the Dark Water", author,
    //     genre, 548.48, "9781728234298"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Non-Fiction");
    //     genre.add("Contemporary");
    //     genre.add("Spiritual");
    //     author.add("Yaa Gyasi");
    //     books.add(new Book("Transcedental Kingdom", author,
    //     genre, 767.36, "9781984899767"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Historical");
    //     genre.add("Fiction");
    //     author.add("Christina Baker Kline");
    //     books.add(new Book("The Exiles", author,
    //     genre, 815.40, "9780062356338"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Non-fiction");
    //     genre.add("Mathematics");
    //     author.add("Joseph Gallian");
    //     books.add(new Book("Contemporary Abstract Algebra",author ,
    //     genre, 490.00, "9789353502522"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Fiction");
    //     genre.add("Science Fiction");
    //     author.add("Isaac Asimov");
    //     books.add(new Book("The End of Eternity", author,
    //     genre, 195.00, "9780449237045"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Fiction");
    //     genre.add("Fantasy");
    //     genre.add("Young Adult Fiction");
    //     genre.add("Drama");
    //     genre.add("Mystery");
    //     author.add("J K Rowling");
    //     books.add(new Book("Harry Potter and the Philosopher's Stone",author,genre,284.00,"9780439362139"));
    //     books.add(new Book("Harry Potter and the Chamber of Secrets",author,genre,285.00,"9780439064873"));
    //     books.add(new Book("Harry Potter and the Prisoner of Azkaban",author,genre,300.00,"9780545582933"));
    //     books.add(new Book("Harry Potter and the Goblet of Fire",author,genre,320.00,"9781338299175"));
    //     books.add(new Book("Harry Potter and the Order of the Phoenix",author,genre,340.00,"9781439520024"));
    //     books.add(new Book("Harry Potter and the Half-Blood Prince",author,genre,330.00,"9780439785969"));
    //     books.add(new Book("Harry Potter and the Deathly Hallows",author,genre,350.00,"9780545010221"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Fiction");
    //     genre.add("Young Adult Fantasy");
    //     genre.add("Contemporary Fantasy");
    //     genre.add("Urban Fantasy");
    //     author.add("Cassandra Clare");
    //     author.add("Joshua Lewis");
    //     books.add(new Book("The Mortal Instruments: City of Bones",author,genre,310.00,"9781534406254"));
    //     books.add(new Book("The Mortal Instruments: City of Ashes",author,genre,320.00,"9780606106405"));
    //     books.add(new Book("The Mortal Instruments: City of Glass",author,genre,323.00,"9780606107235"));
    //     books.add(new Book("The Mortal Instruments: City of Fallen Angels",author,genre,320.00,"9780606269087"));
    //     books.add(new Book("The Mortal Instruments: City of Lost Souls",author,genre,340.00,"9781406332940"));
    //     books.add(new Book("The Mortal Instruments: City of Heavenly Fire",author,genre,350.00,"9780606371384"));

    //     genre.clear();
    //     author.clear();
    //     genre.add("Fiction");
    //     genre.add("Science Fiction");
    //     author.add("Brandon Sanderson");
    //     books.add(new Book("Skyward",author,genre,290.00,"9780399555770"));
    //     books.add(new Book("Starsight",author,genre,290.00,"9780399555817"));
    //     saveBooks(books);
    // }


}
