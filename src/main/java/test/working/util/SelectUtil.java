package test.working.util;

import test.working.classes.Rule;

import java.util.ArrayList;
import java.util.Map;

public class SelectUtil {
    private static boolean flag = true;

    public static boolean isLL1(Map<Character, ArrayList<String>> map){
        boolean isValid;
        for(Map.Entry<Character, ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            Rule rule = new Rule(map,key);
            isValid = rule.compareSelectSet(key,map);
//            System.out.println(key+": "+isValid);

            if(isValid == false){
                flag = false;
            }
        }
        return flag;
    }
}
