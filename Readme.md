Project Name: Library Management System
-

-> This project is all about managing the books in library when a user want to borrow or return a book

-> I have implemented below functionalities

1) Borrow a Book

2) Return a Book

3) Search for a Book

4) Check All Available Books

5) Generate fee report

6) Exit the system

1)Functionalities implemented for Borrow a Book
-
Input:

-> Take the input either Book ID(numeric,positive integer only) or Book Title and userID

validation:

-> Ensure the input is valide the book is available

-> Validate that the book is available for borrowing

output:

* if available,borrow the book:

  -> "Book borrowed successfully"

  -> "Record the borrowed date (current date) for the book"

* if unavailable,display

  -> "Book not available"

* if input us invalid, display:

  -> "Invalid Book ID or Title. Please try again."

2)Functionalities implemented Return a Book
-
Input:

-> Take the input either Book ID(numeric,positive integer only) or Book Title and userID

-> Return Date

Validation:

-> Ensure the book was borrowed before allowing it to return

-> Validate the return date to be later than the borrow date

-> If the book is returned after the due date (borrow date + due period),apply the late fee.

output:

* On successful return

  Display:
    1. "Book returned successfully."
    2. "Late fee applied:$<Fee_Amount>.
* On failure:
  -> If the book is not borrowed:
    * "The book is not currently borrowed."
      -> If the input is invalid:
    * "Invalid Book ID, Title, or Return Date. Please try again."

3)Implemented functionalities for Searching a Books
-
Input:

1. Enter a keyword for the Book Title or Author (case-insensitive,partial or full text).

   Validation:

   -> Ensure the keyword is not empty

Output:

1.Display a list of matching books with columns:

BookID | Title | Author | Status

.If no matches found, display: "No books found matching the search term."

4)Functionalities implemented for Check Available Books
-
Input:

* None

  Output:

1. Display a list of all Available books with columns:

   BookID | Title | Author

2. If no books are available, display:No books are currently available for borrowing.

5)Functionalities implemented for Generate Fee Report
-
Input:

Date Range (Start Date and End Date).

Validate:

* Ensure the date range is valid (stard date <= End date).

  Output:

* If data exists for the specified range:

  -> Display detailed report with columns:

  Book ID | Title | Borrower Name | Days Overdue | Late Fee Collected

  -> Display total late fees collected during the date range:

    * "Total Late Fees Collecte: $<Total_Amount>."
  
* If no data exits:

  -> Display: "No overdue books or late fees recorded during the specified period."

6)Exit the system
-

    Exit the application

Database and server
-
-> In this project I used mysql Database server

-> The database library_management consist of 3 tables

1)user

2)Book

3)borrowed_books

-> where the user can take many books and a book is borrowed by many
users i.e this is many to many relationship

-> where we created a junction table (borrow_books) for to track book records

-> user table consist of userId, user_name and contact_info

-> book table consist of bookId, title, author, status

-> borrowed_books table consist of borrowId,bookId,userId,borrowDate,returnDate

    

