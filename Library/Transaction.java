import java.util.ArrayList;
import java.util.Scanner;

import java.time.LocalDate;
public class Transaction {

    ArrayList<LocalDate> dateOfBorrow= new ArrayList<LocalDate>();


    private boolean payment(int amount) {
        Scanner details= new Scanner(System.in); 
        boolean status=false;
        long cardNumber, n;
        int count=0, cvv;
        char choice='Y';
        String expiryDate= new String();
        do
        {
            System.out.println("Enter debit/credit card number: ");
            cardNumber=details.nextLong();
            n=cardNumber;
            while (n>0)
            {
                ++count;
                n/=10;
            }
            if (n!=16)
            System.out.println("Invalid card number!");
            else
            {
                System.out.println("Enter Expiry Date in MM/YY format: ");
                expiryDate=details.nextLine();
                //Try checking if it is beyind today's date?
                System.out.println("Enter CVV/CVC: ");
                cvv=details.nextInt(); //Later check if 3 digits or not
                System.out.println("Processing transaction... ");
                System.out.println("Transaction Successful!");
                status=true;
            }
            System.out.println("Do you want to continue in payment section? Enter Y if yes else enter N: ");
            choice=details.next().charAt(0);
        } while (choice=='Y');
        details.close();
        return status;
    }
    public boolean buyBook() {
        boolean status=false, statusPayment;
        int amount=0;
        statusPayment=payment(amount);
        if (statusPayment==true)
        status=true;
        return status;
    }
    public boolean borrowBookTransaction(int index, Transaction t)
    {
        boolean status=true, statusPayment;
        int amount=0, numberOfDays;
        LocalDate dateOfReturn= LocalDate.now();
        numberOfDays=dateOfReturn.compareTo(dateOfBorrow.get(index));
        if (numberOfDays>limitBorrow())
        {
            System.out.println("Number of days borrowed exceeds limit! You need to pay a fine!");
            System.out.println("The amount you need to pay is: Rs "+numberOfDays-limitBorrow()+" ");
        }
        statusPayment=payment(amount);
        if (statusPayment==true)
        status=true;
        return status;
    }
}