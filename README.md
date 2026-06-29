# 🔐 Password Strength Checker

A simple and modern **Password Strength Checker** built using **Java Swing**. This application analyzes a password in real time and displays its strength based on common password security rules.

## ✨ Features

* Real-time password strength analysis
* Modern Java Swing graphical user interface
* Strength levels:

  * Weak
  * Fair
  * Strong
  * Very Strong
* Password visibility toggle (Show/Hide)
* Live checklist for password requirements
* Color-coded strength indicator
* Single Java file implementation (Frontend + Backend)

## 📋 Password Validation Rules

The application checks whether the password contains:

* ✅ At least 8 characters
* ✅ At least one uppercase letter (A–Z)
* ✅ At least one lowercase letter (a–z)
* ✅ At least one special character (!@#$%^&*...)
* ✅ At least one digit (0–9) *(Bonus)*

## 🛠️ Technologies Used

* Java
* Java Swing
* AWT

## 📂 Project Structure

```
PasswordCheckerApp.java
```

This project is implemented in a single Java file that contains:

* Password evaluation logic
* Strength calculation
* Swing user interface
* Event handling

## 🚀 How to Run

### Compile

```bash
javac PasswordCheckerApp.java
```

### Run

```bash
java PasswordCheckerApp
```

## 📊 Strength Calculation

| Score | Strength    |
| ----: | ----------- |
|   0–1 | Weak        |
|     2 | Fair        |
|     3 | Strong      |
|     4 | Very Strong |

The application updates the strength bar and requirement checklist instantly as the user types.

## 📸 Application Features

* Interactive password input
* Show/Hide password button
* Live strength meter
* Requirement checklist
* Clean and responsive interface

## 🎯 Learning Objectives

This project demonstrates:

* Java Swing GUI development
* Event-driven programming
* Object-Oriented Programming (OOP)
* Password validation using Java
* Real-time UI updates

## 👨‍💻 Author

Developed as a Java mini project for learning GUI development and password validation concepts.

---

**If you found this project useful, consider giving it a ⭐ on GitHub!**
