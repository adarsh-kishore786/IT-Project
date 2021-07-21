import java.util.*;
import java.io.*;

public class Customer extends Person {
  private static Admin admin=new Admin();
  private static ArrayList<Customer> customerList=new ArrayList<Customer>(); //contains all customer objects; note that it is static
  private static int borrowLimit=admin.getNumBooksBorrowLimit();
  private int numBooksBorrowed;
  private int numBooksBought;
  String history=""; //planning to make hisory an array list at a later stage
  // ArrayList<Book> booksBorrowed; //initialized in constructor
  // ArrayList<Book> booksBought; //initialized in constructor
  Transaction transaction=new Transaction(); //contains history of borrow/return dates with fine; unique to every customer

  Customer(String name,int age,String userName,String password){
    super(name,age,userName,password);
    this.booksBorrowed=new ArrayList<Book>();
    this.booksBought=new ArrayList<Book>();
    // this.booksBorrowed= new Book[numBooksBorrowed];
    // this.booksBought=new Book[numBooksBought];
  }

  int getBorrowLimit(){
    return borrowLimit;
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
    //Date borrowDate=new Date();

    //check if limit is respected
    if(booksBorrowed.size()<borrowLimit){

      if(admin.rentBook(transaction,book)){
        book.setBorrowers(this);
        Book.saveBook(); //this can be called inside setBorrowers method to make customer's work less

        booksBorrowed.add(book); //add book to customer list of borrowed books
        numBooksBorrowed=booksBorrowed.size();

        System.out.println("Congrats! You have borrowed a new book!");
      }

    }else System.out.println("Borrow Limit Reached! Please return a book to continue"); //response to limit breach
  }

  void returnBook(Book book){
    if(admin.getBackBook(transaction,book)){
      //remove book from list
      booksBorrowed.remove(book);
      numBooksBorrowed=booksBorrowed.size();

      //update history(has to be changed)
      history+=book.getTitle()+" ";
    }
    //initializeReturnTransaction(returnDate);
  }

  String getHistory(){
    return history;
  }

  //return transaction object
  Transaction getTransaction(){
    return transaction;
  }

  //write customer object to dat file
  void saveCustomer() throws IOException{


    ObjectOutputStream os=null; //stream that writes object to file
    try {

      if(customerList.contains(this))
      {
        int index=customerList.indexOf(this);
        customerList.set(index,this); //update customer object to list
      }else customerList.add(this); //add customer object to list

      os=new ObjectOutputStream(new FileOutputStream("./src/customer.dat"));
      os.writeObject(customerList); //write array list to file
      os.flush();

    }catch(Exception e){
      e.printStackTrace();
      //customerList.remove(this);
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

  //TODO format returned book list
  ArrayList<Book> getBookUnderPrice(double price){

    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    for(Book b:books){
      if(b.getPrice()<price)
        filteredList.add(b);
    }
    return filteredList;
  }

  ArrayList<Book> getBookWithAuthor(String[] authors){
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    List<String> authorList=Arrays.asList(authors);
    for(Book b:books){
      if(authorList.contains(b.getAuthor()))
        filteredList.add(b);
    }
    return filteredList;
  }

  ArrayList<Book> getBookWithGenre(String[] genre){
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    List<String> genreList=Arrays.asList(genre);

    for(Book b:books){
      if(genreList.contains(b.getGenre()))
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