import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
  * CustomerOptions.java
  *
  * It will handle the UI shown to the
  * Customer.
  */
public class CustomerOptions
{
    private static Scanner sc = null;
    private Customer c = null;
    private AuthService auth = null;
    private int transaction;
    private static int choiceCatalogue = 0;

    CustomerOptions(Customer cust, AuthService auth, Scanner sc1)
    {
        this.c = cust;
        this.auth = auth;
        sc = sc1;
        transaction = 0;
    }

    // borrow, buy, return, view history, logout
    public void showOptions()
    {
        while (true)
        {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1. View Catalogue");
            System.out.println("2. Borrow A Book");
            System.out.println("3. Return A Book");
            System.out.println("4. Buy A Book");
            System.out.println("5. View List of Books You've Currently Borrowed");
            System.out.println("6. View History");
            System.out.println("7. User Manual");
            System.out.println("8. Log Out");
            System.out.print("\nEnter the option number: ");
            int choice = 0;

            try
            {
                choice = Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("That's an invalid option. Try again.\n");
                continue;
            }

            // all options that customer can use
            switch (choice)
            {
                case 1: choiceCatalogue = 1;
                        showCatalogue();
                        break;

                case 2: transaction = 1;
                        choiceCatalogue = 0;
                        showCatalogue();
                        break;

                case 3: transaction = 2;
                        choiceCatalogue = 0;
                        showBorrowedBooks();
                        break;

                case 4: transaction = 3;
                        choiceCatalogue = 0;
                        showCatalogue();
                        break;

                case 5: showBorrowDetails();
                        break;

                case 6: showHistory();
                        break;

                case 7: viewManual();
                        break;

                case 8: System.out.println();
                        auth.logout();
                        break;

                default: System.out.println("That's an invalid option. Try again.\n");
            }
            transaction = 0;
        }
    }

    // filter based on price, author, genre, availability, ISBN
    private void showCatalogue()
    {
        while (true)
        {
            System.out.println("\n1. Get book by its title");
            System.out.println("2. Filter Books under a price");
            System.out.println("3. Filter Books based on authors");
            System.out.println("4. Filter Books based on genre");
            System.out.println("5. Filter Books based on availability");
            System.out.println("6. Search for Books with ISBN");
            System.out.println("7. Back");
            System.out.print("\nEnter the option number: ");

            int choice = 0;
            try
            {
                choice = Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("That's an invalid option. Try again.\n");
                continue;
            }

            switch (choice)
            {
                case 1: filterByTitle();
                        return;

                case 2: filterByPrice();
                        return;

                case 3: filterByAuthors();
                        return;

                case 4: filterByGenre();
                        return;

                case 5: filterByAvailability();
                        return;

                case 6: filterByISBN();
                        return;

                case 7: System.out.println();
                        showOptions();

                default: System.out.println("That's an invalid option. Try again.\n");
            }
        }
    }

    // filter functions
    private void filterByTitle()
    {
        Book selectedBook = null;
        String title;
        ArrayList<Book> books = Catalogue.getBooks();
        System.out.println("Enter the title of the book: ");
        title = sc.nextLine().trim();

        for (Book b : books)
        {
            if (b.getTitle().equalsIgnoreCase(title))
            {
                selectedBook = b;
                break;
            }
        }

        if (selectedBook == null)
        {
            System.out.println("\nNo Book Found!");
            System.out.println("Going back to Catalogue:\n");
            showCatalogue();
        }

        System.out.println(selectedBook);

        if (choiceCatalogue == 0)
            confirmBook(selectedBook);
        else
        {
            System.out.println("\nGo (B)ack to filters.");
            String ch = "";
            do
            {
                try
                {
                    ch = sc.nextLine();
                    if (!ch.equalsIgnoreCase("b"))
                    throw new NoSuchElementException();
                }
                catch (NoSuchElementException nsee)
                {
                    System.out.println("Invalid input. Please try again (B/b).");
                }
            }
            while (!ch.equalsIgnoreCase("b"));
            showCatalogue();
        }
        return;
    }

    private void filterByPrice()
    {
        double price;
        Book selectedBook;
        while (true)
        {
            System.out.print("Enter price: ");
            try
            {
                price = Double.parseDouble(sc.nextLine());
                ArrayList<Book> booklist = Catalogue.getBookUnderPrice(price);
                if (booklist.isEmpty())
                {
                    System.out.println("\nNo Book Found!");
                    System.out.println("Try again with a valid price.\n");
                    continue;
                }

                showBooks(booklist);
                if (choiceCatalogue == 0)
                {
                    selectedBook = selectBook(booklist);
                    confirmBook(selectedBook);
                }
                else
                {
                    System.out.println("\nGo (B)ack to filters.");
                    String ch = "";
                    do
                    {
                        try
                        {
                            ch = sc.nextLine();
                            if (!ch.equalsIgnoreCase("b"))
                            throw new NoSuchElementException();
                        }
                        catch (NoSuchElementException nsee)
                        {
                            System.out.println("Invalid input. Please try again (B/b).");
                        }
                    }
                    while (!ch.equalsIgnoreCase("b"));
                    showCatalogue();
                }
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("That's an invalid price. Try again.\n");
                continue;
            }
        }
    }

