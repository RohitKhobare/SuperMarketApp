# 🛒 Supermarket Management System (Java + MySQL)

## 📘 Project Overview
This **Supermarket Management System** is a **DBMS Mini Project** built using **Java (JDBC)** and **MySQL**.  
It automates basic supermarket operations such as:
- Managing products  
- Recording sales  
- Viewing reports  

This project demonstrates database connectivity using **JDBC** and follows a simple modular design.

---

## 🧰 Tech Stack

| Category | Technology |
|-----------|-------------|
| **Frontend / UI** | Java Swing / AWT |
| **Backend** | Java |
| **Database** | MySQL |
| **Connector** | MySQL JDBC Driver (`mysql-connector-j-9.2.0.jar`) |
| **IDE (Optional)** | IntelliJ IDEA / NetBeans / Eclipse / VS Code |
| **Version Control** | Git & GitHub |

---

## 🧑‍💻 Features

- Add, update, delete, and view **products**
- Record **sales transactions**
- Maintain **billing and inventory**
- Secure **database connectivity**
- Clean **command-line or GUI interface**

---

## ⚙️ Software Requirements

| Requirement | Version / Details |
|--------------|-------------------|
| **JDK (Java Development Kit)** | Version 8 or later |
| **MySQL Server** | Version 8.0+ |
| **MySQL Workbench** | (optional for GUI access) |
| **Git** | For version control |
| **Text Editor / IDE** | IntelliJ, Eclipse, or VS Code |
| **Operating System** | Windows / Linux / macOS |

---

## 🗂️ Folder Structure

```
SupermarketApp/
│
├── lib/
│   └── mysql-connector-j-9.2.0.jar
│
├── SupermarketManagement.java
├── DBConnection.java
├── supermarket_db_setup.sql
├── README.md
└── (other .java files)
```

---

## 🏗️ Database Setup (MySQL)

1. **Start MySQL Server**
   ```bash
   mysql -u root -p
   ```

2. **Import the Database**
   ```sql
   SOURCE C:/Users/Admin/SupermarketApp/supermarket_db_setup.sql;
   ```

3. **Verify the Database**
   ```sql
   SHOW DATABASES;
   USE supermarket_db;
   SHOW TABLES;
   ```

✅ You should see tables like `products` and `sales`.

---

## 🖥️ Project Setup & Execution

### Step 1: Open Command Prompt
```bash
cd C:\Users\Admin\SupermarketApp\SupermarketApp
```

### Step 2: Compile the Project
```bash
javac -cp .;lib\mysql-connector-j-9.2.0.jar SupermarketManagement.java
```

### Step 3: Run the Application
```bash
java -cp .;lib\mysql-connector-j-9.2.0.jar SupermarketManagement
```

If your project includes a GUI, a window will open for product or sales management.

---

## 🧩 Database Connection Details

```java
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/supermarket_db",
    "root",
    "your_mysql_password"
);
```

🔹 Replace `"your_mysql_password"` with your actual MySQL password.

---

## 🧾 Sample Output

```
==== Supermarket Management System ====
1. Add Product
2. View Products
3. Make Sale
4. View Sales
5. Exit
Enter your choice: 1
Enter Product ID: 101
Enter Product Name: Milk
Enter Price: 45.00
Enter Quantity: 10
Product Added Successfully!
```

---

## ☁️ Hosting the Project on GitHub

```bash
git init
git add .
git commit -m "Initial commit - Supermarket DBMS Mini Project"
git branch -M main
git remote add origin https://github.com/rohitkhobare/SupermarketApp.git
git push -u origin main
```

✅ Your project will now be publicly visible on GitHub:
👉 [https://github.com/rohitkhobare/SupermarketApp](https://github.com/rohitkhobare/SupermarketApp)

---

## 👨‍🎓 Author

**Rohit Khobare**  
📍 DBMS Mini Project — Academic Submission  
📧 rohitkhobare2005@gmail.com  
📅 Year: 2025  

---

## 📜 License

This project is open-source and free to use for educational purposes.

---

## 💬 Feedback

If you found this project helpful, please ⭐ **star** the repository on GitHub!  
For improvements or queries, open an issue on the repo.
