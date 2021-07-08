import java.util.*;
import java.io.*;

public class Customer extends Person {
  private static ArrayList<Customer> customerList=new ArrayList<Customer>(); //contains all customer objects
  private int numBooksBorrowed;
  private int numBooksBought;
  String history;
  ArrayList<Book> booksBorrowed;
  ArrayList<Book> booksBought;
  Transaction transaction=new Transaction();

  Customer(String name,int age,String userName,String password){
    super(String name,int age,String userName,String password);
    this.booksBorrowed=new ArrayList<Book>();
    this.booksBought=new ArrayList<Book>();
    // this.booksBorrowed= new Book[numBooksBorrowed];
    // this.booksBought=new Book[numBooksBought];
  }
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
    Date purchaseDate=new Date();
    //with arrays
    // if(booksBought[this.numBooksBought-1]!=null) System.out.println("Buy Limit Reached!");
    // for(int i=0;i<this.numBooksBought;i++){
    //   if(booksBought[i]==null) {
    //     booksBought[i]=book;
    //     System.out.println("Added!");
    //   }
    // }

    //with array list
      booksBought.add(book);
      numBooksBought++;

      transaction.setDateOfPurchase(purchaseDate);
  }
  void borrowBook(Book book){
    Date borrowDate=new Date();
    //with arrays
    // if(booksBought[getBorrowLimit()-1]!=null) System.out.println("Borrow Limit Reached!");
    // for(int i=0;i<this.numBooksBorrowed;i++){
    //   if(booksBorrowed[i]==null) {
    //     booksBorrowed[i]=book;
    //     System.out.println("Added!");
    //   }
    // }

    //with array list
    if(booksBorrowed.size()<getBorrowLimit()){
      booksBorrowed.add(book);
      numBooksBorrowed++;
      //pass date and return status to transaction
      transaction.setDateOfBorrow(borrowDate);
      transaction.setIsReturned(false);

      System.out.println("Added");
    }else System.out.println("Borrow Limit Reached! Please return a book to continue");
  }
  void returnBook(Book book){
    Date returnDate=new Date();

    booksBorrowed.remove(book);
    numBooksBorrowed--;
    transaction.setDateOfReturn(returnDate);
    transaction.setIsReturned(true);
    System.out.println("Returned successfully! \nFine to be paid: "+transaction.getFine());
  }
  String getHistory(){
    return history;
  }
  Transaction getTransaction(){
    return transaction;
  }

  //write customer object to dat file
  void saveCustomer() throws IOException{
    customerList.add(this); //add customer object to list
    ObjectOutputStream os=null;
    try {
      os=new ObjectOutputStream(new FileOutputStream("./src/save.dat"));
      os.writeObject(customerList);
      os.flush();
    }catch(Exception e){
      e.printStackTrace();
    } finally{
      if(os!=null) os.close();
      System.out.println("Saved!");
    }
  }

  //get customer object from list
  static Customer getCustomer(int n){
    return customerList[n];
  }

  ArrayList<Book> getBookUnderPrice(double price){
    //file url can be changed
    var books=Book.getBooks("/src/books.dat");
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(b.getPrice()<price)
        filteredList.add(b);
    }
    return filteredList;
  }
  ArrayList<Book> getBookWithAuthor(String[] authors){
    //file url can be changed
    var books=Book.getBooks("/src/books.dat");
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(Arrays.asList(authors).contains(b.getAuthor()))
        filteredList.add(b);
    }
    return filteredList;
  }
  ArrayList<Book> getBookWithGenre(String[] genre){
    //file url can be changed
    var books=Book.getBooks("/src/books.dat");
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(Arrays.asList(genre).contains(b.getGenre()))
        filteredList.add(b);
    }
    return filteredList;
  }
  ArrayList<Book> getIfAvailable(){
    //file url can be changed
    var books=Book.getBooks("/src/books.dat");
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(b.isAvailable())
        filteredList.add(b);
    }
    return filteredList;
  }
}
