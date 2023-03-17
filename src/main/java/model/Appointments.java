package model;



import java.time.LocalDateTime;

/** This class is used to store and access Appointments data. */
public class Appointments {

    public int apptId;

    public String apptTitle;

    public String apptDesc;

    public String apptLocation;

    public String apptType;

    public LocalDateTime apptStart;

    public LocalDateTime apptEnd;

    public int apptContactId;

    public int apptCustomerId;

    public int apptUserId;

    public String apptContact;



    /** Creates an Appointments object with all of the required fields
     *
     * @param apptId       Unique appointment ID
     * @param apptTitle    Appointment title
     * @param apptDesc     Description of appointment (optional)
     * @param apptLocation Appointment location
     * @param apptContact  Appointment contact
     * @param apptContactId Contact ID associated with contact
     * @param apptType     Type of appointment
     * @param apptStart    Start time for appointment
     * @param apptEnd      End time for appointment
     * @param apptCustomerId    Contact relation to customer ID
     * @param apptUserId   User ID associated with appointment
     */
    public Appointments(int apptId, String apptTitle, String apptDesc, String apptLocation, String apptContact, int apptContactId, String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int apptCustomerId, int apptUserId) {

        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLocation = apptLocation;
        this.apptContact = String.valueOf(apptContact);
        this.apptContactId = apptContactId;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomerId = apptCustomerId;
        this.apptUserId = apptUserId;
    }
    /** Creates an Appointments object with all of the required fields
     *
     * @param apptId       Unique appointment ID
     * @param apptTitle    Appointment title
     * @param apptDesc     Description of appointment (optional)
     * @param apptLocation Appointment location
     * @param apptContact  Appointment contact
     * @param apptContactId Contact ID associated with contact
     * @param apptType     Type of appointment
     * @param apptStart    Start time for appointment
     * @param apptEnd      End time for appointment
     * @param apptCustomerId    Contact relation to customer ID
     * @param apptUserId   User ID associated with appointment
     */
    public Appointments(int apptId, String apptTitle, String apptDesc, String apptLocation, Contacts apptContact, int apptContactId, String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int apptCustomerId, int apptUserId) {

        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLocation = apptLocation;
        this.apptContact = String.valueOf(apptContact);
        this.apptContactId = apptContactId;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomerId = apptCustomerId;
        this.apptUserId = apptUserId;

    }

    /** Creates an Appointments object with all of the required fields
     *
     * @param apptId       Unique appointment ID
     * @param apptTitle    Appointment title
     * @param apptDesc     Description of appointment (optional)
     * @param apptLocation Appointment location
     * @param apptContactId Contact ID associated with contact
     * @param apptType     Type of appointment
     * @param apptStart    Start time for appointment
     * @param apptEnd      End time for appointment
     * @param apptCustomerId    Contact relation to customer ID
     */
    public Appointments(int apptId, String apptTitle, String apptDesc, String apptLocation,
                        String apptType, LocalDateTime apptStart, LocalDateTime apptEnd, int apptCustomerId, int apptUserId, int apptContactId) {
        this.apptId = apptId;
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomerId = apptCustomerId;
        this.apptUserId = apptUserId;
        this.apptContactId = apptContactId;

    }

   public int getApptContactId() {
        return apptContactId;
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public String getApptDesc() {
        return apptDesc;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public String getApptContact() {
        return apptContact;
    }

    public String getApptType() {
        return apptType;
    }

    public LocalDateTime getApptStart() {
        return apptStart;
    }

    public LocalDateTime getApptEnd() {
        return apptEnd;
    }

    public int getApptCustomerId() {
        return apptCustomerId;
    }

    public int getApptUserId() {
        return apptUserId;
    }

    public int getApptId() {
        return apptId;
    }

    public int getContactId() {
        return apptContactId;
    }

    public LocalDateTime getStart() {
        return apptStart;
    }

    public LocalDateTime getEnd() {
        return apptEnd;
    }
}
