package com.rex.cheetah.common.util;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

public class StringUtil {
    public static String firstLetterToUpper(String value) {
        Character character = Character.toUpperCase(value.charAt(0));
        
        return character.toString().concat(value.substring(1));
    }
    
    public static String firstLetterToLower(String value) {
        Character character = Character.toLowerCase(value.charAt(0));
        
        return character.toString().concat(value.substring(1));
    }
    
    /*public static String firstLetterToUpper(String value){
        char[] array = value.toCharArray();
        array[0] -= 32;
        
        return String.valueOf(array);
    }
    
    public static String firstLetterToLower(String value){
        char[] array = value.toCharArray();
        array[0] += 32;
        
        return String.valueOf(array);
    }*/
}