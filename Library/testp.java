import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class testp {
    public static void main(String args[])
    {
        //System.out.println(Catalogue.getBooks().get(1).getBuyers());
        // System.out.println(Catalogue.getBookWithTitle("Starsight"));
        // System.out.println("Number of copies availabe: "+Catalogue.getBookWithTitle("Starsight").getNumCopies());
        // System.out.println("List of Borrowers: \n"+Catalogue.getBookWithTitle("Starsight").getBorrowers()+"cust stuff now:");
        //Customer c=new Customer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        LocalDateTime today =  LocalDateTime.now();     //Today
        LocalDateTime next = today.plusDays(20);
        System.out.println("Date when you Borrowed this Book : "+today.format(formatter));
        System.out.println("Last Date to Return without Fine : "+next.format(formatter));
    }
    
}
