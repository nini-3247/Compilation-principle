package com.working.util;

import com.working.classes.Error;

import java.util.ArrayList;
import java.util.Map;

public class RecursionRmvUtil {

    public static Map<Character, ArrayList<String>> removeRecur(Map<Character, ArrayList<String>> map){
        Map<Character, ArrayList<String>> tmpMap = map;
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            ArrayList<String> values = entry.getValue();
            for(String value:values){
                Character fstChar = value.charAt(0);
                if(value.equals(key) || value.equals("#")){
                    new Error(Error.Type.LoopError);
                    System.exit(0);
                }
                if(Character.isUpperCase(fstChar) == false)
                    continue;
                else if(fstChar.equals(key))
                    tmpMap = removeDctRecur(fstChar,map);
                else{
                    int ptr = 0;
                    ArrayList<Character> fstChars = new ArrayList<>();
                    while(Character.isUpperCase(fstChar) && fstChar.equals(key) == false){
                        ArrayList<String> valuesIn = map.get(fstChar);
                        ArrayList<String> newValues = new ArrayList<>();

                        for(String valueIn:valuesIn){
                            String newValue = value.replace(fstChar.toString(),valueIn);
                            newValues.add(newValue);
                            fstChars.add(valueIn.charAt(0));
                        }
                        fstChar = fstChars.get(ptr);
                        ptr++;
                    }
                    if(fstChar.equals(key)){
                        tmpMap = removeDctRecur(fstChar,map);
                    }
                }
            }
        }

        return map;
    }

    public static Map<Character, ArrayList<String>> removeDctRecur
            (Character key,Map<Character, ArrayList<String>> map){
        Map<Character, ArrayList<String>> tmpMap = map;
        // 若出现形如A->A或A->#则出现环路，出错
        for(String value:map.get(key)){
            if(value.equals(key) || value.equals("#")){
                new Error(Error.Type.LoopError);
                System.exit(0);
            }
        }
        // 在字母表中找到未使用过的大写字母，作为新的语法规则左部
        ArrayList<Character> letters = new ArrayList<>();
        for(char ch = 90;ch >= 65;ch--){
            letters.add(ch);
        }
        Character newKey = null;
        for(Character ch:letters){
            if(map.containsKey(ch) == false){
                newKey = ch;
                break;
            }
        }
        // 用A->βZ替换A->Aα|β
        ArrayList<String> values = map.get(key);
        ArrayList<String> newValues = new ArrayList<>();
        ArrayList<String> newKeyValues = new ArrayList<>();
        for(String value:values){
            Character fstChar = value.charAt(0);
            if(fstChar.equals(key)){
                newKeyValues.add(value);
                continue;
            }
            String newValue = value+newKey;
            newValues.add(newValue);
        }
        tmpMap.put(key,newValues);
        // 新增Z->αZ|#
        for(int i = 0;i < newKeyValues.size();i++){
            String newKeyValue = newKeyValues.get(i);
            newKeyValues.set(i,newKeyValue.substring(1)+newKey);
        }
        newKeyValues.add("#");
        tmpMap.put(newKey,newKeyValues);

        return tmpMap;
    }

}
