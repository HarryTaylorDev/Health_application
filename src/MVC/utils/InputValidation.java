/*
Name:           InputValidation.java

Authors:        Chris, Conor, Harry, Milo, Yacine

Description:    A class to check the input of the application
*/

package MVC.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation
{
    /**
     * Check the input date is older than the age restriction passed and is non null
     * i.e. passing a date and 13 would check if the date was older than 13 years ago
     *
     * @param input    date to check
     * @param ageLimit age restriction, ie 13 would be 13 years ago
     * @return true if the input date is before the age restriction date
     */
    public static boolean checkAgeRestriction(LocalDate input, int ageLimit)
    {
        if (input == null)
        {
            return false;
        }

        LocalDate ageRestriction = LocalDate.now().minusYears(ageLimit);

        // if the input date is before the age limit date, return true, else false
        return (input.isBefore(ageRestriction));
    }

    /**
     * Check the input date is before the date today and is non null
     *
     * @param input date to compare
     * @return true if input is before the date of today, false if not
     */
    public static boolean checkDateBeforeToday(LocalDate input)
    {
        if (input == null)
        {
            return false;
        }

        LocalDate today = LocalDate.now();

        // if the input date is before today return true, else false
        return (input.isBefore(today));
    }

    /**
     * Checks first is before the second date and they are non null
     *
     * @param startDate  first date
     * @param targetDate second date
     * @return true if first date is before second, false if not
     */
    public static boolean checkDateRanges(LocalDate startDate, LocalDate targetDate)
    {
        if (startDate == null || targetDate == null)
        {
            return false;
        }

        int result = startDate.compareTo(targetDate);

        return result < 0;
    }

    /**
     * Check the input date is after the date today and is non null
     *
     * @param input date to compare
     * @return true if input is after the date of today, false if not
     */
    public static boolean checkDateAfterToday(LocalDate input)
    {
        if (input == null)
        {
            return false;
        }

        LocalDate today = LocalDate.now();

        // if the input date is before today return true, else false
        return (input.isAfter(today));
    }

    /**
     * Calculates the amount of days between two dates, includes boundary dates
     * e.g. 20/06/2020 and 27/06/2020 would return 7.
     *
     * @param a date a
     * @param b date b
     * @return number of days between date a and date b
     */
    public static long calculateNumberOfDaysBetweenDates(LocalDate a, LocalDate b)
    {
        return ChronoUnit.DAYS.between(a, b);
    }

    /**
     * Check the input can be passed to a Double or Float (up to 3 decimal places) and is non null
     *
     * @param input input to check
     * @return true if input is either an integer or a floating point number,
     * false if not
     */
    public static boolean checkOnlyFloatingPoint(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^([0-9]+)$|^(([0-9]+)\\.([0-9]{1,3}))$");
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is a valid password and is non null
     *
     * @param input input to check
     * @return true if password contains upper case, lower case, numbers AND
     * any of the following characters !£$%^&*()-_\/+=@~#
     */
    public static boolean checkPasswordSecurity(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                "(?=.*[#!£$%^&*()\\-_\\\\\\/+=@~]).{8,32}$");
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is a valid email address and is non null
     *
     * @param input input to check
     * @return true if valid email, false if not
     */
    public static boolean checkOnlyEmail(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]" +
                "+\\.[A-Za-z]{2,4}");
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is just numbers and letters and is non null
     *
     * @param input input to check
     * @return true if just numbers and letters, false if not
     */
    public static boolean checkOnlyLettersAndNumbers(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is just whitespace or letters and is non null
     *
     * @param input input to check
     * @return true if just whitespace and letters, false if not
     */
    public static boolean checkOnlyLetters(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^[ a-zA-Z]+$"); // checks for whitespace or letters
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is just whitespace, letters, hyphens or apostrophes and is non null
     *
     * @param input input to check
     * @return true if just whitespace, letters, hyphens or apostrophes, false if not
     */
    public static boolean checkName(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^[ a-zA-Z'-]+$"); // checks for whitespace or letters
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is just numbers and non is null
     *
     * @param input input to check
     * @return true if just a number, false if not
     */
    public static boolean checkOnlyNumbers(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^[\\d]+$");
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Checks the input is between the two ranges passed, is inclusive of the range
     * e.g. an input of 2 between 2 and 4 would return true
     *
     * @param input      input to check
     * @param lowerBound lower bound
     * @param upperBound higher bound
     * @return true if input is between ranges, false if not
     */
    public static boolean checkRange(double input, double lowerBound, double upperBound)
    {
        return !(input < lowerBound) && !(input > upperBound);
    }

    /**
     * Checks the input is between the two ranges passed, is inclusive of the range
     * e.g. an input of 2 between 2 and 4 would return true
     *
     * @param input      input to check
     * @param lowerBound lower bound
     * @param upperBound higher bound
     * @return true if input is between ranges, false if not
     */
    public static boolean checkRange(int input, int lowerBound, int upperBound)
    {
        return input >= lowerBound && input <= upperBound;
    }

    /**
     * Checks the input is below a certain length, is inclusive.
     * e.g. an input of "Hello There!" compared to 12 would return true
     *
     * @param input input to check
     * @param bound maximum characters
     * @return true if the string's character length is less than the bound
     */
    public static boolean checkLength(String input, int bound)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^.{0," + bound + "}$");
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }

    /**
     * Check the input is just whitespace, letters, hyphens or apostrophes and is non null
     *
     * @param input input to check
     * @return true if just whitespace, letters, hyphens or apostrophes, false if not
     */
    public static boolean checkLastName(String input)
    {
        if (input == null)
        {
            return false;
        }

        Pattern regex = Pattern.compile("^[ a-zA-Z'-]+$"); // checks for whitespace or letters
        Matcher matcher = regex.matcher(input);
        boolean result = matcher.matches();

        return result;
    }
}
