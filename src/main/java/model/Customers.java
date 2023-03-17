package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** This class represents a customer object containing the customer id, customer name, customer address,
 * customer phone, customer postal code, division id, and division name. */
public class Customers {

    private static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

    public int customerId;

    public String customerName;

    public String custAddress;

    public String custPhone;
    public int countryId;
    public String custPostalCode;

    public int divisionId;

     public String divisionName;

    public Customers(int customerId, String customerName, String custAddress, String custPostalCode, String custPhone, int divisionId, String divisionName) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.custAddress = custAddress;
        this.custPostalCode = custPostalCode;
        this.custPhone = custPhone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;

    }

    public Customers(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public String getCustPostalCode() {
        return custPostalCode;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public int getCountryId() {

        return countryId;   }

    @Override
    public String toString() {

        return (String.valueOf(customerId));
    }
}