    private void filterByAuthors()
    {
        ArrayList<String> authorList = new ArrayList<String>();
        Book selectedBook;

        System.out.println("\nEnter Authors(type \"exit\" when done): ");
        addToList(authorList, sc);
        String[] authorArray = new String[authorList.size()];
        ArrayList<Book> booklist = Catalogue.getBookWithAuthor(authorList.toArray(authorArray));

        if (booklist.isEmpty())
        {
            System.out.println("\nNo Book Found!");
            System.out.println("Going back to Catalogue:\n");
            showCatalogue();
        }

        showBooks(booklist);
        if (choiceCatalogue == 0)
        {
            selectedBook = selectBook(booklist);
            confirmBook(selectedBook);
        }
        else
        {
            System.out.println("\nGo (B)ack to filters.");
            String ch = "";
            do
            {
                try {
                    ch = sc.nextLine();
                    if (!ch.equalsIgnoreCase("b"))
                    throw new NoSuchElementException();
                }
                catch (NoSuchElementException nsee)
                {
                    System.out.println("Invalid input. Please try again (B/b).");
                }
            }
            while (!ch.equalsIgnoreCase("b"));
            showCatalogue();
        }
    }

    private void filterByGenre()
    {
        ArrayList<String> genreList = new ArrayList<String>();
        Book selectedBook;

        System.out.println("\nEnter Genres(type \"exit\" when done): ");
        addToList(genreList, sc);
        String[] genreArray = new String[genreList.size()];
        ArrayList<Book> booklist = Catalogue.getBookWithGenre(genreList.toArray(genreArray));

        if (booklist.isEmpty())
        {
            System.out.println("\nNo Book Found!");
            System.out.println("Going back to Catalogue:\n");
            showCatalogue();
        }

        showBooks(booklist);
        if (choiceCatalogue == 0)
        {
            selectedBook = selectBook(booklist);
            confirmBook(selectedBook);
        }
        else
        {
            System.out.println("\nGo (B)ack to filters.");
            String ch = "";
            do
            {
                try
                {
                    ch = sc.nextLine();
                    if (!ch.equalsIgnoreCase("b"))
                    throw new NoSuchElementException();
                }
                catch (NoSuchElementException nsee)
                {
                    System.out.println("Invalid input. Please try again (B/b).");
                }
            }
            while (!ch.equalsIgnoreCase("b"));
            showCatalogue();
        }
    }

    private void filterByISBN()
    {
        ArrayList<String> ISBNList = new ArrayList<String>();
        Book selectedBook = null;

        String val = "";
        System.out.println("\nEnter ISBNs(type \"exit\" when done): ");
        do
        {
            try
            {
                    val = sc.nextLine().trim();
                    if (Catalogue.checkISBN(val) || val.equalsIgnoreCase("exit"))
                        ISBNList.add(val);
                    else
                        throw new NoSuchElementException();
            }
            catch (NoSuchElementException e)
            {
                System.out.println("That's an invalid input. Try again.\n");
                continue;
            }
        }
        while (!val.equalsIgnoreCase("exit"));
        ISBNList.remove(ISBNList.size() - 1);

        String[] ISBNArray = new String[ISBNList.size()];
        ArrayList<Book> booklist = Catalogue.searchByISBN(ISBNList.toArray(ISBNArray));
        if (booklist.isEmpty())
        {
            System.out.println("\nNo Book Found!");
            System.out.println("Going back to Catalogue:\n");
            showCatalogue();
        }
        showBooks(booklist);
        if (choiceCatalogue == 0)
        {
            selectedBook = selectBook(booklist);
            confirmBook(selectedBook);
        }
        else
        {
            System.out.println("\nGo (B)ack to filters.");
            String ch = "";
            do
            {
                try
                {
                    ch = sc.nextLine();
                    if (!ch.equalsIgnoreCase("b"))
                    throw new NoSuchElementException();
                }
                catch (NoSuchElementException nsee)
                {
                    System.out.println("Invalid input. Please try again (B/b).");
                }
            }
            while (!ch.equalsIgnoreCase("b"));
            showCatalogue();
        }
    }

    private void filterByAvailability()
    {
        ArrayList<Book> booklist = Catalogue.getIfAvailable();
        if (booklist.isEmpty())
        {
            System.out.println("\nNo Book Found!");
            return;
        }
        showBooks(booklist);
        Book selectedBook;

        if (choiceCatalogue == 0)
        {
            selectedBook = selectBook(booklist);
            confirmBook(selectedBook);
        }
        else
        {
            System.out.println("\nGo (B)ack to filters.");
            String ch = "";
            do
            {
                try
                {
                    ch = sc.nextLine();
                    if (!ch.equalsIgnoreCase("b"))
                    throw new NoSuchElementException();
                }
                catch (NoSuchElementException nsee)
                {
                    System.out.println("Invalid input. Please try again (B/b).");
                }
            }
            while (!ch.equalsIgnoreCase("b"));
            showCatalogue();
        }
    }

