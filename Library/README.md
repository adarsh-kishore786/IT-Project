# Instructions

## Compilation Instructions: 

1. Go to ```src``` directory containing the files.
2. Run the following command in command line: ```javac *.java```. This compiles all the .java files in the    directory.

## Runtime Instructions: 

1. Run the following command in the command line: ```java Main```. This runs the file **Main** .
2. ```(L)ogin``` and similar statements indicate that the program is expecting the first letter in any case as the input like in the given example, ```L``` or ```l```. 
3. In the welcome screen, 3 options are shown Login, Sign up and Exit. It is recommended to make an account first in the **Customer** to test the features. 
4. In this implementation, only one administration account is there.  
    The details of the Admin are as follows:  
    Username: ```andrewj@vivlio.org```  
    Password: ```aDm1n-PasS```
5. In this implementation, there is a customer account already created for testing.
   This account also has a borrowed book, 'The Da Vinci Code' borrowed on 03/08/2021, that can be used for testing fine.  
   The details are:  
   Username: ```poornah06@vivlio.org```  
   Password: ```A=2piR```
6. While paying, these are the following conditions for a successful payment:
    1. The credit/debit card number should be 16 digits.
    2. The expiry date has to be entered as MM/YY, and the expiry date should be a valid one.
    3. The CVV/CVC should be 3 digits.
