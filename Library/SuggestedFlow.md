# Library
First a home page with 3 options: Reservation, Management and Exit.

## Visitor
 Suggested APIs/Functions:
 
 1. About the Library (Vivlio)
 2. Help (Information)
 3. Search for a book (Info) Subcategories: Genre, Availability, Author, Keywords; Display book details
 4. Borrowing a book
 5. Buying a book
 6. Checking whether a book is borrowed on name
 7. Create/View/Delete user account
 8. Return a book
 9. Pay fine to library
 10. See user history

## Admin
Suggested APIs/Functions:

1. PW functions (Need password to enter admin)
2. Add books
3. Delete Books
4. Modify book details
5. Check details of any person
6. Check what books are available
7. Check fines pending
8. Check revenue
9. Sell/Rent book

## Some additional points:
1. We will need function to count the number of days automatically for books. For that we can use the Date class in Java. Something like Date.today() - dateOfPurchase() (this is pseudo-code)
2. We will use a .dat file to store the details of books and customers (Book.dat and Customer.dat). We also need to see how we save and read from a .dat file using Java.
