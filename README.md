# Java Petshop Management System

A desktop GUI application for managing a pet store, built with Java and MySQL. I structured this project using the DAO (Data Access Object) pattern to clearly separate the database logic from the user interface. 

The main highlight of this project is its O2O (Online-to-Offline) business logic. It handles both digital bookings and walk-in customers, acting as a bridge between the digital catalog and the physical store's cashier system.

## Features

**Admin / Cashier Role**
- **Dual-Queue System:** Separates the transaction queues for customers who booked online vs. walk-in customers.
- **Point of Sale (POS):** Handles the final payment on-site after the customer verifies their booked pet in person.
- **Inventory Management:** Basic CRUD operations to add, update, or remove animals from the database.

**Customer Role**
- **Interactive Catalog:** Browse available pets, read details, and trigger specific animal sounds.
- **Feeding Simulation:** A simple interactive feature to "feed" the animals virtually.
- **Online Booking:** Users can reserve a pet through the app first, then visit the physical store for the final payment and pickup.

## Tech Stack
- **Language:** Java (Swing/AWT for the GUI)
- **Architecture:** DAO (Data Access Object)
- **Database:** MySQL via JDBC
- **IDE:** NetBeans

## Project Setup / How to Run
1. Import the provided `db_petshop.sql` file into your local MySQL server (e.g., using XAMPP/phpMyAdmin).
2. Clone this repository and open the project in NetBeans.
3. Check the `DBConnection.java` file and update the database username/password to match your local setup if necessary.
4. Run the project from `JavaFinalDB.java`.

---
*Developed by William Jovaski Trikesumah*
