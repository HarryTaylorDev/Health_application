package MVC.utils;

import MVC.model.Group;
import MVC.model.User;

public class TempGroupCreation
{
    public static void main(String[] args) throws Exception
    {
        //String owner, String name, String description, Integer imgID)
        Group group = new Group("a",
                "Testing Group",
                "A group to test the features of this app",
                "boxing");

        String groupString = SerialisationHelper.serializeGroup(group);
        System.out.println(groupString);


        Group.writeNewGroupToDatabase(group);

        User tempUser = User.loadFromDatabase("a");
        tempUser.getProfile().addGroup(1);
        User.writeExistingUserToDatabase(tempUser);
    }
}
