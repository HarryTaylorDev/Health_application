/*
Name:           StrengthGoal.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a StrengthGoal object (extended from Goal). This
                is a type of activity that the user has taken part in.
*/

package MVC.model;

import java.time.LocalDate;

public class StrengthGoal extends Goal
{
    private int currentWeight;
    private int currentReps;
    private int currentSets;
    private int targetWeight;
    private int targetReps;
    private int targetSets;

    /**
     * Constructor for StrengthGoal
     *
     * @param setDate       set date for goal
     * @param targetDate    target date for goal
     * @param activity      activity for goal
     * @param currentWeight current weight lifted
     * @param currentReps   current reps done
     * @param currentSets   current sets done
     * @param targetWeight  target weight to lift
     * @param targetReps    target reps
     * @param targetSets    target sets
     */
    public StrengthGoal(LocalDate setDate, LocalDate targetDate, String activity, int currentWeight, int currentReps, int currentSets, int targetWeight, int targetReps, int targetSets, Integer groupID)
    {
        super(setDate, targetDate, activity, groupID);
        this.currentWeight = currentWeight;
        this.currentReps = currentReps;
        this.currentSets = currentSets;
        this.targetWeight = targetWeight;
        this.targetReps = targetReps;
        this.targetSets = targetSets;
    }

    public int getCurrentWeight()
    {
        return currentWeight;
    }

    public int getCurrentReps()
    {
        return currentReps;
    }

    public int getCurrentSets()
    {
        return currentSets;
    }

    public int getTargetWeight()
    {
        return targetWeight;
    }

    public int getTargetReps()
    {
        return targetReps;
    }

    public int getTargetSets()
    {
        return targetSets;
    }

    @Override
    public String getStart()
    {
        return currentWeight + " kg X " + currentSets + " X " + currentReps;
    }

    @Override
    public String getTarget()
    {
        return targetWeight + " kg X " + targetSets + " X " + targetReps;
    }
}
