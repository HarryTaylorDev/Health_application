/*
Name:           Goal.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a Goal. This is an abstract class that specific
                goal
*/
package MVC.model;

import java.time.LocalDate;

public abstract class Goal
{
    private LocalDate setDate;
    private LocalDate targetDate;
    private String activity;
    private Integer groupID;

    /**
     * Constructor for Goal class
     *
     * @param setDate    date the goal is set
     * @param targetDate target date the goal is aiming for
     * @param activity   the activity the goal is for
     */
    public Goal(LocalDate setDate, LocalDate targetDate, String activity, Integer groupID)
    {
        this.setDate = setDate;
        this.targetDate = targetDate;
        this.activity = activity;
        this.groupID = groupID;
    }

    public String getActivity()
    {
        return activity;
    }

    public abstract String getStart();

    public abstract String getTarget();

    public LocalDate getSetDate()
    {
        return setDate;
    }

    public LocalDate getTargetDate()
    {
        return targetDate;
    }

    @Override
    public boolean equals(Object goal)
    {
        if (goal == this)
        {
            return true;
        }

        if (!(goal instanceof User))
        {
            return false;
        }

        Goal g = (Goal) goal;

        return this.activity.equals(g.activity) &&
                this.setDate.equals(g.setDate) &&
                this.targetDate.equals(g.targetDate) &&
                this.groupID.equals(g.groupID);
    }
}
