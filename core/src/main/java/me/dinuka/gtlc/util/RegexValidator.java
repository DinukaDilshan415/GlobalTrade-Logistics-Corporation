package me.dinuka.gtlc.util;

public class RegexValidator {
    /**
     * Validates an email address using regex.
     * 
     * @param email Email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    /**
     * Validates a phone number using regex.
     * 
     * @param phone Phone number to validate
     * @return true if the phone number is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The phone number can start with +88 or 88 <br>
     * - The phone number must start with 01, followed by 9 digits <br>
     * - The phone number must not contain any other characters <br>
     */
    public static boolean isValidPhone(String phone) {
        return phone.matches("^(?:\\+88|88)?(01[3-9]\\d{8})$");
    }

    /**
     * Validates a Sri Lankan phone number using regex.
     * 
     * @param phone Phone number to validate
     * @return true if the phone number is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The phone number must start with 07, followed by 9 digits <br>
     * - The phone number must not contain any other characters <br>
     */
    public static boolean isValidSlPhone(String phone) {
        return phone.matches("^07[012345678]{1}[0-9]{7}$");
    }

    /**
     * Validates a name using regex.
     * 
     * @param name Name to validate
     * @return true if the name is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The name can only contain letters and spaces <br>
     */
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Validates an address using regex.
     * 
     * @param address Address to validate
     * @return true if the address is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The address can only contain letters, numbers, commas, and spaces <br>
     */
    public static boolean isValidAddress(String address) {
        return address.matches("^[a-zA-Z0-9,\\s]+$");
    }

    /**
     * Validates a date of birth using regex.
     * 
     * @param dob Date of birth to validate
     * @return true if the date of birth is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - Year must be between 1900 and 2099 <br>
     * - Month must be between 1 and 12 <br>
     * - Day must be between 1 and 31 <br>
     * - The date must be separated by either a - or / or . <br>
     */
    public static boolean isValidDob(String dob) {
        return dob.matches("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$");
    }

    /**
     * Validates a gender using regex.
     * 
     * @param gender Gender to validate
     * @return true if the gender is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The gender must be either "male" or "female" <br>
     */
    public static boolean isValidGender(String gender) {
        return gender.matches("^(male|female)$");
    }

    /**
     * Validates a password using regex.
     * 
     * @param password Password to validate
     * @return true if the password is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The password must contain at least 8 characters <br>
     * - The password must contain at least one lowercase letter <br>
     * - The password must contain at least one uppercase letter <br>
     * - The password must contain at least one digit <br>
     * - The password must contain at least one special character in the following set: @ $ ! % * ? & <br>
     */
    public static boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    /**
     * Validates an id using regex.
     * 
     * @param id Id to validate
     * @return true if the id is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The id can only contain numbers <br>
     */
    public static boolean isValidId(String id) {
        return id.matches("^[0-9]+$");
    }

    /**
     * Validates a number using regex.
     * 
     * @param number Number to validate
     * @return true if the number is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The number can only contain numbers <br>
     */
    public static boolean isValidNumber(String number) {
        return number.matches("^[0-9]+$");
    }

    /**
     * Validates a date using regex.
     * 
     * @param date Date to validate
     * @return true if the date is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The date must be in the form of "YYYY-MM-DD" or "YYYY/MM/DD" or "YYYY.MM.DD" <br>
     * - The year must be between 1900 and 2099 <br>
     * - The month must be between 1 and 12 <br>
     * - The day must be between 1 and 31 <br>
     */
    public static boolean isValidDate(String date) {
        return date.matches("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$");
    }

    /**
     * Validates a time using regex.
     * 
     * @param time Time to validate
     * @return true if the time is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The time must be in the form of "HH:MM" <br>
     * - The hour must be between 0 and 23 <br>
     * - The minute must be between 0 and 59 <br>
     */
    public static boolean isValidTime(String time) {
        return time.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
    }

    /**
     * Validates a URL using regex.
     * 
     * @param url URL to validate
     * @return true if the URL is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The URL must start with http://, https://, ftp://, or file:// <br>
     * - The URL must contain letters, numbers, +, &, @, #, /, % , ?, =, ~, _, !, :, ;, or . <br>
     * - The URL must not end with a / <br>
     */
    public static boolean isValidUrl(String url) {
        return url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$");
    }

    /**
     * Validates a code using regex.
     * 
     * @param code Code to validate
     * @return true if the code is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The code must contain only letters and numbers <br>
     */
    public static boolean isValidCode(String code) {
        return code.matches("^[0-9a-zA-Z]+$");
    }

    /**
     * Validates an amount using regex.
     * 
     * @param amount Amount to validate
     * @return true if the amount is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The amount must start with one or more digits <br>
     * - The amount must not contain any letters <br>
     * - The amount must not contain more than two decimal places <br>
     */
    public static boolean isValidAmount(String amount) {
        return amount.matches("^[0-9]+(\\.[0-9]{1,2})?$");
    }

    /**
     * Validates a percentage using regex.
     * 
     * @param percentage Percentage to validate
     * @return true if the percentage is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The percentage must start with one or more digits <br>
     * - The percentage must not contain any letters <br>
     * - The percentage must not contain more than two decimal places <br>
     */
    public static boolean isValidPercentage(String percentage) {
        return percentage.matches("^[0-9]+(\\.[0-9]{1,2})?$");
    }

