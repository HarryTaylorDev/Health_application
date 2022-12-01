/*
Name:           DatabaseManagement.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a DatabaseManagement object. These are used
                to interface with the database, reading from and writing
                data to the database.

                The class is initialised, then methods can be called as
                needed. The closing of all connections is handled internally,
                which lessens the possibility of exceptional database accessing
                behaviour occurring.
*/
package MVC.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManagement
{
    /**
     * Private helper method used to connect to the database each time.
     *
     * @return
     */
    private Connection connect()
    {
        String url = "jdbc:sqlite:src\\MVC\\model\\healthtracker.db";

        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(url);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Insert data into the database, passing in your statement and the
     * corresponding elements
     * e.g. INSERT INTO test(field1, field2) VALUES(?, ?)
     * elements[0] = field1 data
     * elements[1] = field2 data
     *
     * @param sqlStatement The insert statement to be run
     * @param elements     The elements to be inserted
     */
    public void insert(String sqlStatement, String[] elements)
    {

        try (Connection conn = this.connect();
             PreparedStatement prpstmnt = conn.prepareStatement(sqlStatement))
        {

            // Set the SQL statement to contain elements
            for (int j = 0; j < elements.length; j++)
            {
                String toAdd = elements[j];
                prpstmnt.setString(j + 1, toAdd);
            }

            prpstmnt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * Select data from the database using the passed in SQL statement
     *
     * @param sqlStatement the SQL statement to be run against the database
     * @param elements     the elements to be inserted
     * @return A list of HashMaps, each entry in the list representing a line
     * of data returned. The key for the HashMap is the column header within
     * the database
     * This method returns the string representation of items within the
     * database (i.e. all fields are returned as a string for comparability
     * reasons)
     */
    public ArrayList<HashMap<String, String>> select(String sqlStatement, String[] elements)
    {
        ArrayList<HashMap<String, String>> rows = new ArrayList<>();

        try (Connection conn = this.connect();

             PreparedStatement prpstmnt = conn.prepareStatement(sqlStatement))
        {
            // Set the SQL statement to contain elements
            for (int j = 0; j < elements.length; j++)
            {
                String toAdd = elements[j];
                prpstmnt.setString(j + 1, toAdd);
            }

            ResultSet rs = prpstmnt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int noOfColumns = rsmd.getColumnCount();

            // Iterate through ResultsSet
            while (rs.next())
            {
                HashMap<String, String> indivRow = new HashMap<String, String>();
                for (int i = 1; i <= noOfColumns; i++)
                {
                    indivRow.put(rsmd.getColumnName(i), rs.getString(i));
                }

                rows.add(indivRow);
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rows;
        //return results;
    }


    /**
     * Select data from the database using the passed in SQL statement
     *
     * @param sqlStatement the SQL statement to be run against the database
     * @param elements     the elements to be inserted
     * @return A list of HashMaps, each entry in the list representing a line
     * of data returned. The key for the HashMap is the column header within
     * the database
     * This method returns the object representation of items within the
     * database (e.g. an integer will return a Java Integer)
     */
    public ArrayList<HashMap<String, Object>> selectObject(String sqlStatement, String[] elements)
    {
        ArrayList<HashMap<String, Object>> rows = new ArrayList<>();

        try (Connection conn = this.connect();
             PreparedStatement prpstmnt = conn.prepareStatement(sqlStatement))
        {
            // Set the SQL statement to contain elements
            for (int j = 0; j < elements.length; j++)
            {
                String toAdd = elements[j];
                prpstmnt.setString(j + 1, toAdd);
            }

            ResultSet rs = prpstmnt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int noOfColumns = rsmd.getColumnCount();

            // Iterate through ResultsSet
            while (rs.next())
            {
                HashMap<String, Object> indivRow = new HashMap<String, Object>();
                for (int i = 1; i <= noOfColumns; i++)
                {
                    indivRow.put(rsmd.getColumnName(i), rs.getObject(i));
                }

                rows.add(indivRow);
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rows;
    }

}