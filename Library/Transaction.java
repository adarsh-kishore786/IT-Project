import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {

    ArrayList<LocalDate> dateOfBorrow;
    ArrayList<LocalDate> dateOfReturn;
    ArrayList<LocalDate> dateOfPurchase;
    ArrayList<Book> borrowedBooks;
    ArrayList<Book> boughtBooks;
    ArrayList<Boolean> isReturned; // So it will have isReturned one to one map to
                                   // borrowedBooks

    Transaction() {
        dateOfBorrow = new ArrayList<LocalDate>();
        dateOfReturn = new ArrayList<LocalDate>();
        dateOfPurchase = new ArrayList<LocalDate>();
        borrowedBooks = new ArrayList<Book>();
        boughtBooks = new ArrayList<Book>();
        isReturned = new ArrayList<Boolean>();
    }

    private boolean payment(double amount) {
        Scanner details = new Scanner(System.in);
        boolean status = false;
        // long cardNumber, n;
        String cardNumber;
        int count = 0, CVV;
        char choice = 'Y';
        String expiryDate = new String();
        // About the two warnings, it's because I just used those variables for input,
        // kept it that way so that we can update our program
        do {
            System.out.println("Enter debit/credit card number: ");
            cardNumber = details.nextLine();
            /*
             * while (n>0) { ++count; n/=10; }
             */
            count = cardNumber.length();
            if (count != 16)
                System.out.println("Invalid card number!");
            else {
                System.out.println("Enter Expiry Date in MM/YY format: ");
                expiryDate = details.nextLine();
                // Try checking if it is beyond today's date? (A possible expansion)
                System.out.println("Enter CVV/CVC: ");
                CVV = details.nextInt(); // Later check if 3 digits or not (A possible exapansion)
                System.out.println("Processing transaction... ");
                System.out.println("Transaction Successful!");
                status = true;
            }
            System.out.println("Do you want to continue in payment section? Enter Y if yes else enter N: ");
            choice = details.next().charAt(0);
            details.nextLine();
        } while (choice == 'Y');
        details.close();
        return status;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public ArrayList<Boolean> getIsReturned() {
        return isReturned;
    }

    public ArrayList<Book> getBoughtBooks() {
        return boughtBooks;
    }

    public boolean rentBookTransaction(Book b) {
        boolean status = true;
        int newNumberCopies;
        LocalDate newDate = LocalDate.now();
        for (int i = 0; i < borrowedBooks.size(); i++) {
            if (borrowedBooks.get(i).getISBN() == b.getISBN()) {
                if (isReturned.get(i)) {
                    status = false;
                    break;
                }
            }
        }
        if (status == true) {
            borrowedBooks.add(b);
            isReturned.add(false); // It should technically work but check once
            dateOfBorrow.add(newDate);
            newNumberCopies = b.getNumCopies();
            --newNumberCopies; // Decrease number of copies
            b.setNumCopies(newNumberCopies);
        }
        return status;
    }

    public boolean sellBookTransaction(Book b) {
        boolean status = false, statusPayment;
        int newNumberCopies;
        double amount = b.getPrice();
        statusPayment = payment(amount);
        if (statusPayment == true) {
            status = true;
            newNumberCopies = b.getNumCopies();
            --newNumberCopies; // Decrease number of copies
            b.setNumCopies(newNumberCopies);
            boughtBooks.add(b);
            dateOfPurchase.add(LocalDate.now());
        }
        return status;
    }

    public double returnBookTransaction(int index) {
        boolean statusPayment = true;
        int numberOfDays, check, newNumberCopies;
        double fine = -1, amount = 0.0;
        LocalDate calculateDate = LocalDate.now();
        numberOfDays = calculateDate.compareTo(dateOfBorrow.get(index));
        check = numberOfDays - Admin.getMaxBorrowDays();
        if (check > 0) {
            System.out.println("Number of days borrowed exceeds limit! You need to pay a fine!");
            amount = Admin.getFineRate() * check;
            System.out.println("The amount you need to pay is: Rs. " + check + " ");
            statusPayment = payment(amount);
        }
        if (statusPayment == true) {
            fine = check;
            isReturned.set(index, true);
            newNumberCopies = borrowedBooks.get(index).getNumCopies();
            ++newNumberCopies;
            borrowedBooks.get(index).setNumCopies(newNumberCopies); // I am a little doubtful about this, need to test
                                                                    // with data, does it update for the book as such or
                                                                    // just update and keep in my array list? Is it
                                                                    // synchronized basically?
            dateOfReturn.add(index, calculateDate); // A test here too?
        }
        return fine;
    }
}
