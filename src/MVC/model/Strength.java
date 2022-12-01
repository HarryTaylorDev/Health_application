/*
Name:           Strength.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a Strength object, containing weight, sets and reps
*/

package MVC.model;

public class Strength extends Exercise
{
    private int weight;
    private int sets;
    private int reps;

    /**
     * Constructor for Strength object
     *
     * @param activity Activity type
     * @param weight   weight lifted
     * @param sets     sets done
     * @param reps     reps done
     */
    public Strength(Activity activity, int weight, int sets, int reps)
    {
        super(activity);
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public int getSets()
    {
        return sets;
    }

    public int getReps()
    {
        return reps;
    }

    /**
     * function to make a string of what the user has completed
     * @return returns a string of what the user has completed
     */
    @Override
    public String getAchieved()
    {
        return sets + " sets of " + reps + " of " + weight + "kg";
    }

    @Override
    public float getAmount()
    {
        return reps;
    }

    @Override
    public String toString()
    {
        return "Strength{" +
                "weight=" + weight +
                ", sets=" + sets +
                ", reps=" + reps +
                '}';
    }
}
