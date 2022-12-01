import MVC.model.*;
import MVC.utils.InputValidation;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class Phase2OtherTests
{
    //A set of tests to test non-group related features we added for phase 2

    @Test
    public void ShouldFindDatesBeforeToday(){
        //Test to see if we can check if a date is before today
        assertEquals(true, InputValidation.checkDateBeforeToday(LocalDate.MIN));
        assertEquals(false, InputValidation.checkDateBeforeToday(LocalDate.now()));
        assertEquals(false, InputValidation.checkDateBeforeToday(LocalDate.MAX));
    }

    @Test
    public void ShouldFindDatesAfterToday(){
        //Test to see if we can check if a date is after today
        assertEquals(false, InputValidation.checkDateAfterToday(LocalDate.MIN));
        assertEquals(false, InputValidation.checkDateAfterToday(LocalDate.now()));
        assertEquals(true, InputValidation.checkDateAfterToday(LocalDate.MAX));
    }

    @Test
    public void ShouldFindNumberOfDaysBetweenDates(){
        //Test to see if we can find the number of days between two dates.
        assertEquals(0,InputValidation.calculateNumberOfDaysBetweenDates(LocalDate.now(),LocalDate.now()));
        assertEquals(10, InputValidation.calculateNumberOfDaysBetweenDates(LocalDate.of(2020,6,20),LocalDate.of(2020,6,30)));
        assertEquals(-10,InputValidation.calculateNumberOfDaysBetweenDates(LocalDate.of(2020,6,30),LocalDate.of(2020,6,20)));
    }

}
