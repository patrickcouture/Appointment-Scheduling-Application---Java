package model;


/** This class represents a contact object containing the name, contact email, and contact id. */
public class Contacts {


    public String contactEmail;
    public int contactId;

    public String contactName;

    public Contacts(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public int getContactId() {
        return contactId;
    }


    @Override
    public String toString() {

        return contactName;
    }
}
