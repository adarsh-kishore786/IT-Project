import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Catalogue {
    // static variable containing a list of all the books in the library
    private static ArrayList<Book> booksList = new ArrayList<Book>();
    static String booksFile = FileSystems.getDefault().getPath(System.getProperty("user.dir"), "dat/booksFile.dat").toString();

    public static boolean checkISBN(String ISBN) {
        ISBN = ISBN.trim();

        // length of book ISBNs is 13
        if (ISBN.length() != 13)
            return false;

        // weighted sum must be divisible by 10
        // digits are multiplied with 1 and 3 alternatively
        int sum = 0;
        for (int i = 0; i < 13; i++) {
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
    public static ArrayList<Book> getBooks() {
        initBooks();
        return booksList;

    }

    // initializes the books list with all the books in the file
    @SuppressWarnings("unchecked")
    private static void initBooks() {
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

        } catch (EOFException eofe) {
            System.out.println(eofe);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ie) {
            System.err.println(ie);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void saveBooks() {
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
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ie) {
            System.err.println(ie);
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    public static void saveBooks(ArrayList<Book> books) {
        booksList = books;
        saveBooks();
    }

    // returns a book object with the argument as its title
    public static Book getBookWithTitle(String title) {
        initBooks();

        for (int i = 0; i < booksList.size(); i++)
            if (booksList.get(i).getTitle().equalsIgnoreCase(title.trim()))
                return booksList.get(i);

        return null;
    }

    static ArrayList<Book> getBookUnderPrice(double price) {
        initBooks();
        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        for (Book b : booksList) {
            if (b.getPrice() < price)
                filteredList.add(b);
        }
        return filteredList;
    }

    static ArrayList<Book> getBookWithAuthor(String[] authors) {
        initBooks();

        ArrayList<Book> filteredList = new ArrayList<Book>();// contains req list
        List<String> authorSearchList = Arrays.asList(authors);
        for (Book b : booksList) {
            ArrayList<String> bookAuthorList = b.getAuthor();
            for (String author : authorSearchList) {
                if (bookAuthorList.stream().anyMatch(author.trim()::equalsIgnoreCase)) {
                    filteredList.add(b);
                    break;
                }
            }
        }

        return filteredList;
    }

    static ArrayList<Book> getBookWithGenre(String[] genres) {
        initBooks();

        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        List<String> genreList = Arrays.asList(genres);

        for (Book b : booksList) {

            ArrayList<String> bookGenreList = b.getGenre();

            for (String genre : genreList) {
                if (bookGenreList.stream().anyMatch(genre.trim()::equalsIgnoreCase)) {
                    filteredList.add(b);
                    break;
                }
            }
        }
        return filteredList;
    }

    static ArrayList<Book> getIfAvailable() {
        initBooks();

        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        for (Book b : booksList) {
            if (b.isAvailable())
                filteredList.add(b);
        }
        return filteredList;
    }

    static ArrayList<Book> searchByISBN(String[] ISBN) {
        initBooks();

        ArrayList<Book> filteredList = new ArrayList<Book>(); // contains req list
        List<String> isbnList = Arrays.asList(ISBN);
        for (Book b : booksList) {
            if (isbnList.contains(b.getISBN()))
                filteredList.add(b);
        }
        return filteredList;
    }


}
