package MVC.model;

import MVC.utils.DatabaseManagement;
import MVC.utils.EmailSender;
import MVC.utils.SerialisationHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Group
{
    private String owner;
    private ArrayList<String> userList;
    private Goal goal;
    private ArrayList<Announcement> announcementList;
    private Integer groupID;
    private String name;
    private String description;
    private String imgID;

    /**
     * constructor to make a group with specific values
     * @param owner username of the owner of the group
     * @param name name of the group
     * @param description group description
     * @param imgID id of the group image
     */
    public Group(String owner, String name, String description, String imgID)
    {
        this.owner = owner;
        this.name = name;
        this.description = description;
        userList = new ArrayList<>();
        userList.add(owner);
        announcementList = new ArrayList<>();
        this.imgID = imgID;
    }

    /**
     * function to write a new group to the database from a group object
     * @param group group to be written to database
     */
    public static void writeNewGroupToDatabase(Group group)
    {
        DatabaseManagement db = new DatabaseManagement();

        String sqlStatement = "INSERT INTO groups(group_name, group_info) VALUES (?, ?)";

        String[] fields = new String[2];
        fields[0] = group.getName();
        fields[1] = SerialisationHelper.serializeGroup(group);
        db.insert(sqlStatement, fields);

        // Getting id and setting group to it
        String getIDStatement = "SELECT group_id FROM groups ORDER BY group_id DESC LIMIT 1;";
        ArrayList<HashMap<String, String>> lastID = db.select(getIDStatement, new String[0]);
        int groupID = Integer.parseInt(lastID.get(0).get("group_id"));
        group.setGroupID(groupID);

        Group.writeExistingGroupToDatabase(group);
    }

    /**
     * function to write an existing group to the database from a group object
     * @param group group to rewritten
     */
    public static void writeExistingGroupToDatabase(Group group)
    {
        DatabaseManagement db = new DatabaseManagement();
        String sqlStatement = "UPDATE groups SET group_info=? WHERE group_id=?";

        String[] fields = new String[2];
        fields[0] = SerialisationHelper.serializeGroup(group);
        fields[1] = "" + group.groupID.toString();

        db.insert(sqlStatement, fields);
    }

    /**
     *  function to load a group from the database from its ID
     * @param groupID ID of group to be loaded
     * @return returns the group object
     * @throws SQLException
     */
    public static Group loadGroupFromDatabase(Integer groupID) throws SQLException
    {
        DatabaseManagement db = new DatabaseManagement();

        String statement = "SELECT * FROM groups WHERE group_id=?";

        String[] parameters = {groupID.toString()};

        ArrayList<HashMap<String, String>> userData = db.select(statement, parameters);

        String groupString = userData.get(0).get("group_info");
        Group group = SerialisationHelper.deserializeGroup(groupString);

        return group;
    }

    /**
     * function to remove a group from the database by passing in the group object
     * @param group group object to be removed
     */
    public static void removeGroupFromDatabase(Group group)
    {
        DatabaseManagement db = new DatabaseManagement();
        String sqlStatement = "DELETE FROM groups WHERE group_id = ?";
        String[] fields = new String[1];
        fields[0] = "" + group.getGroupID();
        db.insert(sqlStatement, fields);
    }

    /**
     * function to add a new invite code to the database along with the user ID
     * and group ID for the user to be added to
     * @param code invite code
     * @param groupID group id of group for user to be added to
     * @param userID Id of user to be added
     */
    public static void addInviteCodeToDatabase(String code, int groupID, int userID)
    {
        DatabaseManagement db = new DatabaseManagement();
        String sqlStatement = "INSERT INTO invite_codes(code, group_id, user_id) VALUES (?,?,?)";
        String[] fields = new String[3];
        fields[0] = code;
        fields[1] = "" + groupID;
        fields[2] = "" + userID;
        db.insert(sqlStatement, fields);
    }

    /**
     * function to check if an invite code is in the database
     * @param code code to be checked
     * @return returns true if the code is in db
     */
    public static boolean checkDatabaseForCode(String code)
    {
        DatabaseManagement db = new DatabaseManagement();
        String checkStatement = "SELECT COUNT(*) AS count FROM invite_codes WHERE code = ?";
        String[] fields = new String[1];
        fields[0] = code;

        ArrayList<HashMap<String, String>> count = db.select(checkStatement, fields);
        int returnedCount = Integer.parseInt(count.get(0).get("count"));

        return returnedCount > 0;
    }

    /**
     * function to check if a invite code is in the database with the right
     * user ID
     * @param code code to be looked for
     * @param userID user id for the invite code
     * @return
     */
    public static boolean verifyUserCode(String code, int userID)
    {
        DatabaseManagement db = new DatabaseManagement();
        String checkStatement = "SELECT COUNT(*) AS count FROM invite_codes WHERE code = ? AND user_id = ?";
        String[] fields = new String[2];
        fields[0] = code;
        fields[1] = "" + userID;

        ArrayList<HashMap<String, String>> count = db.select(checkStatement, fields);
        int returnedCount = Integer.parseInt(count.get(0).get("count"));
        return returnedCount > 0;
    }

    /**
     * function to check if a user is already invited to a specific group
     * @param groupID group to check for invite
     * @param userID user id to check for if already invited
     * @return
     */
    public static boolean checkForAlreadyExistingInvite(int groupID, int userID)
    {
        DatabaseManagement db = new DatabaseManagement();
        String checkStatement = "SELECT COUNT(*) AS count FROM invite_codes WHERE group_id = ? AND user_id = ?";

        String[] fields = new String[2];
        fields[0] = "" + groupID;
        fields[1] = "" + userID;

        ArrayList<HashMap<String, String>> count = db.select(checkStatement, fields);
        int returnedCount = Integer.parseInt(count.get(0).get("count"));

        return returnedCount > 0;
    }

    /**
     * function to return the group id from an invite code in the database
     * @param code invite code to search for
     * @return the Id of the group that the user has been invited too
     */
    public static int getGroupIDFromCode(String code)
    {
        DatabaseManagement db = new DatabaseManagement();
        String statement = "SELECT * FROM invite_codes WHERE code = ?";
        String[] fields = new String[1];
        fields[0] = code;

        ArrayList<HashMap<String, String>> codeInfo = db.select(statement, fields);
        int groupID = Integer.parseInt(codeInfo.get(0).get("group_id"));

        return groupID;
    }

    /**
     * function to send an email to a user to tell them they have been invited
     * and to give them their code
     * @param email users email
     * @param username users username
     * @param groupname group name that user has been invited too
     * @param owner owner of the group
     * @param description description of the group
     * @param code invite code to the group
     * @throws Exception
     */
    public static void sendInviteEmail(String email, String username,
                                       String groupname, String owner,
                                       String description, String code) throws Exception
    {
        String message = loadFromDatabase("src\\MVC\\config\\email_template_add_to_group.txt");

        message = message.replace("<USERNAME>", username);
        message = message.replace("<GROUP>", groupname);
        message = message.replace("<OWNER>", owner);
        message = message.replace("<DESCRIPTION>", description);
        message = message.replace("<CODE>", code);

        System.out.println("Sending to: " + email);

        System.out.println(message);

        EmailSender.sendEmail(email, "Your Group Invitation Code", message);
    }

    /**
     * function to tell the users in a group that a new goal has been added to a group
     * @param emails email for the message to sent too
     * @param groupname name of group that the goal has been added too
     * @throws Exception
     */
    public static void sendGoalEmail(String emails, String groupname) throws Exception
    {
        String message = loadFromDatabase("src\\MVC\\config\\email_template_goal_added.txt");

        message = message.replace("<GROUPNAME>", groupname);

        StringBuilder emailSubject = new StringBuilder("A new goal has been added to ");
        emailSubject.append(groupname);

        EmailSender.sendEmail(emails, emailSubject.toString(), message);
    }

    /**
     * function to load the email template from the database
     * @param filepath path to the email template
     * @return
     * @throws IOException
     */
    private static String loadFromDatabase(String filepath) throws IOException
    {
        StringBuilder toBeReturned = new StringBuilder();
        File file = new File(filepath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            toBeReturned.append(st + "\n");
        return toBeReturned.toString();
    }

    /**
     * function to remove an invite code from the database
     * @param code code to be removed
     */
    public static void removeInviteCodeFromDatabase(String code)
    {
        DatabaseManagement db = new DatabaseManagement();
        String sqlStatement = "DELETE FROM invite_codes WHERE code = ?";
        String[] fields = new String[1];
        fields[0] = code;
        db.insert(sqlStatement, fields);
    }

    /**
     * function to generate a random invite code
     * @return returns a string of 6 long random numbers and or characters
     */
    public static String generateInviteCode()
    {
        SecureRandom random = new SecureRandom();

        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++)
        {
            // Generating numbers between bounds taken from here
            //https://mkyong.com/java/java-generate-random-integers-in-a-range/

            int currentGenerationCode = random.nextInt(3) + 1;

            // Append random number
            if (currentGenerationCode == 1)
                code.append(random.nextInt(10));

            // Append capital letter
            if (currentGenerationCode == 2)
                code.append((char) (random.nextInt((90 - 65) + 1) + 65));

            //(random.nextInt((122 - 97) + 1) + 97)
            // Append lower case letter
            if (currentGenerationCode == 3)
                code.append((char) (random.nextInt((122 - 97) + 1) + 97));
        }

        return code.toString();
    }

    /**
     * function to check if a group name has already been taken
     * @param name group name to be checked
     * @return returns true if the name is taken
     */
    public static boolean isNameTaken(String name)
    {
        DatabaseManagement db = new DatabaseManagement();
        String checkStatement = "SELECT COUNT(*) AS count FROM groups WHERE group_name = ?";
        String[] fields = new String[1];
        fields[0] = name;

        ArrayList<HashMap<String, String>> count = db.select(checkStatement, fields);
        int returnedCount = Integer.parseInt(count.get(0).get("count"));
        return returnedCount > 0;
    }

    public void addUser(User user)
    {
        userList.add(user.getUsername());
        user.getProfile().addGroup(groupID);
    }

    public void removeUser(User user)
    {
        userList.remove(user.getUsername());
        user.getProfile().removeGroup(groupID);
    }

    public void newMessage(String message, User userName)
    {
        announcementList.add(new Announcement(message, userName.getUsername()));
    }

    public String getOwner()
    {
        return owner;
    }

    public ArrayList<String> getUserList()
    {
        return userList;
    }

    public Goal getGoal()
    {
        return goal;
    }

    public void setGoal(Goal newGoal)
    {
        goal = newGoal;
    }

    public ArrayList<Announcement> getAnnouncementList()
    {
        return announcementList;
    }

    public Integer getGroupID()
    {
        return groupID;
    }

    public void setGroupID(Integer groupID)
    {
        this.groupID = groupID;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getImgID()
    {
        return imgID;
    }

    /**
     * function to check if given user is in a group
     * @param username user to check for in group
     * @return true if the user is in the group
     */
    public boolean checkGroupForUser(String username)
    {
        boolean userExistsInGroup = false;
        for (int i = 0; i < userList.size(); i++)
        {
            if (userList.get(i).equals(username))
                userExistsInGroup = true;
        }

        return userExistsInGroup;
    }

    @Override
    public String toString()
    {
        return "Group{" +
                "owner='" + owner + '\'' +
                ", userList=" + userList +
                ", goal=" + goal +
                ", announcementList=" + announcementList +
                ", groupID=" + groupID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgID=" + imgID +
                '}';
    }

    public void addAnnouncement(Announcement announcement)
    {
        announcementList.add(announcement);
    }
}
