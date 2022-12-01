/*
Name:           CardioGoal.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a CardioGoal object (extended from Goal). This is
                a type of goal specific to Cardio. It stores attributes related
                to a CardioGoal, such as the current distance the user can do
                for a type of activity, the current time they can complete this
                distance in, as well as their goal distance and time.
*/
package MVC.model;

import java.time.LocalDate;

public class CardioGoal extends Goal
{
    private int currentDistance;
    private int currentTime;
    private int targetDistance;
    private int targetTime;

    /**
     * Default constructor that returns a fully defined CardioGoal object.
     *
     * @param setDate        the date the goal has been set
     * @param targetDate     the date which the user wishes to achieve their goal
     *                       by
     * @param activity       the activity they wish to do
     * @param startDistance  the distance they can do at the start
     * @param startTime      the time that it currently takes the user to complete
     *                       this distance
     * @param targetDistance the users target distance
     * @param targetTime     the users target time.
     */
    public CardioGoal(LocalDate setDate, LocalDate targetDate, String activity, int startDistance, int startTime, int targetDistance, int targetTime, Integer groupID)
    {
        super(setDate, targetDate, activity, groupID);
        this.currentDistance = startDistance;
        this.currentTime = startTime;
        this.targetDistance = targetDistance;
        this.targetTime = targetTime;
    }

    public int getCurrentDistance()
    {
        return currentDistance;
    }

    public void setCurrentDistance(int startDistance)
    {
        this.currentDistance = startDistance;
    }

    public int getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(int startTime)
    {
        this.currentTime = startTime;
    }

    public int getTargetDistance()
    {
        return targetDistance;
    }

    public void setTargetDistance(int targetDistance)
    {
        this.targetDistance = targetDistance;
    }

    public int getTargetTime()
    {
        return targetTime;
    }

    public void setTargetTime(int targetTime)
    {
        this.targetTime = targetTime;
    }

    @Override
    public String getStart()
    {
        return currentDistance + "m in " + currentTime + " minutes";
    }

    @Override
    public String getTarget()
    {
        return targetDistance + "m in " + targetTime + " minutes";
    }

    @Override
    public String toString()
    {
        return "CardioGoal{" +
                "currentDistance=" + currentDistance +
                ", currentTime=" + currentTime +
                ", targetDistance=" + targetDistance +
                ", targetTime=" + targetTime +
                '}';
    }
}
