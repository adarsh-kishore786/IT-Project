import java.util.*;
import java.io.*;

public class Customer extends Person {
  private static Admin admin=new Admin();
  private static ArrayList<Customer> customerList=new ArrayList<Customer>(); //contains all customer objects; note that it is static
  //private static int borrowLimit;
  private int numBooksBorrowed;
  private int numBooksBought;
  String history=""; //planning to make hisory an array list at a later stage
  ArrayList<Book> booksBorrowed; //initialized in constructor
  ArrayList<Book> booksBought; //initialized in constructor
  Transaction transaction=new Transaction(); //contains history of borrow/return dates with fine; unique to every customer

  Customer(String name,int age,String userName,String password){
    super(name,age,userName,password);
    this.booksBorrowed=new ArrayList<Book>();
    this.booksBought=new ArrayList<Book>();
    // this.booksBorrowed= new Book[numBooksBorrowed];
    // this.booksBought=new Book[numBooksBought];
  }

  //needs update; needs to be set by admin
  int getBorrowLimit(){
    return 100;
  }

  int getNumBooksBorrowed(){
    return this.numBooksBorrowed;
  }

  int getNumBooksBought(){
    return this.numBooksBought;
  }

  void buyBook(Book book){
    //currently no restriction on buying
    Date purchaseDate=new Date(); //returns current date

    //reduce number of copies of book
    // int n=book.getNumCopies();
    // book.setNumCopies(n-1);

    //array lists
      booksBought.add(book); //update array list
      numBooksBought++; //update number of books bought

      //set transaction purchase date
      this.transaction.setDateOfPurchase(purchaseDate);
  }

  void borrowBook(Book book){
    //return current date
    Date borrowDate=new Date();

    //check if limit is respected
    if(booksBorrowed.size()<getBorrowLimit()){
      booksBorrowed.add(book);
      numBooksBorrowed++;

      initializeBorrowTransaction(borrowDate);

      //reduce number of copies of book
      // int n=book.getNumCopies();
      // book.setNumCopies(n-1);


      System.out.println("Added");
    }else System.out.println("Borrow Limit Reached! Please return a book to continue"); //response to limit breach
  }

  void returnBook(Book book){
    Date returnDate=new Date();

    //remove book from list
    booksBorrowed.remove(book);
    numBooksBorrowed--;

    //increase number of copies of book
    int n=book.getNumCopies();
    book.setNumCopies(n+1);

    initializeReturnTransaction(returnDate);

    //update history
    history+=book.getTitle()+" ";
    System.out.println("Returned successfully! \nFine to be paid: "+transaction.getFine());
  }

  String getHistory(){
    return history;
  }

  void initializeBorrowTransaction(Date borrowDate){
    //pass date and return status to transaction
    this.transaction.setDateOfBorrow(borrowDate);
    this.transaction.setIsReturned(false);

  }

  void initializeReturnTransaction(Date returnDate){
    //update transaction object with return date and return status
    this.transaction.setDateOfReturn(returnDate);
    this.transaction.setIsReturned(true);
  }
  //return transaction object
  Transaction getTransaction(){
    return transaction;
  }

  //write customer object to dat file
  void saveCustomer() throws IOException{
    customerList.add(this); //add customer object to list
    ObjectOutputStream os=null; //stream that writes object to file
    try {
      os=new ObjectOutputStream(new FileOutputStream("./src/customer.dat"));
      os.writeObject(customerList); //write array list to file
      os.flush();
    }catch(Exception e){
      e.printStackTrace();
      customerList.remove(this);
    } finally{
      if(os!=null) os.close();
      System.out.println("Saved!");
    }
  }

  //get customer object from list
  static Customer getCustomer(int n){
    return customerList.get(n);
  }

  //following functions assume a books.dat file in /src/books.dat

  ArrayList<Book> getBookUnderPrice(double price){
    //file url can be changed
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(b.getPrice()<price)
        filteredList.add(b);
    }
    return filteredList;
  }

  ArrayList<Book> getBookWithAuthor(String[] authors){
    //file url can be changed
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(Arrays.asList(authors).contains(b.getAuthor()))
        filteredList.add(b);
    }
    return filteredList;
  }

  ArrayList<Book> getBookWithGenre(String[] genre){
    //file url can be changed
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(Arrays.asList(genre).contains(b.getGenre()))
        filteredList.add(b);
    }
    return filteredList;
  }

  ArrayList<Book> getIfAvailable(){
    //file url can be changed
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(b.isAvailable())
        filteredList.add(b);
    }
    return filteredList;
  }
}
