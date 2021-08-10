import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.time.LocalDate;
import java.io.*;

public class Transaction implements Serializable {


  private ArrayList<LocalDate> dateOfBorrow;
  private ArrayList<LocalDate> dateOfReturn;
  private ArrayList<LocalDate> dateOfPurchase;
  private ArrayList<Book> borrowedBooks;
  private ArrayList<Book> boughtBooks;
  private ArrayList<Boolean> isReturned; // So it will have isReturned one to one map to
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
      if (!Character.isDigit(s.charAt(i))) {
        check = false;
        break;
      }
    return check;
  }

  private boolean payment(double amount) {
    boolean status = false;
    String cardNumber, CVV;
    int count = 0;
    char choice = 'N';
    String expiryDate = new String();
    LocalDate nowDate = LocalDate.now();
    int m = nowDate.getMonthValue();
    int y = nowDate.getYear();
    y %= 100;
    do {
      System.out.println("Enter debit/credit card number: ");
      cardNumber = Main.sc.nextLine();
      count = cardNumber.length();
      if (count != 16 || !checkStringIsANumber(cardNumber))
        System.out.println("Invalid card number!\n");
      else {
        count = 0;
        System.out.println("Enter Expiry Date in MM/YY format: ");
        expiryDate = Main.sc.nextLine();
        String temp1 = expiryDate.substring(0, 2);
        String temp2 = expiryDate.substring(3, 5);
        if (Integer.parseInt(temp2) < y)
          System.out.println("Invalid expiry date!\n");
        else if (Integer.parseInt(temp2) == y && Integer.parseInt(temp1) <= m) {
            System.out.println("Invalid expiry date!\n");
        } else {
          System.out.println("Enter CVV/CVC: ");
          CVV = Main.sc.nextLine();
          if (CVV.length() == 3 && checkStringIsANumber(CVV)) {
            System.out.println("Processing transaction...\n");
            Main.sleep(1);
            System.out.println("Transaction Successful!\n");
            status = true;
          } else
            System.out.println("Invalid CVV/CVC!\n");
        }

      }
      if (status == false) {
        System.out.println("Do you want to continue in payment section? Enter Y if yes else enter N: ");
        choice = Main.sc.next().charAt(0);
        Main.sc.nextLine();
      }
      else
        break;

    } while (choice == 'Y');
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
    int index=-1;
    for(Book book:borrowedBooks){
      if(book.getTitle().equals(b.getTitle())) index=this.borrowedBooks.indexOf(book);
    }
    return this.dateOfBorrow.get(index);
  }

  public LocalDate getDateOfPurchase(Book b) {
    int index=-1;
    for(Book book:boughtBooks){
      if(book.getTitle().equals(b.getTitle())) index=this.boughtBooks.indexOf(book);
    }
    return this.dateOfPurchase.get(index);
  }

  public LocalDate getDateOfReturn(Book b) {
    int index=-1;
    for(Book book:borrowedBooks){
      if(book.getTitle().equals(b.getTitle())) index=this.borrowedBooks.indexOf(book);
    }
    return this.dateOfReturn.get(index);
  }

  public boolean isReturned(Book b){
    int index=-1;
    for(Book book:borrowedBooks){
      if(book.getTitle().equals(b.getTitle())) index=this.borrowedBooks.indexOf(book);
    }
    return this.getIsReturned().get(index);
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
      isReturned.add(false);
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
    double fine = 0.0;
    LocalDate calculateDate = LocalDate.now();
    numberOfDays = calculateDate.compareTo(dateOfBorrow.get(index));
    check = numberOfDays - Admin.getMaxBorrowDays();
    if (check>0) {
      System.out.println("Number of days borrowed exceeds limit! You need to pay a fine!");
      fine = Admin.getFineRate() * check;
      System.out.println("The amount you need to pay is: Rs. " + fine + " ");
      statusPayment = payment(fine);

      if (!statusPayment)
          fine = -1;
    }
    if (statusPayment == true) {
      isReturned.set(index, true);
      // newNumberCopies = borrowedBooks.get(index).getNumCopies();
      // ++newNumberCopies;
      // borrowedBooks.get(index).setNumCopies(newNumberCopies);

      dateOfReturn.add(index, calculateDate);

      ArrayList<Book> books = Catalogue.getBooks();
     for (Book b : books)
     {
         if (b.getISBN().equals(borrowedBooks.get(index).getISBN()))
         {
            newNumberCopies = b.getNumCopies();
            b.setNumCopies(++newNumberCopies);
            break;
        }
     }
    }
    return fine;
  }

  public static boolean custAdd()
  {
      Transaction t = new Transaction();
      return t.payment(100);
  }
}
