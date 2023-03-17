Title: QAM2 TASK 1: JAVA APPLICATION DEVELOPMENT - Appointment Scheduler Application

Purpose: A GUI allows users to add, update, and delete customer and appointment records in a SQL database. In addition, customers' demographic data can be accessed, appointments can be seen, and reports of three different kinds can be generated.

Author:  Patrick Couture

Contact Information:  pcoutur@wgu.edu

Application Version: Scheduler Application 3.0
Date: March 14, 2023

IDE:  IntelliJ IDEA 2022.3.2 (Community Edition)

JDK Java Development Kit: Java version "17.0.6.0" 01-17-2023
JavaFX Version: This is a Maven build, so IntelliJ does not require a separate JavaFX download

Directions: Unzip the file and open the package in the IDE to run this program. Log in using the username "test" and password "test."
Users are directed to the Appointment and Customer Overview Screen from the login screen and are presented with button choices. The user can view all, by month or by week appointments by using the radio buttons above the Appointment Table.  Users can add, update, or delete appointments using the buttons immediately below the Appointment table. (Note: An appointment must be selected to use the update or delete buttons.)  Using the add or update buttons will navigate the user to a new screen to allow them to add or update appointment information.

For the customer table all customers are listed in the Customer Table by default.  Users can add, update, or delete customers using the buttons immediately 
below the Customer table. (Note: A customer must be selected to use the update or delete buttons.) Using the add or update buttons will navigate the user to a new screen to allow them to add or update customer information.

To view reports, the user needs to click the Reports button in the lower left of the Appointment Customer Overview Screen.
        Customer Appointments by Type & Month Report: counts the total number of appointments scheduled by a specified type and month
        Customers by Province / State Report: count the total number of appointments scheduled for customers from a specific State or Province (First Level Division)
        Contact Schedule Report: displays the appointments scheduled for a specified contact

MySQL Connector driver version number: 8.0.321.jar