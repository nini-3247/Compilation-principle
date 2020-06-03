package test.working.util;

import java.util.ArrayList;
import java.util.Map;

public class SetToStringUtil {
    public static String toString(Map<Character, ArrayList<Character>> set){
        StringBuilder result = new StringBuilder();
        for(Map.Entry<Character, ArrayList<Character>> entry:set.entrySet()){
            Character key = entry.getKey();
            ArrayList<Character> values = entry.getValue();
            result.append(key+" = "+values+"\n");
        }
        return result.toString();
    }
}
