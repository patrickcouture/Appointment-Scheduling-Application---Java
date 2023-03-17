package model;


/** The Users class is a model object and represents a registered user in the system.
 * The class contains the user's id, username, and hashed password. */
public class Users {

    public int userId;
    public String username;
    public String password;

    public Users(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public Users (int userId) {
        this.userId = userId;
    }

    public String getUsername() {

        return username;
    }

    public int getUserId() {

        return userId;
    }

    @Override
    public String toString() {

        return (String.valueOf(userId));
    }
}
