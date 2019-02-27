package com.example.nada.devhires.utils;

import android.content.Context;
import android.widget.EditText;

import com.example.nada.devhires.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    // TODO Dialogs

    public static boolean isEmptyEditTexts(Context context, EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.requestFocus();
                editText.setError("Mandatory Field", context.getDrawable(R.drawable.ic_error));
                return true;
            }
        }
        return false;
    }

    public static boolean isMatched(EditText editText1, EditText editText2) {
        if (!editText2.getText().toString().equals(editText1.getText().toString())) {
            editText2.requestFocus();
            editText2.setError("Password doesn't match");
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(EditText password) {
        String passwordPattern = "((?=.*[A-Za-z]).(?=.*[A-Za-z0-9@#$%_])(?=\\S+$).{3,})";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password.getText().toString());
        if (matcher.matches()) {
            return true;
        }
        password.requestFocus();
        password.setError("password must contain at least 3 digits, one character or more ");
        return false;
    }

    public static boolean isValidEmail(EditText email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email.getText().toString().trim();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (inputStr.length() >= 6 && matcher.matches()) {
            return true;
        }
        email.requestFocus();
        email.setError("Invalid Email");
        return false;
    }
}
