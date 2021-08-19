import java.util.*;

/**
  * AdminOptions.java
  *
  * It will handle the UI shown to the
  * Admin.
  */
public class AdminOptions
{
    private static Scanner sc = null;
    private Admin admin = null;
    private AuthService auth = null;
    Customer c = new Customer();
    CustomerOptions custOptions = null;

    AdminOptions(Admin a, AuthService auth, Scanner sc1)
    {
        this.admin = a;
        this.auth = auth;
        sc = sc1;
    }

    // Shows the home UI to the Admin, lists all the functions they can
    // perform
    public void showFunctions()
    {
        while (true)
        {
            System.out.println(admin + "\n");
            System.out.println("What would you like to do?");
            System.out.println("1. See list of all customers");
            System.out.println("2. See list of all books along with their copies");
            System.out.println("3. See the complete details of a book");
            System.out.println("4. Modify the details of a book or add a book");
            System.out.println("5. Remove a book");
            System.out.println("6. See the history of a customer");
            System.out.println("7. Delete an account of a customer");
            System.out.println("8. Log out");
            System.out.print("\nEnter option number: ");
            int choice = 0;
            try
            {
                choice = Integer.parseInt(sc.nextLine());
            }
            catch (NumberFormatException e)
            {
                auth.error();
                continue;
            }

            // All functions which the admin can do
            switch (choice)
            {
                case 1: showCustomers();
                        break;

                case 2: seeBookList();
                        break;

                case 3: showBook();
                        break;

                case 4: modifyDetails();
                        break;

                case 5: removeBook();
                        break;

                case 6: seeHistory();
                        break;

                case 7: removeAcc();
                        break;

                case 8: System.out.println("Logging you out...");
                        AuthService.sleep(1);
                        Main.main(null);

                default: System.out.println("That's an invalid option. Try " +
                            "again.\n");
            }
        }
    }

    // Remove a user's account with consent from the user and no pending dues
    public void removeAcc()
    {
        Customer cust = null;

        do
        {
            System.out.print("\nEnter the username of customer: ");
            String uname = sc.nextLine();

            cust = findCustomer(uname);
            if (cust == null)
                System.out.println("No such customer exists! Try again.");
            continue;
        }
        while (cust == null);

        System.out.println(cust);
        String ch;
        for (Book book : Catalogue.getBooks())
        {
            // Customer should have no dues
            if (!cust.getTransaction().isReturned(book))
            {
                System.out.println("There are still books left to be returned! Returning to home screen.");
                return;
            }
        }

        do
        {
            System.out.print("Delete this account? (Y/N): ");
            ch = sc.nextLine();
            if (ch.equalsIgnoreCase("Y"))
            {
                c.getCustomers().remove(cust);

                // Update data file
                Customer.saveCustomer();

                System.out.println("Account deleted successfully!\n");
                return;
            }
            else if (ch.equalsIgnoreCase("N"))
                return;
            else
                System.out.println("That's an invalid option. Try again.\n");
        }
        while (true);
    }

    // Show a particular book, its details and list of borrowers and buyers
    public void showBook()
    {
        Book book = null;
        do
        {
            System.out.println("\nEnter the title of the book: ");
            ArrayList<Book> books = Catalogue.getBooks();
            String title = sc.nextLine().trim();
            for (Book b : books)
            {
                if (b.getTitle().equalsIgnoreCase(title))
                {
                    book = b;
                    break;
                }
            }

            if (book == null)
                System.out.println("No Book Found! Please try again!");
        }
        while (book == null);
        System.out.println(book.display());
        return;
    }

    // See the history of a customer: personal details, list of books bought
    // and borrowed
    public void seeHistory() {
        Customer cust = null;

        do
        {
            System.out.print("\nEnter the username of customer: ");
            String uname = sc.nextLine();
            AuthService.sleep(1);

            cust = findCustomer(uname);

            if (cust == null)
            {
                System.out.println("No such customer exists! Try again.");
                continue;
            }
        }
        while (cust == null);

        custOptions = new CustomerOptions(cust, auth, sc);
        System.out.println(cust);
        custOptions.showHistory();
    }

    // Find a customer on basis of username
    public Customer findCustomer(String uname)
    {
        for (Customer customer : c.getCustomers())
            if (customer.getUsername().equals(uname))
                return customer;
        return null;
    }

    // See the entire catalogue of books
    private void seeBookList()
    {
        ArrayList<Book> bookList = Catalogue.getBooks();
        for (Book b : bookList)
            System.out.println(b + "Copies : " + b.getNumCopies());

        System.out.println("\n---------------------------------");
    }

