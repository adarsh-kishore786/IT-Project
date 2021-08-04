import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.time.LocalDate;
import java.io.*;

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

  private boolean checkStringIsANumber(String s) {
    boolean check = true;
    for (int i = 0; i < s.length(); i++)
      if (Character.isDigit(s.charAt(i))) {
        check = false;
        break;
      }
    return check;
  }

  private boolean payment(double amount) {
    Scanner details = new Scanner(System.in);
    boolean status = false;
    // long cardNumber, n;
    String cardNumber, CVV;
    int count = 0;
    char choice = 'Y';
    String expiryDate = new String();
    LocalDate nowDate = LocalDate.now();
    int m = nowDate.getMonthValue();
    int y = nowDate.getYear();
    y %= 100;
    // String s = String.format("%2s/%2s", m, y);
    // About the two warnings, it's because I just used those variables for input,
    // kept it that way so that we can update our program
    do {
      System.out.println("Enter debit/credit card number: ");
      cardNumber = details.nextLine();
      count = cardNumber.length();
      if (count != 16 || !checkStringIsANumber(cardNumber))
        System.out.println("Invalid card number!");
      else {
        count = 0;
        System.out.println("Enter Expiry Date in MM/YY format: ");
        expiryDate = details.nextLine();
        String temp1 = expiryDate.substring(0, 2);
        String temp2 = expiryDate.substring(3, 5);
        if (Integer.parseInt(temp2) < y)
          System.out.println("Invalid expiry date!");
        else if (Integer.parseInt(temp2) == y) {
          if ((Integer.parseInt(temp1) <= m))
            System.out.println("Invalid expiry date!");
        } else {
          // Try checking if it is beyond today's date? (A possible expansion)
          System.out.println("Enter CVV/CVC: ");
          CVV = details.nextLine(); // Later check if 3 digits or not (A possible exapansion)

          if (CVV.length() == 3 && checkStringIsANumber(CVV)) {
            System.out.println("Processing transaction... ");
            Main.sleep(1);
            System.out.println("Transaction Successful!");
            status = true;
          } else
            System.out.println("Invalid CVV/CVC!");
        }

      }
      if (status == false) {
        System.out.println("Do you want to continue in payment section? Enter Y if yes else enter N: ");
        choice = details.next().charAt(0);
        details.nextLine();
      }

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

  public LocalDate getDateOfBorrow(Book b) {
    return this.dateOfBorrow.get(this.borrowedBooks.indexOf(b));
  }

  public LocalDate getDateOfPurchase(Book b) {
    return this.dateOfPurchase.get(this.boughtBooks.indexOf(b));
  }

  public LocalDate getDateOfReturn(Book b) {
    return this.dateOfReturn.get(this.boughtBooks.indexOf(b));
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
      try {
        b.setNumCopies(newNumberCopies);
      } catch (ClassNotFoundException e) {
        System.err.println(e);
      } catch (IOException e) {
        System.err.println(e);
      }
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
      try {
        b.setNumCopies(newNumberCopies);
      } catch (ClassNotFoundException e) {
        System.err.println(e);
      } catch (IOException e) {
        System.err.println(e);
      }
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
      try {
        borrowedBooks.get(index).setNumCopies(newNumberCopies); // I am a little doubtful about this, need to test
                                                                // with data, does it update for the book as such or
                                                                // just update and keep in my array list? Is it
                                                                // synchronized basically?
      } catch (ClassNotFoundException e) {
        System.err.println(e);
      } catch (IOException e) {
        System.err.println(e);
      }

      dateOfReturn.add(index, calculateDate); // A test here too?
    }
    return fine;
  }
}
