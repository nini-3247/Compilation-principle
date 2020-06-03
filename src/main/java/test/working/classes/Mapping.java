package test.working.classes;

import test.working.classes.*;
import test.working.classes.Error;
import test.working.util.CharArrayUtil;
import test.working.util.RecursionRmvUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 存放语法分析的映射
 * map: 规则映射：以一个非终结符号key（规则左部）为单位，存放key和key对应的value(所有规则右部的字符串)
 * tableMap: 存放预测分析表的映射（规则所在的表格中的位置，单条规则）
 * table: 存放空的预测分析表
 */
public class Mapping {
    private Map<Character, ArrayList<String>> map;
    private Map<Integer[],SingleRule> tableMap;
    private Character[][] table;

    public Map<Character, ArrayList<String>> getMap() {
        return map;
    }

    public void setMap(Map<Character, ArrayList<String>> map) {
        this.map = map;
    }

    public Map<Integer[], SingleRule> getTableMap() {
        return tableMap;
    }

    public void setTableMap(Map<Integer[], SingleRule> tableMap) {
        this.tableMap = tableMap;
    }

    public Character[][] getTable() {
        return table;
    }

    public void setTable(Character[][] table) {
        this.table = table;
    }

    public Mapping(){
        map = new HashMap<>();
        tableMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "map=" + map +
                '}';
    }

    /**
     * 从文件中读取内容，并存入map映射
     * @param map 空映射map
     * @return map 从文件中读取内容后，形成的规则映射map
     */
    public Map<Character, ArrayList<String>> mapInit(String filePath,Map<Character, ArrayList<String>> map){

        InputStreamReader inReader = null;
        BufferedReader bfReader = null;
        StringBuilder strBuilder = null;

        // 从指定路径读取规则
        try{
            inReader = new InputStreamReader(new FileInputStream(filePath),"utf-8");
            bfReader = new BufferedReader(inReader);
            strBuilder = new StringBuilder();
            String str = null;
            boolean isLeftRecur = false;
            ArrayList<Character> leftKeys = new ArrayList<>();

            /* 按行读取字符串 */
            while((str = bfReader.readLine()) != null){
                strBuilder.append(str+"\n");

                 /* 将每一条规则存入map映射 */

                // 去除所有空格等
                str.replaceAll("\\s","");
                int ind = str.indexOf("->");

                // 推导符号左边不是一个非终结符号，则不是2型文法，也不是LL(1)文法
                if(ind != 1 || (Character.isUpperCase(str.charAt(0)) == false) ){
                    new Error(Error.Type.LeftError);
                    System.exit(0);
                }

                char key = str.charAt(0);
                ArrayList<String> array = new ArrayList<>();

                // 如果有一行多条规则的情况
                if(str.contains("|")){
                    String tmpStr = str;
                    tmpStr = tmpStr.substring(3);
                    String[] strs = tmpStr.split("\\|");
                    // 本来有某非终结符的映射了，则把原来的也加进来
                    if(map.containsKey(key) == true){
                        // 实现深拷贝
                        for(String s:map.get(key)){
                            array.add(s);
                        }
                    }
                    // 添加规则右部字符串数组中所有字符串
                    for(String s:strs){
                        if(s.charAt(0) == key){
                            isLeftRecur = true;
                            if(leftKeys.contains(key) == false)
                                leftKeys.add(key);
                        }
                        array.add(s);
                    }
                }else{
                    // 本来有某非终结符的映射了，则把原来的也加进来
                    if(map.containsKey(key) == true) {
                        // 实现深拷贝
                        for (String s : map.get(key)) {
                            array.add(s);
                        }
                    }
                    if(str.charAt(3) == key){
                        isLeftRecur = true;
                        if(leftKeys.contains(key) == false)
                            leftKeys.add(key);
                    }
                    // 添加规则右部单个字符串
                    array.add(str.substring(3));
                }

                map.put(key, array);
            }
            // 消除直接左递归
            if(isLeftRecur){
                for(Character leftKey:leftKeys){
//                    System.out.print(leftKey+"\t");
                    map = new RecursionRmvUtil().removeDctRecur(leftKey,map);
                }
            }
        }catch(IOException ioe){
            new Error(Error.Type.FileError);
            System.exit(0);
        }finally {
            try{
                inReader.close();
                bfReader.close();
            }catch (IOException ioe){
                new Error(Error.Type.FileError);
                System.exit(0);
            }
        }

        /*System.out.println("The content of file:");
        System.out.println(strBuilder);
        System.out.println("——————————");*/

        return map;
    }

