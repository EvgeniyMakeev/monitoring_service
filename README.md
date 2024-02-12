1. [ДЗ №3](https://github.com/EvgeniyMakeev/monitoring_service/pull/3) 
2. [ДЗ №2](https://github.com/EvgeniyMakeev/monitoring_service/pull/2)


# Monitoring Service Console Application

This repository contains a Java console application for managing utility meter readings. Users can log in, submit meter readings, view their submission history, and more. Additionally, administrators have access to additional options, such as viewing submission history for all users and logs.

## Getting Started

To run the application, you need to have Java installed on your machine. Follow these steps:

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/EvgeniyMakeev/monitoring_service.git
   ```

2. Navigate to the project directory:

   ```bash
   cd your-repo
   ```

3. Compile and run the application:

   ```bash
   javac App.java
   java App
   ```

## Usage

1. Upon running the application, you will be presented with the main menu:

   ```plaintext
   ======= MAIN MENU =======
   1. Log in.
   2. Sign in.

   0. Exit
   ```

2. Choose an option by entering the corresponding number.

3. If you choose to log in or sign in, you will be prompted to enter your login and password.

4. Follow the on-screen instructions to navigate through the application and perform various actions.

5. To exit the application, select option 0.

## Features

- **User Management:**
    - Log in
    - Sign in

- **User Options:**
    - Show current meter indications
    - Submit meter of counters
    - Show indications for the selected month
    - Show indications submission history
    - Admin options (if user is an administrator)
    - Log out

*For log in as administrator use the login:* **admin** *and password:* **admin**

- **Admin Options:**
    - Show indications submission history for all users
    - Show indications submission history for a specific user
    - Show log for a specific user
    - Show log for all users
    - Back to User menu

## Project Structure

The project is organized into several packages:

- `ui`: Contains the main application code.
- `constants`: Defines enums for utility counters.
- `dao`: Data Access Object interfaces and implementations for managing data.
- `in`: Input interfaces and implementations for user input.
- `model`: Data model classes and records.
- `out`: Output interfaces and implementations for displaying messages.
- `service`: Service interfaces and implementations for application logic.
