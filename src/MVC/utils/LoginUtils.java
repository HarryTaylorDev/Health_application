/*
Name:           LoginUtils.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Small helper class to help manage login functionality.
                This class is designed to be intitialised and called by the
                LoginController class.
*/
package MVC.utils;

import MVC.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginUtils
{
    private String userName;
    private String password;

    /**
     * Simple constructor for LoginController object
     *
     * @param userName the username / email of the user
     * @param password the password of the user
     */
    public LoginUtils(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
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

    public static Map<String, Float> loadFormula()
    {
        Map<String, Float> formulaValue = new HashMap<>();
        try
        {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Float>>()
            {
            }.getType();

            Reader reader = Files.newBufferedReader(Paths.get("src\\MVC\\config\\formulaConfig.json"));
            formulaValue = gson.fromJson(reader, type);
            reader.close();

        }
        catch (Exception ex)
        {
            //default to normal values
            formulaValue.put("weight", new Float(10.0));
            formulaValue.put("height", new Float(6.25));
            formulaValue.put("age", new Float(5.0));
            formulaValue.put("male", new Float(5.0));
            formulaValue.put("female", new Float(-161.0));
        }
        return formulaValue;
    }

    /**
     * Simple main method (for testing purposes)
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException
    {
        LoginUtils loginUtils = new LoginUtils("sedsallrr", "360Ux0S89DqG");
        System.out.println(loginUtils.login());
        System.out.println(loginUtils.getUser());
    }

    /**
     * Checks if the user exists in the database, and if so, whether the
     * password is correct or not. Returns true if the entered user credentials
     * are correct, and false if either the user does not exist, or if the
     * passed credentials are wrong
     *
     * @return True if the user credentials are correct, false otherwise
     * @throws SQLException if the is a Database error
     */
    public boolean login() throws SQLException
    {
        boolean login = false;
        DatabaseManagement db = new DatabaseManagement();

        String checkIfExistsStatement;
        String userFetchStatement;

        if (userName.contains("@"))
        {
            checkIfExistsStatement = "SELECT COUNT(*) AS count FROM USERS WHERE email=?";
            userFetchStatement = "SELECT email, username, salt, password FROM USERS WHERE email=?";
        }
        else
        {
            checkIfExistsStatement = "SELECT COUNT(*) AS count FROM USERS WHERE username=?";
            userFetchStatement = "SELECT email, username, salt, password FROM USERS WHERE username=?";
        }

        String[] parameters = {userName};
        ArrayList<HashMap<String, String>> userData = db.select(checkIfExistsStatement, parameters);

        int count = Integer.parseInt(userData.get(0).get("count"));
        if (count < 1)
            return false;

        ArrayList<HashMap<String, String>> loginList = db.select(userFetchStatement, parameters);
        String fetched_email = loginList.get(0).get("email");
        String fetched_username = loginList.get(0).get("username");
        String fetched_salt = loginList.get(0).get("salt");
        String fetched_password = loginList.get(0).get("password");

        String hashedPassword = hashDataWithSalt(password, fetched_salt);

        if (hashedPassword.equals(fetched_password))
            login = true;

        return login;
    }

    /**
     * Selects the correct user data from the database, and constructs a user
     * object using this data, complete with profile information. This method
     * uses the LoginController user variables.
     *
     * @return A instance of User, with the correct data
     * @throws SQLException if a Database exception occurs
     */
    public User getUser() throws SQLException
    {
        User toBeReturned = User.loadFromDatabase(userName);
        toBeReturned.getProfile().setFormulaValue(loadFormula());
        return toBeReturned;
    }
}
