/*
Name:           Cardio.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    Class defines a Cardio object (extended from Exercise). This
                is a type of activity that the user has taken part in.
*/

package MVC.model;

public class Cardio extends Exercise
{
    private int distance;
    private int time;

    /**
     * Constructs a Cardio object from passed in params
     *
     * @param activity the activity being done e.g. rowing
     * @param distance the distance the user has done this for
     * @param time     the time taken
     */
    public Cardio(Activity activity, int distance, int time)
    {
        super(activity);
        this.distance = distance;
        this.time = time;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    @Override
    public String getAchieved()
    {
        return distance + "m in " + time + " minutes";
    }

    @Override
    public float getAmount()
    {
        return time;
    }

    @Override
    public String toString()
    {
        return "Cardio{" +
                "distance=" + distance +
                ", time=" + time +
                '}';
    }
}