    // Modify the details of any particular book, or add a new book
    private void modifyDetails()
    {
        System.out.print("\nEnter title: ");
        String title = sc.nextLine().trim();

        System.out.println("Enter authors (write \"exit\" when done):");
        ArrayList<String> authorList = new ArrayList<>();
        CustomerOptions.addToList(authorList, sc);
        System.out.println();

        System.out.println("Enter genre (write \"exit\" when done):");
        ArrayList<String> genreList = new ArrayList<>();
        CustomerOptions.addToList(genreList, sc);
        System.out.println();

        double price = 0;
        do
        {
            try
            {
                System.out.print("Enter price: ");
                price = Double.parseDouble(sc.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("That's an invalid price. Try again.\n");
                continue;
            }
            break;
        }
        while (true);

        String ISBN = "";
        do
        {
            System.out.print("Enter ISBN: ");
            ISBN = sc.nextLine();
            if (!Catalogue.checkISBN(ISBN))
            {
                System.out.println("Invalid ISBN. Try again.\n");
                continue;
            }
            break;
        }
        while (true);

        AuthService.sleep(1);
        Book b = new Book(title, authorList, genreList, price, ISBN);
        System.out.println("Book:\n" + b);

        // If it is an ISBN which already exists in the database, it means we
        // are modifying the details of some old book. If it is a new ISBN
        // means it is a new book
        if (hasBook(ISBN))
        {
            String choice;
            do
            {
                System.out.print("This ISBN already exists in the database. Update book (Y/N)? ");
                choice = sc.nextLine().trim();
                if (choice.equalsIgnoreCase("y"))
                {
                    addBook(b);
                    break;
                }
                else if (choice.equalsIgnoreCase("n"))
                    return;
                else
                    System.out.println("Invalid input. Try again.\n");
            }
            while (true);
        }
        else
            addBook(b);

        System.out.println("---------------------------------");
    }

    // remove a book if it is not borrowed currently
    private void removeBook()
    {
        boolean flag = true;
        ArrayList<String> isbnList = new ArrayList<>();
        System.out.println("\nEnter ISBNs of books to remove (\"exit\" to " +
            "stop):");
        CustomerOptions.addToList(isbnList, sc);

        ArrayList<Book> removeBooksList = new ArrayList<>();

        ArrayList<Book> booksList = Catalogue.getBooks();
        Iterator<Book> itr = booksList.iterator();

        // this complex looking loop basically checks if no customer has
        // borrowed it currently.
        while (itr.hasNext())
        {
            Book b = (Book) itr.next();
            bookLoop:
            for (String ISBN : isbnList)
                if (b.getISBN().equals(ISBN))
                {
                    for (Customer c : b.getBorrowers())
                        for (Customer cust : c.getCustomers())
                            if (cust.getUsername().equalsIgnoreCase(c.getUsername()))
                                for (Book bk : cust.booksBorrowed)
                                    if (b.getISBN().equalsIgnoreCase(bk.getISBN()))
                                    {
                                        System.out.println("\nBook: " + b.getISBN() + " is on hold, can't be removed.");
                                        flag = false;
                                        break bookLoop;
                                    }
                    itr.remove();
                    removeBooksList.add(b);
                }
        }

        // ISBN is not in database, means book is not existing in the first
        // place!
        if (removeBooksList.size() == 0 && flag)
            System.out.println("\nNo books match these ISBN values!\n");
        else
        {
            System.out.println("Removed books:\n");
            for (Book b : removeBooksList)
                System.out.println(b);
            Catalogue.saveBooks(booksList);
        }

        System.out.println("---------------------------------");
    }

    // Checks if a book is there on basis of ISBN
    private boolean hasBook(String ISBN)
    {
        for (Book b : Catalogue.getBooks())
            if (b.getISBN().equals(ISBN))
                return true;
        return false;
    }

    // Add a new book
    private void addBook(Book book)
    {
        boolean found = false;
        ArrayList<Book> booksList = Catalogue.getBooks();
        for (int i = 0; i < booksList.size(); i++)
        {
            Book b = booksList.get(i);
            if (b.getISBN().equals(book.getISBN()))
            {
                AuthService.sleep(1);
                found = true;
                booksList.set(i, book);
                System.out.println("Updated!\n");
                break;
            }
        }
        if (!found)
        {
            AuthService.sleep(1);
            booksList.add(book);
            System.out.println("Added new book!\n");
        }
        Catalogue.saveBooks(booksList);
    }

    // Show list of customers
    private static void showCustomers()
    {
        Customer c1 = new Customer();
        for (Customer cust : c1.getCustomers())
            System.out.println(cust);
        System.out.println("---------------------------------");
    }
}
