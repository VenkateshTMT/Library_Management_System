Project Name: Library Management System

->This project is all about managing the books in library when a user borrow or return a book 

->I have implemented below functionalities

1)Borrow a Book
2)Return a Book
3)Search for a Book
4)Check All Available Books
5)Generate fee report
6)Exit the system

Database and server
-
->In this project I used mysql Database server
->The database library_management consist of 3 tables
    1)user
    2)Book
    3)borrowed_books
->where the user can take many books and a book is borrowed by many 
  users i.e this is many to many relationship
->where we created a junction table (borrow_books) for to track book records
->user table consist of userId, user_name and contact_info

    