    // display borrowed book details
    public void showBorrowDetails()
    {
        if (c.booksBorrowed.isEmpty())
        {
            System.out.println("\nNo Book Found!");
            return;
        }

        int i = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        for (Book b : c.booksBorrowed)
        {
            System.out.println("\n" + i + "\n" + b);
            String borrowDate = c.transaction.getDateOfBorrow(b).format(formatter);
            String maxDate = c.transaction.getDateOfBorrow(b).plusDays(Admin.getMaxBorrowDays()).format(formatter);

            System.out.println("Date when you Borrowed this Book : " + borrowDate);
            System.out.println("Last Date to Return without Fine : " + maxDate);
            System.out.println("\n---------------------------------");
            i++;
        }
    }

    // display history using a HashMap Data Structure
    public void showHistory()
    {
        HashMap<Book, ArrayList<LocalDate>> map = (HashMap<Book, ArrayList<LocalDate>>) c.getHistory();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        if (map.isEmpty())
        {
            System.out.println("\nNO HISTORY!");
            AuthService.sleep(1);
            return;
        }

        int index = 1;
        System.out.println("\nHISTORY:");
        for (Map.Entry<Book, ArrayList<LocalDate>> entry : map.entrySet())
        {
            Book book = (Book) entry.getKey();
            ArrayList<LocalDate> dates = (ArrayList<LocalDate>) entry.getValue();

            if (dates.size() == 2)
            {
                String borrowDate = dates.get(0).format(formatter);
                String returnDate = dates.get(1).format(formatter);

                System.out.println("\n" + index++);
                System.out.println(book);
                System.out.println("Transaction Details: ");
                System.out.println("Borrow Date  : " + borrowDate);
                System.out.println("Return Date  : " + returnDate + "\n");
            }
            else
            {
                String purchaseDate = dates.get(0).format(formatter);
                System.out.println("\n" + index++);
                System.out.println(book);
                System.out.println("Transaction Details: ");
                System.out.println("Bought Date  : " + purchaseDate + "\n");

            }
            System.out.println("---------------------------------");
        }
    }

    // show currently borrowed books(used for returning books)
    private void showBorrowedBooks()
    {
        if (c.booksBorrowed.isEmpty())
        {
            System.out.println("\nNo Book Found!");
            return;
        }

        showBooks(c.booksBorrowed);
        Book selectedBook = selectBook(c.booksBorrowed);
        confirmBook(selectedBook);
    }

    // confirm index of book
    private void confirmBook(Book book)
    {
        String ch = null;
        if (book == null)
        return;

        if (book.getNumCopies() == 0 && transaction != 2) {
            System.out.println("No copies of this book!");
            AuthService.sleep(1);
            return;
        }

        do
        {
            if (transaction != 2)
                System.out.println("\nThere are " + book.getNumCopies() + " copies of this book available.");

            System.out.print("(C)onfirm, (B)ack : ");
            ch = sc.nextLine();

            if (ch.equalsIgnoreCase("c"))
            {
                switch (transaction)
                {
                    case 1: c.borrowBook(book);
                            return;

                    case 2: c.returnBook(book);
                            return;

                    case 3: c.buyBook(book);
                            return;
                }
            }
            else if (ch.equalsIgnoreCase("b"))
            {
                System.out.println();
                return;
            }
            else
                System.out.println("Invalid input, try again.");

            ch = "";
        }
        while (!ch.equalsIgnoreCase("c") && !ch.equalsIgnoreCase("b"));
    }

    // select index of book
    private Book selectBook(ArrayList<Book> booklist)
    {
        int index = 0;
        while (true)
        {
            System.out.print("\nEnter index: ");
            try
            {
                index = Integer.parseInt(sc.nextLine());
                return Catalogue.getBookWithTitle(booklist.get(index - 1).getTitle());
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                System.out.println("Invalid index, try again.\n");
                continue;
            }
        }
    }

    // show books based on filteredList
    private void showBooks(ArrayList<Book> booklist)
    {
        int i = 1;

        for (Book b : booklist) {
            System.out.println(b + "Index  : " + i);
            i++;
        }
    }

    // a private function to handle adding inputs
    public static void addToList(ArrayList<String> arList, Scanner sc)
    {
        String val = "";
        do
        {
            try
            {
                val = sc.nextLine().trim();
                arList.add(val);
            }
            catch (NoSuchElementException e)
            {
                System.out.println("That's an invalid input. Try again.\n");
                continue;
            }
        }
        while (!val.equalsIgnoreCase("exit"));
        arList.remove(arList.size() - 1);
    }

    public static void viewManual()
    {
        System.out.println("\nA list of instructions: \n");
        System.out.println("1. The maximum number of books that can be borrowed at once is 5.");
        System.out.println("2. Each book can be borrowed for a maximum of 14 days.");
        System.out.println("3. After 14 days, fine will be imposed at the rate of Rs 100.00 x days exceeded.");
        System.out.println(
        "4. Be sure to note down your username and password. Your username will be generated and displayed after you sign up. \n\nIMPORTANT: You cannot change your username and password!\n");
    }
}
