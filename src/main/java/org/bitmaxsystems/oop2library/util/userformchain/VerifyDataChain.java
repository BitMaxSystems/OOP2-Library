package org.bitmaxsystems.oop2library.util.userformchain;

import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyDataChain implements IUserFormChain {
    private final int MIN_AGE_ALLOWED = 13;
    private final int MAX_AGE_ALLOWED = 100;
    private final int MIN_POINTS_ALLOWED = 0;
    private final int MAX_POINTS_ALLOWED = 100;
    private IUserFormChain nextChain;
    private List<String> errorList = new ArrayList<>();

    @Override
    public void setNextChain(IUserFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(UserDataDTO formData) throws Exception {
        int age,loyaltyPoints;

        Pattern namePattern = Pattern.compile("[\\W\\d]");
        Pattern phonePattern = Pattern.compile("^(\\+359)\\d{9}$");
        Pattern usernamePattern = Pattern.compile("\\W");
        Pattern passwordMatcher = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\s:])(\\S){8,16}$");
        Matcher matcher = namePattern.matcher(formData.getFirstName().strip());

//        Check if the pattern matches with the input for first and last name - BAD
        if (matcher.find() || formData.getFirstName().isBlank())
        {
            errorList.add("- First name can only be word characters without whitespaces!");
        }

        matcher = namePattern.matcher(formData.getLastName().strip());

        if (matcher.find() || formData.getLastName().isBlank())
        {
            errorList.add("- Last name can only be word characters without whitespaces!");
        }
//          Check if the age is an integer and if it is in the correct range

        try
        {
            age = Integer.parseInt(formData.getAge().strip());

            if (age<MIN_AGE_ALLOWED || age>MAX_AGE_ALLOWED)
            {
                errorList.add("- Age must be between 13 and 100!");
            }
        }
        catch (NumberFormatException e) {
            errorList.add("- Age must be a numeric value!");
        }
//      Check if the phone is a valid Bulgarian phone number. If not - Bad.
        matcher = phonePattern.matcher(formData.getPhoneField().strip());

        if (!matcher.find() || formData.getPhoneField().isBlank())
        {
            errorList.add("- Only Bulgarian phone numbers are allowed (Must include +359 at the start)!");
        }
//      Check if loyalty points is an integer and if it is in the correct range
        if (!formData.getLoyaltyPoints().isBlank())
        {
            try
            {
                loyaltyPoints = Integer.parseInt(formData.getLoyaltyPoints().strip());

                if (loyaltyPoints<MIN_POINTS_ALLOWED || loyaltyPoints>MAX_POINTS_ALLOWED)
                {
                    errorList.add("- Loyalty points must be between 0 and 100!");
                }
            }
            catch (NumberFormatException e) {
                errorList.add("- Loyalty points must be a numeric value!");
            }
        }

//      Check if the username is valid.
        matcher = usernamePattern.matcher(formData.getUsernameField().strip());

        if (matcher.find() || formData.getUsernameField().isBlank())
        {
            errorList.add("- The username cannot include special characters and whitespaces!");
        }

//        Check if the password is valid and if it is the same as the repeat password
        if (formData.isNewPassword())
        {
            matcher = passwordMatcher.matcher(formData.getPasswordField().strip());

            if (!matcher.find() || formData.getPasswordField().isBlank())
            {
                errorList.add("- The password does not match all the rules!");
            }
            else
            {
                if (!formData.getPasswordField().strip().equals(formData.getRepeatPasswordField().strip()))
                {
                    errorList.add("- The passwords are not the same!");
                }
            }
        }

        if (errorList.isEmpty())
        {
            if (nextChain != null)
            {
                nextChain.execute(formData);
            }
        }
        else
        {
            StringBuilder sb = new StringBuilder();

            for (String error: errorList)
            {
                sb.append(error).append("\n");
            }

            throw new DataValidationException(sb.toString());
        }

    }
}
