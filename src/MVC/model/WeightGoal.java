/*
Name:           WeightGoal.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a WeightGoal object (extended from Goal). This
                class stores current weight and target weight of a weight goal
*/

package MVC.model;

import java.time.LocalDate;

public class WeightGoal extends Goal
{
    private Float currentWeight;
    private Float targetWeight;

    /**
     * Constructor for a weight goal
     *
     * @param setDate       date the goal is set
     * @param targetDate    target date for the goal
     * @param currentWeight the current weight of the user, to be stored to
     *                      see progress over time
     * @param targetWeight  the target weight the user is aiming for
     */
    public WeightGoal(LocalDate setDate, LocalDate targetDate, Float currentWeight, Float targetWeight, Integer groupID)
    {
        super(setDate, targetDate, "weight", groupID);
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
    }

    public Float getCurrentWeight()
    {
        return currentWeight;
    }

    public void setCurrentWeight(Float currentWeight)
    {
        this.currentWeight = currentWeight;
    }

    public Float getTargetWeight()
    {
        return targetWeight;
    }

    public void setTargetWeight(Float targetWeight)
    {
        this.targetWeight = targetWeight;
    }

    @Override
    public String getStart()
    {
        return currentWeight.toString();
    }

    @Override
    public String getTarget()
    {
        return targetWeight.toString();
    }

    @Override
    public String toString()
    {
        return "WeightGoal{" +
                "currentWeight=" + currentWeight +
                ", targetWeight=" + targetWeight +
                '}';
    }
}