    /**
     * Validates a text using regex.
     * 
     * @param text Text to validate
     * @return true if the text is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The text must only contain letters and spaces <br>
     */
    public static boolean isValidText(String text) {
        return text.matches("^[a-zA-Z\\s]+$");
    }  

    /**
     * Validates a decimal number using regex.
     * 
     * @param decimal Decimal to validate
     * @return true if the decimal is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The decimal must start with one or more digits <br>
     * - The decimal must not contain any letters <br>
     * - The decimal must not contain more than two decimal places <br>
     */
    public static boolean isValidDecimal(String decimal) {
        return decimal.matches("^[0-9]+(\\.[0-9]{1,2})?$");
    }

    /**
     * Validates an integer using regex.
     * 
     * @param integer Integer to validate
     * @return true if the integer is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The integer must only contain digits <br>
     */
    public static boolean isValidInteger(String integer) {
        return integer.matches("^[0-9]+$");
    }

    /**
     * Validates a zip code using regex.
     * 
     * @param zipCode Zip code to validate
     * @return true if the zip code is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The zip code must be 5 digits <br>
     * - The zip code must not contain any letters <br>
     * - The zip code can optionally have a hyphen and 4 more digits <br>
     */
    public static boolean isValidZipCode(String zipCode) {
        return zipCode.matches("^[0-9]{5}(?:-[0-9]{4})?$");
    }

    /**
     * Validates a social security number (SSN) using regex.
     * 
     * @param ssn SSN to validate
     * @return true if the SSN is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The SSN must be 9 digits <br>
     * - The SSN must not have all zeros in the first 3 or last 4 digits <br>
     * - The SSN must not have 666 or 000 in the first 3 digits <br>
     * - The SSN must not have 00 in the middle 2 digits <br>
     * - The SSN must not have 0 in all 4 of the last 4 digits <br>
     * - The SSN must not have 9 in the first 3 digits <br>
     */
    public static boolean isValidSSN(String ssn) {
        return ssn.matches("^(?!666|000|9\\d{2})\\d{3}(?!00)\\d{2}(?!0{4})\\d{4}$");
    }

    /**
     * Validates a credit card number using regex.
     * 
     * @param creditCard Credit card number to validate
     * @return true if the credit card number is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The credit card number must be 13, 14, 15, or 16 digits <br>
     * - The credit card number must start with 4, 51, 52, 53, 54, or 55 <br>
     * - The credit card number must not contain any letters <br>
     * - The credit card number must not contain any spaces <br>
     * - The credit card number must not contain any special characters <br>
     * - The credit card number must not have all zeros <br>
     */
    public static boolean isValidCreditCard(String creditCard) {    
        return creditCard.matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$");
    }

    /**
     * Validates an International Bank Account Number (IBAN) using regex.
     * 
     * @param iban IBAN to validate
     * @return true if the IBAN is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The IBAN must start with 2 uppercase letters <br>
     * - The IBAN must contain 2 digits <br>
     * - The IBAN must contain 4 alphanumeric characters <br>
     * - The IBAN must contain 7 digits <br>
     * - The IBAN can optionally contain up to 16 alphanumeric characters <br>
     */
    public static boolean isValidIBAN(String iban) {
        return iban.matches("^[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$");
    }

    /**
     * Validates a Value Added Tax (VAT) number using regex.
     * 
     * @param vat VAT number to validate
     * @return true if the VAT number is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The VAT number must start with 2 uppercase letters <br>
     * - The VAT number must contain 9 digits <br>
     */
    public static boolean isValidVAT(String vat) {
        return vat.matches("^[A-Z]{2}[0-9]{9}$");
    }

    /**
     * Validates an International Standard Book Number (ISBN) using regex.
     * 
     * @param isbn ISBN to validate
     * @return true if the ISBN is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The ISBN must start with "ISBN" (case insensitive) followed by a hyphen and the number 1, 0, or 3, or just the number 1, 0, or 3 <br>
     * - The ISBN must contain 10 digits, each separated by a hyphen, underscore, or nothing <br>
     * - The ISBN must end with a check digit <br>
     * - The ISBN must not contain any letters <br>
     * - The ISBN must not contain any spaces <br>
     * - The ISBN must not contain any special characters <br>
     * - The ISBN must not have all zeros <br>
     */
    public static boolean isValidISBN(String isbn) {
        return isbn.matches("^(?:ISBN(?:-1[03])?:? )?(?=(?:\\d[-●_]){10}\\d[-●_]?){13}\\d$");
    }

    /**
     * Validates a Sri Lankan National Identity Card (NIC) using regex.
     * 
     * @param nic NIC to validate
     * @return true if the NIC is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The NIC must contain 9 digits <br>
     * - The NIC must end with a letter in the set {V, X, v, x} <br>
     */
    public static boolean isValidSlNic(String nic) {
        return nic.matches("^[0-9]{9}[vVxX]$");
    }

    /**
     * Validates a new Sri Lankan National Identity Card (NIC) using regex.
     * 
     * @param nic NIC to validate
     * @return true if the NIC is valid, false otherwise
     * <p>
     * The regex pattern is as follows: <br>
     * - The NIC must contain 12 digits <br>
     */
    public static boolean isValidSlNewNic(String nic) {
        return nic.matches("^[0-9]{12}$");
    }
}
