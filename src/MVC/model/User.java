/*
Name:           User.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a User object. This class has all information
                regarding a single user of the system.
*/

package MVC.model;

import MVC.utils.DatabaseManagement;
import MVC.utils.SerialisationHelper;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class User
{
    private int userID;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String salt;
    private String password;
    private int securityQuestionOneID;
    private String securityAnswerOne;
    private int getSecurityQuestionTwoID;
    private String securityAnswerTwo;
    private Profile profile;

    /**
     * Constructor to initialise a user with empty fields (to be filled in
     * later)
     */
    public User()
    {
        this.userID = 0;
        this.firstName = null;
        this.lastName = null;
        this.username = null;
        this.email = null;
        this.password = null;
        this.profile = new Profile();
    }


    /**
     * Constructor used to initialise a user based on passed in objects
     *
     * @param userID
     * @param firstName
     * @param lastName
     * @param username
     * @param email
     * @param password
     * @param profile
     */
    public User(int userID, String firstName, String lastName, String username, String email, String salt, String password, Profile profile)
    {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.profile = profile;
    }

    /**
     * Generate a random 32 byte salt and return it (to be used when hashing
     * the password data)
     *
     * @return A random 32 byte salt
     */
    public static String generateSalt()
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String saltStr = encoder.encodeToString(salt);
        return saltStr;
    }

    /**
     * Class helper method that takes in a salt and password as its arguments
     * and returns a 32 byte digest of them using the SHA-256 hashing algorithm
     * <p>
     * Adapted from: https://www.geeksforgeeks.org/sha-256-hash-in-java/
     *
     * @param inputData the password or data to be hashed
     * @param salt      the salt to be used
     * @return a generated 32 byte digest
     */
    public static String hashDataWithSalt(String inputData, String salt)
    {
        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String toHash = salt + inputData;

        byte[] digest = md.digest(toHash.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, digest);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    /**
     * Write a new User object (along with profile) to the database
     *
     * @param user the user to be written to the database
     */
    public static void writeNewUserToDatabase(User user)
    {
        DatabaseManagement db = new DatabaseManagement();

        // Write user to database
        String sqlStatement = "INSERT INTO users(first_name, last_name, email, username, salt, password, question1_id, question1_answer, question2_id, question2_answer, profile) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        String[] fields = new String[11];
        fields[0] = user.firstName;
        fields[1] = user.lastName;
        fields[2] = user.email;
        fields[3] = user.username;
        fields[4] = user.salt;
        fields[5] = user.password;
        fields[6] = "" + user.securityQuestionOneID;
        fields[7] = user.securityAnswerOne;
        fields[8] = "" + user.getSecurityQuestionTwoID;
        fields[9] = user.securityAnswerTwo;
        fields[10] = SerialisationHelper.serializeProfile(user.profile);

        db.insert(sqlStatement, fields);
    }

    /**
     * Write an exisiting user object to the database
     *
     * @param user
     */
    public static void writeExistingUserToDatabase(User user)
    {
        DatabaseManagement db = new DatabaseManagement();

        String statement = "UPDATE users SET first_name=?, last_name=?, email=?, username=?, salt=?, password=?, " +
                "question1_id=?, question1_answer=?, question2_id=?, question2_answer=?, profile=? WHERE user_id=?";

        String userID = "" + user.getUserID();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String username = user.getUsername();
        String salt = user.getSalt();
        String password = user.getPassword();
        String question1_id = "" + user.getSecurityQuestionOneID();
        String question1_answer = user.getSecurityAnswerOne();
        String question2_id = "" + user.getSecurityQuestionTwoID();
        String question2_answer = user.getSecurityAnswerTwo();
        String profile = SerialisationHelper.serializeProfile(user.getProfile());

        String[] parameters = {firstName, lastName, email, username, salt, password, question1_id, question1_answer, question2_id, question2_answer, profile, userID};

        db.insert(statement, parameters);
    }

    /**
     * Creates a user from the database
     *
     * @param username username to get from the database
     * @return User from the database matching the username passed
     */
    public static User loadFromDatabase(String username) throws SQLException
    {
        DatabaseManagement db = new DatabaseManagement();

        String statement;

        if (username.contains("@"))
        {
            statement = "SELECT * FROM USERS WHERE email=?";
        }
        else
        {
            statement = "SELECT * FROM USERS WHERE username=?";
        }

        String[] parameters = {username};

        ArrayList<HashMap<String, String>> userData = db.select(statement, parameters);

        // Extract all the data from the ResultSet
        int fetched_userID = Integer.parseInt(userData.get(0).get("user_id"));
        String fetched_firstName = userData.get(0).get("first_name");
        String fetched_lastName = userData.get(0).get("last_name");
        String fetched_email = userData.get(0).get("email");
        String fetched_username = userData.get(0).get("username");
        String fetched_salt = userData.get(0).get("salt");
        String fetched_password = userData.get(0).get("password");
        int fetched_question1_id = Integer.parseInt(userData.get(0).get("question1_id"));
        String fetched_question1_answer = userData.get(0).get("question1_answer");
        int fetched_question2_id = Integer.parseInt(userData.get(0).get("question2_id"));
        String fetched_question2_answer = userData.get(0).get("question2_answer");
        String fetched_profile = userData.get(0).get("profile");
        // Deserialise the user profile data
        Profile profile = SerialisationHelper.deserializeProfile(fetched_profile);

        profile.setLoggedIn();

        User toReturn = new User(fetched_userID,
                fetched_firstName, fetched_lastName, fetched_username,
                fetched_email, fetched_salt, fetched_password, profile);

        toReturn.setSecurityQuestionOneID(fetched_question1_id);
        toReturn.setSecurityAnswerOne(fetched_question1_answer);
        toReturn.setSecurityQuestionTwoID(fetched_question2_id);
        toReturn.setSecurityAnswerTwo(fetched_question2_answer);
        return toReturn;
    }

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getSecurityQuestionOneID()
    {
        return securityQuestionOneID;
    }

    public void setSecurityQuestionOneID(int securityQuestionOneID)
    {
        this.securityQuestionOneID = securityQuestionOneID;
    }

    public String getSecurityAnswerOne()
    {
        return securityAnswerOne;
    }

    public void setSecurityAnswerOne(String securityAnswerOne)
    {
        this.securityAnswerOne = securityAnswerOne;
    }

    public int getSecurityQuestionTwoID()
    {
        return getSecurityQuestionTwoID;
    }

    public void setSecurityQuestionTwoID(int getSecurityQuestionTwoID)
    {
        this.getSecurityQuestionTwoID = getSecurityQuestionTwoID;
    }

    public String getSecurityAnswerTwo()
    {
        return securityAnswerTwo;
    }

    public void setSecurityAnswerTwo(String securityAnswerTwo)
    {
        this.securityAnswerTwo = securityAnswerTwo;
    }

    public Profile getProfile()
    {
        return profile;
    }

    public void setProfile(Profile profile)
    {
        this.profile = profile;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                ", securityQuestionOneID=" + securityQuestionOneID +
                ", securityAnswerOne='" + securityAnswerOne + '\'' +
                ", getSecurityQuestionTwoID=" + getSecurityQuestionTwoID +
                ", securityAnswerTwo='" + securityAnswerTwo + '\'' +
                ", profile=" + profile +
                '}';
    }

    @Override
    public boolean equals(Object user)
    {

        if (user == this)
        {
            return true;
        }

        if (!(user instanceof User))
        {
            return false;
        }

        User u = (User) user;

        return this.userID == u.userID;
    }

}