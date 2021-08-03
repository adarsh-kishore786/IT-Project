import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.time.LocalDate;

public class Customer extends Person {

  private static Admin admin=Admin.getAdmin();
  private static ArrayList<Customer> customerList=new ArrayList<>(); //contains all customer objects; note that it is static

  private static int borrowLimit=Admin.getNumBooksBorrowLimit();
  private int numBooksBorrowed;
  private int numBooksBought;
  HashMap<Book,ArrayList<LocalDate>> history; //planning to make hisory an array list at a later stage
  ArrayList<Book> booksBorrowed; //initialized in constructor
  ArrayList<Book> booksBought; //initialized in constructor
  Transaction transaction; //contains history of borrow/return dates with fine; unique to every customer

  public Customer(){
      // Customer c1 = new Customer("Harish", 25, "harry123@vivlio.org", "harRy-P0t");
      // Customer c2 = new Customer("Adarsh", 24, "ash786@vivlio.org", "C=(acrossb)");
      // Customer c3 = new Customer("Poorna", 25, "poornah06@vivlio.org", "A=2piR");
      // Customer c4 = new Customer("Ranjana", 24, "ranjanak45@vivlio.org", "B1n@ry-M");
      //
      // customerList.add(c1);
      // customerList.add(c2);
      // customerList.add(c3);
      // customerList.add(c4);
      // // System.out.println("!" + customerList.size());
      //
      // saveCustomer();
      initCustomerList();
  }

  Customer(String name,int age,String userName,String password){
    super(name,age,userName,password);
    this.booksBorrowed=new ArrayList<Book>();
    this.booksBought=new ArrayList<Book>();
    this.transaction=new Transaction();
    this.history=new HashMap();
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

  ArrayList<Customer> getCustomers(){
      return customerList;
  }

  boolean addCustomer(Customer c){
      for (Customer c1 : customerList)
      {
          if (c1.getUsername().equals(c.getUsername()))
          {
              System.out.println("This username is already taken. Try another\n");
              return false;
          }
      }
      customerList.add(c);
      saveCustomer();
      return true;
  }

  void buyBook(Book book){
    //currently no restriction on buying
    //Date purchaseDate=new Date(); //returns current date

    //reduce number of copies of book

    if(admin.sellBook(transaction,book)){
      int n=book.getNumCopies();
      book.setNumCopies(n-1);
      booksBought.add(book); //update array list
      numBooksBought=booksBought.size(); //update number of books bought

      //updating book list
      book.setBuyers(this);
      //Book.saveBook(); already called in Book.java

      System.out.println("Bought Successfully!");
    }
      //set transaction purchase date
      // this.transaction.setDateOfPurchase(purchaseDate);
  }

  void borrowBook(Book book){
    //return current date
    //Date borrowDate=new Date();

    //check if limit is respected
    if(booksBorrowed.size()<borrowLimit){

      if(admin.rentBook(transaction,book)){
        book.setBorrowers(this);
        Book.saveBooks(); //this can be called inside setBorrowers method to make customer's work less


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

      //update history by adding the book and list of dates to hashmap
      ArrayList<LocalDate> dates=new ArrayList<LocalDate>();
      dates.add(this.getTransaction().getDateOfBorrow(book));
      dates.add(this.getTransaction().getDateOfReturn(book));
      history.put(book,dates);
      //update history(has to be changed)
      //history+=book.getTitle()+" ";
    }
    //initializeReturnTransaction(return  Date);
  }

  HashMap<Book,ArrayList<LocalDate>> getHistory(){
    return this.history;
  }

  //return transaction object
  Transaction getTransaction(){
    return transaction;
  }

  //write customer object to dat file
  void saveCustomer()
  {

   //ObjectOutputStream os=null; //stream that writes object to file

   try(ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream("./src/customer.dat"))) {

     // if(customerList.contains(this) && this != null)
     // {
     //   int index=customerList.indexOf(this);
     //   customerList.set(index,this); //update customer object to list
     // }else if (this != null) customerList.add(this); //add customer object to list

     os.writeObject(customerList); //write array list to file

     initCustomerList();
   }catch(IOException e){
     System.err.println(e);
     //customerList.remove(this);
   }
 }

  //get customer object from list
  static Customer getCustomer(int n)
  {
    //initCustomerList();
    return customerList.get(n);
  }

  private static void initCustomerList(){
      //ObjectInputStream in=null;
      //try with resources block doesnt need resource closing
      try(ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(new FileInputStream("./src/customer.dat")))) {

        customerList=(ArrayList<Customer>) in.readObject(); //write array list to file

      }catch(IOException e){
        System.err.println(e);
      } catch(ClassNotFoundException e){
        System.err.println(e);
      }
  }

  //following functions assume a books.dat file in /src/books.dat

  //TODO: format returned book list
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

  ArrayList<Book> getBookWithGenre(String[] genres){
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    List<String> genreList=Arrays.asList(genres);

    for(Book b:books){

      ArrayList<String> bookGenreList=b.getGenre();

      for(String genre:genreList){
        if(bookGenreList.contains(genre)){
          filteredList.add(b);
          break;
        }
      }
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

  ArrayList<Book> searchByISBN(String[] ISBN){
    ArrayList<Book> books=Book.getBooks();
    ArrayList<Book> filteredList=new ArrayList<Book>(); //contains req list
    List<String> isbnList=Arrays.asList(ISBN);
    for(Book b:books){
      if(isbnList.contains(b.getISBN()))
        filteredList.add(b);
    }
    return filteredList;
  }
}
