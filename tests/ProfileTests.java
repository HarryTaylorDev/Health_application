import MVC.model.Day;
import MVC.model.Profile;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;



public class ProfileTests {

    @Test
    public void getWeightShouldBeNullIfNotSet () {
        Profile tester = new Profile();
        assertEquals(null, tester.getWeight());
    }

    @Test
    public void getWeightShouldReturnMostRecentWeight(){
        Profile tester = new Profile();

        Day day1 = new Day();
        Day day2 = new Day();
        day1.setWeight((float) 50);
        day2.setWeight((float) 100);
        tester.addDay(day1);
        tester.addDay(day2);
        assertEquals(100, tester.getWeight());
    }

    @Test
    public void bmrCalculationShouldBeCorrect(){
        Profile tester = new Profile();     //testing on Profile
        Profile tester2 = new Profile();    //ran on two different profiles to ensure it works for both men and women
        Day day1 = new Day();
        day1.setWeight((float)70);
        Day day2 = new Day();
        day2.setWeight((float)57);


        tester.addDay(day1);
        tester.setHeight((float)190);
        tester.setDoB(LocalDate.of(1999,8,9));
        tester.setAgeFromDoB();
        tester.setSex("MALE");

        tester2.addDay(day2);
        tester2.setHeight((float)167);
        tester2.setDoB(LocalDate.of(1988,1,1));
        tester2.setAgeFromDoB();
        tester2.setSex("FEMALE");

        assertEquals((float)1792.5,tester.getBMR());
        assertEquals((float)1292.75, tester2.getBMR());
    }

    @Test
    public void maintenanceCalculationShouldBeCorrect() {
        //RESUSED PROFILES FROM BMR CALCULATION
        Profile tester = new Profile();     //testing on Profile
        Profile tester2 = new Profile();    //ran on two different profiles to ensure it works for both men and women
        Day day1 = new Day();
        day1.setWeight((float)70);
        Day day2 = new Day();
        day2.setWeight((float)57);


        tester.addDay(day1);
        tester.setHeight((float)190);
        tester.setDoB(LocalDate.of(1999,8,9));
        tester.setAgeFromDoB();
        tester.setSex("MALE");
        tester.setLifeStyle(Profile.LifeStyle.HIGH);

        tester2.addDay(day2);
        tester2.setHeight((float)167);
        tester2.setDoB(LocalDate.of(1988,1,1));
        tester2.setAgeFromDoB();
        tester2.setSex("FEMALE");
        tester2.setLifeStyle(Profile.LifeStyle.MODERATE);

        assertEquals((float)3047.25,tester.getMaintenanceCalories());
        assertEquals((float)1939.125,tester2.getMaintenanceCalories());
    }

    @Test
    public void bmiShouldBeAccurate(){
        Profile tester = new Profile();
        Day day = new Day();

        day.setWeight((float)70);
        tester.addDay(day);
        tester.setHeight((float)175);

        assertEquals((float)22.857143, tester.getBMI());
    }

    @Test
    public void testDaysShouldAddProperly(){
        Profile tester = new Profile();
        Day day1 = new Day();
        Day day2 = new Day();

        tester.addDay(day1);
        tester.addDay(day2);

        assertEquals(day1, tester.getDays().get(0));
        assertEquals(day2, tester.getDays().get(1));
    }

    @Test
    public void ageShouldSetProperly(){
        Profile tester = new Profile();
        tester.setDoB(LocalDate.of(1990,1,6));
        tester.setAgeFromDoB();

        assertEquals(30,tester.getAge());
    }

    @Test
    public void defaultDataShouldLoadProperly(){
        Profile tester = new Profile();
        tester.loadDefaultData();

        assertNotEquals(null, tester.getFoods());
    }

    @Test
    public void currentDateShouldAddIfLastDateIsNotToday () {
        Profile tester = new Profile();

        Day dayOld = new Day();
        dayOld.setDate(LocalDate.of(2000,1,1));

        Day todayDate = new Day();
        todayDate.setDate(LocalDate.now());

        tester.addDay(dayOld);
        tester.checkCurrentDay();

        assertEquals(todayDate.getDate(), tester.getCurrentDay().getDate());
    }
}
