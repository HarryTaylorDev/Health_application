/*
Name:           Points.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a points object, used to give the user some incentive
                to keep completing goals
*/

package MVC.model;

public class Points
{
    private int total;
    private int totalToday;
    private int todayGoalPoints;            //a user can only get 100 points a day from goals, this int keeps track of how many they have gotten so far from goals.
    private boolean calorieTodayMet;        //a user gets 150 points from reaching their calorie goal. This boolean keeps track of wether or not they have reached it (and not exceeded)
    private boolean loggedInToday;          //a user gets 50 points a day for logging in, this keeps track of if they have gotten these points today or not.
    /**
     * Points constructor
     */
    public Points()
    {
        total = 0;
        totalToday = 0;
        todayGoalPoints = 0;
        calorieTodayMet = false;
        loggedInToday = false;
    }

    /**
     * Points constructor
     *
     * @param total           total points
     * @param totalToday      total points today
     * @param todayGoalPoints points from goals today
     * @param calorieTodayMet true if user has met calorie goal, false if not
     * @param loggedInToday   true if the user has logged in today, false if not
     */
    public Points(int total, int totalToday, int todayGoalPoints, boolean calorieTodayMet, boolean loggedInToday)
    {
        this.total = total;
        this.totalToday = totalToday;
        this.todayGoalPoints = todayGoalPoints;
        this.calorieTodayMet = calorieTodayMet;
        this.loggedInToday = loggedInToday;
    }

    /**
     * Adds points to a point object
     *
     * @param pointsToAdd how many points to add
     * @param pointtype   point type
     * @return boolean if points are added, false if not
     */
    public boolean addPoints(int pointsToAdd, PointType pointtype)
    {
        boolean pointsAdded = false;

        if (totalToday + pointsToAdd > 300)
        {
            pointsToAdd = 300 - totalToday; //adds up to 300
        }

        if (pointtype == PointType.CALORIEMET && !calorieTodayMet)
        {
            total += pointsToAdd;
            totalToday += pointsToAdd;
            calorieTodayMet = true;
            pointsAdded = true;
        }

        if (pointtype == PointType.GOAL && todayGoalPoints < 100)
        {
            total += pointsToAdd;
            totalToday += pointsToAdd;
            todayGoalPoints += pointsToAdd;
            pointsAdded = true;
        }

        if (pointtype == PointType.LOGIN && !loggedInToday)
        {
            total += pointsToAdd;
            totalToday += pointsToAdd;
            loggedInToday = true;
            pointsAdded = true;
        }

        return pointsAdded;
    }

    /**
     * Removes points if the user has not met their calorie goal
     */
    public void removeCalorieMet()
    {
        setCalorieTodayMet(false);
        total -= 150;
        totalToday -= 150;
    }

    public void setLoggedInToday(boolean loggedInToday)
    {
        this.loggedInToday = loggedInToday;
    }

    //Getters
    public int getTodayGoalPoints()
    {
        return todayGoalPoints;
    }

    public void setTodayGoalPoints(int todayGoalPoints)
    {
        this.todayGoalPoints = todayGoalPoints;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getTotalToday()
    {
        return totalToday;
    }

    public void setTotalToday(int totalToday)
    {
        this.totalToday = totalToday;
    }

    public boolean isCalorieTodayMet()
    {
        return calorieTodayMet;
    }

    //Setters
    public void setCalorieTodayMet(boolean calorieTodayMet)
    {
        this.calorieTodayMet = calorieTodayMet;
    }

    // point type enum
    public enum PointType
    {
        GOAL, LOGIN, CALORIEMET
    }
}
