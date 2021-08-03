import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerOptions {
  private static Scanner sc=null;
  private Customer c=null;
  private AuthService auth=null;
  private int transaction;

  CustomerOptions(Customer cust,AuthService auth,Scanner sc){
    this.c=cust;
    this.auth=auth;
    this.sc=sc;
    transaction=0;
  }

  public void showOptions(){
    while (true) {
      System.out.println("\nWhat do you want to do?");
      System.out.println("1.Borrow A Book");
      System.out.println("2.Return A Book");
      System.out.println("3.Buy A Book");
      System.out.println("4.View History");
      System.out.println("5.Log Out");
      System.out.print("Enter the option number: ");
      int choice = 0;

      try{
          choice = Integer.parseInt(sc.nextLine());
      }
      catch (NumberFormatException e){
          System.out.println("That's an invalid option. Try again.\n");
          continue;
      }

      switch(choice){
          case 1: transaction=1;
                  showCatalogue();
                  break;
          case 2: transaction=2;
                  showBorrowedBooks();
                  break;
          case 3: transaction=3;
                  showCatalogue();
                  break;
          case 4: showHistory();
                  break;
          case 5: System.out.println();
                  auth.logout();
                  break;

          default: System.out.println("That's an invalid option. Try again.\n");
      }
      transaction=0;
    }
  }

  private void showCatalogue(){

    while(true){
      System.out.println("\n1.Filter Books based on price");
      System.out.println("2.Filter Books based on authors");
      System.out.println("3.Filter Books based on genre");
      System.out.println("4.Filter Books based on availability");
      System.out.println("5.Search for Books with ISBN");
      System.out.println("6.Back");
      System.out.print("Enter the option number: ");

      int choice=0;
      try{
          choice = Integer.parseInt(sc.nextLine());
      }
      catch (NumberFormatException e){
          System.out.println("That's an invalid option. Try again.\n");
          continue;
      }
      switch(choice){
          case 1: filterByPrice();
                  return;
          case 2: filterByAuthors();
                  return;
          case 3: System.out.println("Enter Genre");
                  return;
          case 4: System.out.println("availability");
                  return;
          case 5: System.out.println("ISBN");
                  return;
          case 6: System.out.println();
                  return;

          default: System.out.println("That's an invalid option. Try again.\n");
      }
    }
  }

  private void filterByPrice(){
    double price;
    Book selectedBook;
    while(true){
      System.out.print("Enter price: ");
        try {
          price=Double.parseDouble(sc.nextLine());
          ArrayList<Book> booklist=c.getBookUnderPrice(price);
          showBooks(booklist);
          selectedBook = selectBook(booklist);
          confirmBook(selectedBook);
          break;
      } catch (NumberFormatException e){
          System.out.println("That's an invalid price. Try again.\n");
          continue;
      }
    }
  }

  private void filterByAuthors(){
    ArrayList<String> authorList=new ArrayList<String>();
    Book selectedBook;

    System.out.print("\nEnter Authors(type \"exit\" when done): ");
    String author;

    do {
      author=null;
      try {
          author=sc.nextLine().trim();
          authorList.add(author);
      } catch (NoSuchElementException e){
          System.out.println("That's an invalid input. Try again.\n");
          continue;
      }
    } while(!author.equals("exit"));
    authorList.remove(authorList.size()-1);
    String[] authorArray=new String[authorList.size()-1];
    ArrayList<Book> booklist=c.getBookWithAuthor(authorList.toArray(authorArray));
    if(booklist.isEmpty()){
      System.out.println("\nNo Book Found!");
      return;
    }
    showBooks(booklist);
    selectedBook = selectBook(booklist);
    confirmBook(selectedBook);
  }

  private void showHistory(){
    HashMap<Book,ArrayList<LocalDate>> map=(HashMap<Book,ArrayList<LocalDate>>) c.getHistory();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
    if(map.isEmpty()){
      System.out.println("\nNO HISTORY!");
      Main.sleep(1);
      return;
    }

    for (Map.Entry<Book,ArrayList<LocalDate>> entry : map.entrySet()) {
      Book book=(Book) entry.getKey();
      ArrayList<LocalDate> dates=(ArrayList<LocalDate>) entry.getValue();
      String borrowDate = dates.get(0).format(formatter);
      String returnDate = dates.get(1).format(formatter);

      System.out.println(book);
      System.out.println("Borrow Date: "+borrowDate);
      System.out.println("Return Date: "+returnDate);
    }
  }

  private void showBorrowedBooks(){

  }

  private void confirmBook(Book book){
    String ch = null;
    do
    {
        System.out.print("(C)onfirm, (B)ack : ");
        ch = sc.nextLine();

        if (ch.equalsIgnoreCase("c")){
          switch (transaction) {
            case 1: c.borrowBook(book);
                    break;
            case 2: c.returnBook(book);
                    break;
            case 3: c.buyBook(book);
                    break;
          }
        }
        else if (ch.equalsIgnoreCase("b")){
          System.out.println();
          return;
        }
        else
          System.out.println("Invalid input ,Try again");

        ch = "";
    } while (!ch.equalsIgnoreCase("c") && !ch.equalsIgnoreCase("b"));
  }

  private Book selectBook(ArrayList<Book> booklist){
    int index=0;
    while(true) {
      System.out.print("Enter index: ");
      try {
        index=Integer.parseInt(sc.nextLine());
        return booklist.get(index-1);
      } catch(NumberFormatException e) {
        System.out.println("Invalid index, try again");
        continue;
      }
    }
  }

  private void showBooks(ArrayList<Book> booklist){
    int i=1;

    for(Book b:booklist){
      System.out.println(b+"Index: "+i);
      i++;
    }
  }
}
