import MVC.model.Points;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PointTests {

    @Test
    public void pointsShouldNotBeAddedIfTotalPointsTodayIs300() {
        Points tester = new Points(); //Points is tested

        //assert statements
        assertEquals(true, tester.addPoints(150, Points.PointType.CALORIEMET));
        assertEquals(true, tester.addPoints(50, Points.PointType.LOGIN));
        assertEquals(true, tester.addPoints(100, Points.PointType.GOAL));
        assertEquals(false, tester.addPoints(50, Points.PointType.GOAL));
    }

    @Test
    public void pointsShouldNotBeGivenIfLoggingInAgainOnTheSameDay() {
        Points tester = new Points(); //Points is tested

        //assert statements
        assertEquals(true, tester.addPoints(50, Points.PointType.LOGIN));
        assertEquals(false, tester.addPoints(50, Points.PointType.LOGIN));
    }

    @Test
    public void pointsShouldNotBeGivenIfMeetingTheCalorieGoalAgain() {
        Points tester = new Points(); //Points is tested

        //assert statements
        assertEquals(true, tester.addPoints(150, Points.PointType.CALORIEMET));
        assertEquals(false, tester.addPoints(150, Points.PointType.CALORIEMET));
    }

    @Test
    public void pointsShouldNotBeGivenIfAlreadyAdded100TodayGoalPoints() {
        Points tester = new Points(); //Points is tested

        //assert statements
        assertEquals(true, tester.addPoints(100, Points.PointType.GOAL));
        assertEquals(false, tester.addPoints(100, Points.PointType.GOAL));
    }

    @Test
    public void pointsShouldAddUpTo300AndNotAddAnyHigher () {
        Points tester = new Points(); //Points is tested

        //assert statements
        tester.addPoints(150, Points.PointType.CALORIEMET);
        assertEquals(150, tester.getTotal());
        tester.addPoints(50, Points.PointType.LOGIN);
        assertEquals(200, tester.getTotal());
        tester.addPoints(95, Points.PointType.GOAL);
        assertEquals(295, tester.getTotal());
        tester.addPoints(150, Points.PointType.GOAL);
        assertEquals(300, tester.getTotal());
        tester.addPoints(50, Points.PointType.LOGIN);
        assertEquals(300, tester.getTotal());
    }

}
