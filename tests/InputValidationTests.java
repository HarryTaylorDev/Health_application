import MVC.utils.InputValidation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
public class InputValidationTests {

    @Test
    public void ageRestrictionsShouldApply(){
        LocalDate age = LocalDate.now();
        int ageLimit = 18;
        assertEquals(false, InputValidation.checkAgeRestriction(age, ageLimit));
    }

    @Test
    public void endDateShouldntBeBeforeStartDate(){
        LocalDate startDate = LocalDate.of(3000,1,1);
        LocalDate endDate = LocalDate.of(2000,1,1);
        assertEquals(false, InputValidation.checkDateRanges(startDate,endDate));
    }

    @Test
    public void nullDatesCantBeTested(){
        assertEquals(false, InputValidation.checkAgeRestriction(null, 18));
        assertEquals(false, InputValidation.checkDateAfterToday(null));
        assertEquals(false, InputValidation.checkDateRanges(null,null));
        assertEquals(false, InputValidation.checkDateBeforeToday(null));
    }

    @Test
    public void onlyFloatingPointShouldOnlyAcceptNumbers(){
        String numberToTest = "10.3";
        String numberToTest2 = "10";
        String failiureToTest = "Hello!";
        assertEquals(true, InputValidation.checkOnlyFloatingPoint(numberToTest));
        assertEquals(true, InputValidation.checkOnlyFloatingPoint(numberToTest2));
        assertEquals(false, InputValidation.checkOnlyFloatingPoint(failiureToTest));
    }

    @Test
    public void passwordsShouldBeSecure(){
        String validPassword = "Password#1";
        String invalidPassword1 = "password#1";
        String invalidPassword2 = "Password1";
        String invalidPassword3 = "Password#";
        String invalidPassword4 = "Pwd#1";

        assertEquals(true, InputValidation.checkPasswordSecurity(validPassword));
        assertEquals(false, InputValidation.checkPasswordSecurity(invalidPassword1));
        assertEquals(false, InputValidation.checkPasswordSecurity(invalidPassword2));
        assertEquals(false, InputValidation.checkPasswordSecurity(invalidPassword3));
        assertEquals(false, InputValidation.checkPasswordSecurity(invalidPassword4));
    }

    @Test
    public void emailsShouldBeValid(){
        String validEmail = "email@email.co.uk";
        String invalidEmail1 = "email@email";
        String invalidEmail2 = "email.com";

        assertEquals(true, InputValidation.checkOnlyEmail(validEmail));
        assertEquals(false, InputValidation.checkOnlyEmail(invalidEmail1));
        assertEquals(false, InputValidation.checkOnlyEmail(invalidEmail2));
    }

    @Test
    public void checkOnlyLettersShouldntAcceptNumbers(){
        String validString = "Hello";
        String invalidString = "Hello123";

        assertEquals(true, InputValidation.checkOnlyLetters(validString));
        assertEquals(false, InputValidation.checkOnlyLetters(invalidString));
    }

    @Test
    public void checkOnlyNumbersShouldntAcceptLetters(){
        String validString = "123";
        String invalidString = "Hello 123";

        assertEquals(true, InputValidation.checkOnlyNumbers(validString));
        assertEquals(false, InputValidation.checkOnlyNumbers(invalidString));
    }

    @Test
    public void lastNameShouldAcceptCertainSymbols(){
        String validString1 = "Dali-Chaouch";
        String validString2 = "O'Neal";
        String invalidString = "£McDonald";

        assertEquals(true, InputValidation.checkLastName(validString1));
        assertEquals(true, InputValidation.checkLastName(validString2));
        assertEquals(false, InputValidation.checkLastName(invalidString));
    }
}
