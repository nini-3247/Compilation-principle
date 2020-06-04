package com.working.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Rule {
    private Character key;
    private ArrayList<String> values;
    private boolean isNull;

    private ArrayList<Character> firstSet;
    private ArrayList<Character> followSet;
    private ArrayList<Character> selectSet;
    private ArrayList<Character[]> selectSets;
    private Map<SingleRule,ArrayList<Character>> selectMap;

    public ArrayList<Character> getFirstSet() {
        return firstSet;
    }

    public ArrayList<Character> getFollowSet() {
        return followSet;
    }

    public ArrayList<Character> getSelectSet() {
        return selectSet;
    }

    public ArrayList<Character[]> getSelectSets() {
        return selectSets;
    }


    public Rule(Map<Character,ArrayList<String>> map, Character key){
        this.key = key;
        this.values = new ArrayList<>();
        this.isNull = false;

        firstSet = new ArrayList<>();
        followSet = new ArrayList<>();
        selectSet = new ArrayList<>();
        selectSets = new ArrayList<>();
        selectMap = new HashMap<>();
    }

    /**
     * 寻找规则左边非终结符号key对应的first集
     * @param key 当前非终结符号
     * @param map 规则映射
     * @return first集
     */
    public ArrayList<Character> findFirstSet(Character key,Map<Character,ArrayList<String>> map){
        boolean isAllNull = false;
        // 找到当前key对应的first集
        for(String value:map.get(key)){
            char fstChar = value.charAt(0);
            // 当前存在推导出空集，标志出来
            if(fstChar == '#'){
                isNull = true;
                continue;
            }
            // 推导出的第一个元素是非终结符号，则加入firstSet
            else if(Character.isUpperCase(fstChar) == false){
                this.firstSet.add(fstChar);
            }
            // 是终结符号，递归计算下一个字符
            else{
                key = value.charAt(0);
                firstSet = findFirstSet(key,map);

                if(isNull){
                    isAllNull = true;
                }
            }
            if(isAllNull || this.isNull){
                if(this.firstSet.contains('#') == false){
                    this.firstSet.add('#');
                }
            }
        }
        return this.firstSet;
    }

    /**
     * 寻找规则左边非终结符号key对应的follow集
     * @param key 当前非终结符号
     * @param map 规则映射
     * @return follow集
     */
    public ArrayList<Character> findFollowSet(Character key,Map<Character,ArrayList<String>> map){
        // 若为文法开始符号，则先加入$
        if(key == 'S'){
            if(this.followSet.contains('$') == false){
                this.followSet.add('$');
            }
        }
        // 规则右部含有该非终结符号的子映射
        Map<Character,ArrayList<String>> subMap = new HashMap<>();
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            for(String nowValue:entry.getValue()){
                if(nowValue.contains(String.valueOf(key))){
                    if(subMap.containsKey(entry.getKey()) == false){
                        ArrayList<String> validValues= new ArrayList<>();
                        validValues.add(nowValue);
                        subMap.put(entry.getKey(),validValues);
                    }
                    else{
                        subMap.get(entry.getKey()).add(nowValue);
                    }
                }
            }
        }
        // 在子映射中找到该非终结符号的后一个元素
        for(Map.Entry<Character,ArrayList<String>> entry:subMap.entrySet()){
            Character nowKey = entry.getKey();
            ArrayList<String> nowValues = new ArrayList<>();
            for(String s:entry.getValue()){
                nowValues.add(s);
            }
            for(String nowValue:nowValues){
                // 该非终结符号所在的字符串中的位置
                int ind = nowValue.indexOf(key);
                // 若该非终结符号是最后一个符号
                if(ind == nowValue.length()-1){
                    if(nowKey.equals(key)){
                        continue;
                    }
                    followSet = findFollowSet(nowKey,map);
                }
                // 该非终结符号后面是终结符号，直接加入follow集
                else if(Character.isUpperCase(nowValue.charAt(ind+1)) == false){
                    if(this.followSet.contains(nowValue.charAt(ind+1)) == false)
                        this.followSet.add(nowValue.charAt(ind+1));
                }
                // 该非终结符号后面仍然是非终结符号，递归计算follow集
                else{
                    Character nextChar = nowValue.charAt(ind+1);
                    ArrayList<Character> firstValues = findFirstSet(nextChar,map);
                    for(Character ch:firstValues){
                        if(this.followSet.contains(ch) == false && ch.equals('#') == false)
                            this.followSet.add(ch);
                    }
                    if(firstValues.contains('#')){
                        ArrayList<Character> followValues = findFollowSet(nowKey,map);
                        for(Character ch:followValues){
                            if(this.followSet.contains(ch) == false)
                                this.followSet.add(ch);
                        }
                    }
                }
            }
        }

        return this.followSet;
    }

    /**
     * 寻找规则左边非终结符号key对应的select集
     * @param key 该非终结符号key
     * @param value 一个key对应的的单条规则右部的字符串
     * @param map 规则映射
     * @return 单条规则的select集
     */
    public ArrayList<Character> findSelectSet(Character key,String value,Map<Character,ArrayList<String>> map){
        char fstChar = value.charAt(0);
        // 存在推导出空集，则寻找该key对应的follow集
        if(fstChar == '#'){
            selectSet = findFollowSet(key,map);
        }
        // 推导出的第一个元素是非终结符号，加入select集
        else if(Character.isUpperCase(fstChar) == false){
            ArrayList<Character> tmpChar = new ArrayList<>();
            tmpChar.add(fstChar);
            selectSet = tmpChar;
        }
        // 推导出的第一个元素是非终结符号，找到对应的first集
        else{
            selectSet = findFirstSet(fstChar,map);
        }

        return this.selectSet;
    }

    /**
     * 找到该key对应的所有规则的select集
     * @param key 该非终结符号key
     * @param map 规则映射
     * @return key的所有select集
     */
    public ArrayList<Character[]> findSelectSets(Character key,Map<Character,ArrayList<String>> map){
        ArrayList<String> values = map.get(key);
        for(String value:values){
            ArrayList<Character> fstSet = findSelectSet(key,value,map);
            Character[] charList = fstSet.toArray(new Character[fstSet.size()]);
            this.selectSets.add(charList);
        }

        return this.selectSets;
    }

    /**
     * 将该key对应的所有select集转换成map映射形式
     * @param key 该非终结符号key
     * @param map 规则映射
     * @return select集的map映射
     */
    public Map<SingleRule,ArrayList<Character>> findSelectMap(Character key,Map<Character,ArrayList<String>> map){
        ArrayList<String> values = map.get(key);
        SingleRule srule;

        for(String value:values){
            ArrayList<Character> fstSet = findSelectSet(key,value,map);
            srule = new SingleRule(key,value);
            selectMap.put(srule,fstSet);
        }

        return this.selectMap;
    }

    public boolean compare(ArrayList<Character> charArray){
        int times = 0;
        for(Character[] chs:selectSets){
            ArrayList<Character> nowArray = new ArrayList<>(Arrays.asList(chs));
            // 去除与该规则本身的比较
            if(nowArray.equals(charArray)){
                times++;
                continue;
            }
            if(times > 1)
                return false;
            // 判断是否含有交集
            // retainAll(String)保存String中与当前字符串相同的部分
            nowArray.retainAll(charArray);
            // 有交集则返回false
            if(nowArray.size() != 0)
                return false;
        }
        return true;
    }

    /**
     * 判断key对应的select集是否符合要求（交集为空）
     * @param key 该非终结符号key
     * @param map 规则映射
     * @return 是否符合要求（true/false）
     */
    public boolean compareSelectSet(Character key,Map<Character,ArrayList<String>> map){
        if(selectSets.size() == 0){
            selectSets = findSelectSets(key,map);
        }
        for(Character[] chs:selectSets){
            ArrayList<Character> charArray = new ArrayList<>(Arrays.asList(chs));
            if(compare(charArray) == false){
                return false;
            }
        }
        return true;
    }

}