    /**
     * 求所有非终结符号的first集
     * @param map 规则映射map
     * @return  first集映射
     */
    public Map<Character,ArrayList<Character>> findAllFirstSet(Map<Character,ArrayList<String>> map){
        Map<Character,ArrayList<Character>> firstSets = new HashMap<>();
        // 扫描映射中的每一个数据项<key,value>
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            Rule rule = new Rule(map,key);
            // 调用Rule类的方法获得该key对应的first集
            ArrayList<Character> firstSet = rule.findFirstSet(key,map);
            firstSets.put(key,firstSet);
        }
        return firstSets;
    }

    /**
     * 求所有非终结符号的follow集
     * @param map 规则映射map
     * @return  follow集映射
     */
    public Map<Character,ArrayList<Character>> findAllFollowtSet(Map<Character,ArrayList<String>> map){
        Map<Character,ArrayList<Character>> followSets = new HashMap<>();
        // 扫描映射中的每一个数据项<key,value>
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            Rule rule = new Rule(map,key);
            // 调用Rule类的方法获得该key对应的follow集
            ArrayList<Character> followSet = rule.findFollowSet(key,map);
            followSets.put(key,followSet);
        }
        return followSets;
    }

    /**
     * 求所有非终结符号的select集
     * @param map 规则映射map
     * @return  select集映射
     */
    public Map<Character,ArrayList<Character[]>> findAllSelectSets(Map<Character,ArrayList<String>> map){
        Map<Character,ArrayList<Character[]>> selectSets = new HashMap<>();
        // 扫描映射中的每一个数据项<key,value>
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            Rule rule = new Rule(map,key);
            // 调用Rule类的方法获得该key对应的select集
            ArrayList<Character[]> selectSet = rule.findSelectSets(key,map);
            selectSets.put(key,selectSet);
        }
        return selectSets;
    }

    /**
     * 初始化空的预测分析表
     * @param map 规则映射
     * @return table 空的预测分析表
     */
    public Character[][] tableInit(Map<Character,ArrayList<String>> map){
        ArrayList<Character> left = new ArrayList<>();
        ArrayList<Character> right = new ArrayList<>();
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            ArrayList<String> values = entry.getValue();
            // 第一列，所有的非终结符号
            if(left.contains(key) == false){
                left.add(key);
            }
            // 第一行，所有的非终结符号和$
            for(String value:values){
                char[] arrChar = value.toCharArray();
                for(Character ch:arrChar){
                    if(right.contains(ch) == false &&
                       Character.isUpperCase(ch) == false &&
                       ch.equals('#') == false){
                        right.add(ch);
                    }
                }
            }
        }
        right.add('$');
        // 放入table数组
        table = new Character[left.size()+1][right.size()+1];
        for(int i = 0;i < left.size();i++){
            table[i+1][0] = left.get(i);
        }
        for(int i = 0; i < right.size();i++){
            table[0][i+1] = right.get(i);
        }
        return table;
    }

    /**
     * 形成预测分析表的映射
     * @param map 规则映射
     * @param table 空的预测分析表
     * @return tableMap 预测分析表的映射
     */
    public Map<Integer[],SingleRule> analyzeTableMap
            (Map<Character,ArrayList<String>> map,Character[][] table){
        tableMap = new HashMap<>();
        ArrayList<Character> keys = new ArrayList<>();
        for(Map.Entry<Character,ArrayList<String>> entry:map.entrySet()){
            Character key = entry.getKey();
            keys.add(key);
        }
        Character[] charKeys = keys.toArray(new Character[keys.size()]);

        int row = 1,col;
        for(Character key:charKeys){
            // 获得所有select集合的映射
            Map<SingleRule,ArrayList<Character>> selectMap =
                    new Rule(map,key).findSelectMap(key,map);
            for(Map.Entry<SingleRule,ArrayList<Character>> entry:selectMap.entrySet()){
                SingleRule srule = entry.getKey();
                ArrayList<Character> selectSet = entry.getValue();
                // 得到select集合中元素在table中的位置
                for(Character ch:selectSet){
                    if(ch.equals('$')){
                        col = table[0].length-1;
                    }
                    else{
                        col = new CharArrayUtil(table[0]).getIndex(ch);
                    }
                    Integer[] ptr = {row,col};
                    tableMap.put(ptr,srule);
                }
            }
            row++;
        }

        return tableMap;
    }


}
