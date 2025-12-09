Group 3
Members and Contributions
Stephanie Colunga: I created the ER model for our budgeting application and identified the functional dependencies needed to normalize the schema to BCNF. These results helped me ensure that each entity (Users, Categories, Transactions, and Budgets) was modeled efficiently with proper keys and relationships. On the backend, I implemented the homepage dashboard and its corresponding Mustache template and CSS styling to display the user's monthly income, expenses, net balance, and recent transactions. I also developed the TransactionService class, which handles SQL queries, retrieves dashboard data, maps query results into Transaction model objects, and provides methods for transaction retrieval and summaries.

My Phuong Ly: I created the database schema & implemented the categories part. This feature allows users to create, view, and delete income and expense categories that are securely associated with their accounts. Categories are linked to transactions through foreign keys to ensure accurate financial tracking and reporting.

Susan Awad: I created the budgets page for the application, implementing the controller and services for it. This allows the user to add a budget for a specific category and month, with only one category for each designated month, and the amount they want to budget. The user is able to delete and edit their individual budgets and they will see their currently budget for the month.

Joelia Agbavon: I created the transactions page and implemented the controller. This allows the user to add new transactions, filters their transactions based on the date, type and category. Users are also able to delete and edit any exisiting transactions. 

