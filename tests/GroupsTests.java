import MVC.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class GroupsTests
{
    //Set of tests to test the features that were added for groups
    Announcement a1 = new Announcement("announcement1", "user1");
    Announcement a2 = new Announcement ("announcement2", "user2");
    Group group1 = new Group("user1","group1","this is group 1,", "1");
    Profile user1profile = new Profile();
    User user1 = new User(1,"user","1","user1","user1@email.com","n/a","n/a", user1profile);
    StrengthGoal goal1 = new StrengthGoal(LocalDate.MIN, LocalDate.MAX,"test",10,10,10,10,10,10,10);

    @Test
    public void AnnouncementsShouldBeComparable(){
        //Tests the compare to statement to make sure it sorts in the correct order
        a1.setDate(LocalDate.of(2020,6,20));
        a2.setDate(LocalDate.of(1990,6,20));
        assertEquals(30, a1.compareTo(a2));
    }

    @Test
    public void UsersShouldBeAddedToGroup(){
        //Test to see if a user can successfully be added to a group
        group1.removeUser(user1);
        assertEquals(false, group1.checkGroupForUser("user1"));
        group1.addUser(user1);
        assertEquals(true, group1.checkGroupForUser("user1"));
    }

    @Test
    public void UsersShouldBeRemovedFromGroup(){
        //Test to see if a user can be removed from a group
        assertEquals(true, group1.checkGroupForUser("user1"));
        group1.removeUser(user1);
        assertEquals(false, group1.checkGroupForUser("user1"));
    }

    @Test
    public void GroupGoalsShouldBeAdded(){
        //Test to see if a new group goal can be added
        group1.setGoal(goal1);
        assertEquals(goal1, group1.getGoal());
    }

    @Test
    public void AnnouncementsShouldBeAdded(){
        //Test to see if announcements can be added
        group1.addAnnouncement(a1);
        group1.addAnnouncement(a2);
        assertEquals(true, group1.getAnnouncementList().contains(a1) && group1.getAnnouncementList().contains(a2));
    }



}
