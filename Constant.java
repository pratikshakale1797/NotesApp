package com.example.noteapp.Utils;

import android.text.TextUtils;
import android.util.Patterns;

public class Constant {

    public static boolean isEmptyOrNull(String text){
        return text == null || text.isEmpty() || text.trim().length() <= 0;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
