# Budget Management System

## Overview
The Budget Management System is a desktop application built with JavaFX, designed to help users manage their finances by tracking expenses, incomes, goals, and regular payments. It connects to a MySQL database for secure and efficient data management.

## Technologies Used
- **Programming Language:** Java
- **Framework:** JavaFX
- **Database:** MySQL
- **IDE:** *(IntelliJ IDEA, Eclipse,Visual Studio)*

## Database Structure
The MySQL database, `budget_app`, includes the following key tables:
- **users:** Stores user information with roles (ADMIN, USER).
- **categories:** Tracks types of expenses and incomes.
- **transaction_types:** Defines transaction types (Income, Expense).
- **transactions:** Logs all user financial transactions.
- **goals:** Manages user financial goals and progress.
- **period_reports:** Stores summaries of income and expenses over time.
- **notifications:** Displays important reminders and updates for users.
- **regular_payments:** Tracks recurring expenses with due dates.
- **balances:** Records each user's account balance.

## Features
- User management with roles (Admin, User)
- Expense and income tracking by category
- Goal setting and progress monitoring
- Automated notifications (e.g., budget milestones, payment reminders)
- Periodic financial reports
- Management of recurring payments

## Installation
1. **Clone the repository:**  
   ```bash
   git clone https://github.com/berkekamisoglu/budgetmanagmentsystem
   ```
2. **Database Setup:**  
   - Create the database using the provided SQL script.
   - Configure database credentials in your Java project.
3. **Run the Project:**  
   Build and launch using your IDE.

## Future Improvements
- Add data export options (CSV, PDF)
- Implement graphical analytics (charts, graphs)
- Develop advanced search and filtering features

## Author
- **Name:** *Berke Kamisoglu*
- **Course:** Java Programming Course

