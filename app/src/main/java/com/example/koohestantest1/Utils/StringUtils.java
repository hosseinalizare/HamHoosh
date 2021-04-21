package com.example.koohestantest1.Utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringUtils {

    private static String TAG = StringUtils.class.getSimpleName();

    @Deprecated
    /*
        converts numbers like 13,543 to 13543
        @deprecated
     */
    public static float getNumberFromString(String mixedNumber) {
        Log.d(TAG, "getNumberFromString: " + mixedNumber + " " + mixedNumber.length());
        for (int i = 0; i < mixedNumber.length(); i++) {
            Log.d(TAG, "getNumberFromString: before if");
            char currentChar = mixedNumber.charAt(i);
            if (!Character.isDigit(currentChar)) {
                Log.d(TAG, "getNumberFromString: before removing");
                String cleanedFormat = mixedNumber.replaceAll(currentChar + "", "");
                Log.d(TAG, "getNumberFromString: before returning");
                Log.d(TAG, "getNumberFromString: returned number: " + Integer.parseInt(cleanedFormat) + " String: " + cleanedFormat);
                return Float.parseFloat(cleanedFormat);
            }
        }

        return Integer.parseInt(mixedNumber);
    }

    /*
        Converts Non-digit string numbers to float
     */
    public static float getNumberFromStringV2(String mixedNumber) {
        Log.d(TAG, "getNumberFromStringV2: before digits");
        String englishNum = persianToEnglish(mixedNumber);
        Log.d(TAG, "getNumberFromStringV2: " + englishNum);
        String digits = englishNum.replaceAll("[^0-9.]", "");
        Log.d(TAG, "getNumberFromStringV2: " + digits);
        float number = Float.parseFloat(digits);
        Log.d(TAG, "getNumberFromStringV2: float number: " + number + " String: " + digits);
        return number;
    }

    // gets 12,345(or all kind of non-digits) and turns it to 12345 / string-string
    public static String getNumberFromStringV3(String mixedNumber) {
        Log.d(TAG, "getNumberFromStringV2: before digits");
        String englishNum = persianToEnglish(mixedNumber);
        Log.d(TAG, "getNumberFromStringV2: " + englishNum);
        String digits = englishNum.replaceAll("[^0-9.]", "");
        Log.d(TAG, "getNumberFromStringV2: " + digits);
        return digits;
    }

    public static String getTrimedText(String word){
        return word.replace(",","");
    }

    private static String persianToEnglish(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


    /*
     remove dot from string
     */
    public static String getNumberWithoutDot(int price) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String nPrice = String.valueOf(price);
        if (!nPrice.isEmpty()) {
            nPrice = formatter.format(price);
        }
        return nPrice;
    }

    public static String getNumberWithoutDot(String price) {
        if (!price.isEmpty()) {
            return getNumberWithoutDot((int) getNumberFromStringV2(price));
        }
        return null;
    }

    public static String showProductViewCount(int count){
        String viewCount;
        if (count>=1000){
            double newCount = count/1000f;
            viewCount =newCount+"k";
        }else {
            viewCount =count+"";
        }
        return viewCount;

    }
}
