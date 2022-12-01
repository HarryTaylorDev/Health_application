/*
Name:           Announcement.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines an announcement object.
*/

package MVC.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Announcement implements Comparable<Announcement>
{
    private String user;
    private String announcement;
    private LocalDate date;
    private String groupName;

    public Announcement(String announcement, String username)
    {
        user = username;
        this.announcement = announcement;
        date = LocalDate.now();
    }

    public String getUser()
    {
        return user;
    }

    public String getAnnouncement()
    {
        return announcement;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    /**
     * Only used for testing purposes:
     * Sets the date of an announcement
     * @param date date to be set
     */
    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    /**
     * Function to print an announcement for the group hub page, e.g.
     * USERNAME, MONTH DD, YYYY
     * ANNOUNCEMENT
     * @return announcement in formatted version
     */
    public String printForHub()
    {
        String output = "%s, %s\n%-20s\n\n";
        return String.format(output, user, date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)), announcement);
    }

    /**
     * Function to print an announcement for the group dashboard page, e.g.
     * GROUP NAME, MONTH DD, YYYY
     * ANNOUNCEMENT
     * @return announcement in formatted version
     */
    public String printForDashboard()
    {
        String output = ", %s\n%-20s\n\n";
        return String.format(output, date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)), announcement);
    }

    @Override
    public String toString()
    {
        return user + ", " + date + ", " + announcement;
    }

    @Override
    public int compareTo(Announcement o)
    {
        return getDate().compareTo(o.getDate());
    }
}
