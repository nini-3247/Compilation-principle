package com.working.methods;

import com.working.classes.SingleRule;
import com.working.util.SelectUtil;
import com.working.classes.Mapping;
import com.working.util.ForecastUtil;

import java.util.ArrayList;
import java.util.Map;

public class MappingTest {

    public static void main(String[] args) {
        String filePath = "src/main/resources/txt/grammar.txt";

        Mapping mapTest = new Mapping();
        Map<Character, ArrayList<String>> map = mapTest.mapInit(filePath,mapTest.getMap());
        mapTest.setMap(map);
        System.out.println("Map: ");
        System.out.println(mapTest.getMap());

        Map<Character, ArrayList<Character>> firstSet;
        firstSet = mapTest.findAllFirstSet(mapTest.getMap());
        System.out.println("FirstSet：");
        System.out.println(firstSet);

        Map<Character,ArrayList<Character>> followSet;
        followSet = mapTest.findAllFollowtSet(mapTest.getMap());
        System.out.println("FollowSet: ");
        System.out.println(followSet);

        Map<Character,ArrayList<Character[]>> selectSets;
        selectSets = mapTest.findAllSelectSets(mapTest.getMap());
        System.out.println("SelectSet: ");
        for(Map.Entry<Character,ArrayList<Character[]>> entry:selectSets.entrySet()){
            Character key = entry.getKey();
            ArrayList<Character[]> values = entry.getValue();

            System.out.print(key+" = [");
            for(Character[] chs:values){
                System.out.print("[");
                for(Character ch:chs){
                    if(ch.equals(chs[chs.length-1]) == false)
                        System.out.print(ch+",");
                    else
                        System.out.print(ch);
                }
                if(chs.equals(values.get(values.size()-1)) == false)
                    System.out.print("],");
                else
                    System.out.print("]");
            }
            System.out.print("]\n");
        }

        System.out.println("\nJudge selectSet... ");
        boolean flag = SelectUtil.isLL1(mapTest.getMap());
        if(flag)
            System.out.println("The grammar is GrammarLL1!");
        else
            System.out.println("The grammar isn't GrammarLL1!");

        System.out.println("\nAnalysis table(before analysis):");
        Character[][] table = mapTest.tableInit(mapTest.getMap());
        mapTest.setTable(table);
        for(Character[] chs:mapTest.getTable()){
            for(Character ch:chs){
                System.out.print(ch+" ");
            }
            System.out.println();
        }

        System.out.println("\nAnalysis table(after analysis):");
        Map<Integer[], SingleRule> tableMap =
                mapTest.analyzeTableMap(mapTest.getMap(),mapTest.getTable());
        mapTest.setTableMap(tableMap);
        for(Map.Entry<Integer[],SingleRule> entryOut:mapTest.getTableMap().entrySet()){
            Integer[] ptr = entryOut.getKey();
            SingleRule sruleMap = entryOut.getValue();

            System.out.println("Location: ("+ptr[0]+","+ptr[1]+")\tRule: "+sruleMap);
        }

        System.out.println("\nThe analysis table is:");
        String[][] strTable = ForecastUtil.analyzeTable(mapTest.getTable(),mapTest.getTableMap());
        for(int i = 0;i < strTable.length;i++ ){
            for(int j = 0;j < strTable[0].length;j++){
                System.out.print(strTable[i][j]+"\t");
            }
            System.out.println();
        }

        String sentence = "a+a";
        System.out.println("\nThe sentence is: "+sentence);
        PDALL1 pda = new PDALL1(sentence,mapTest.getTable(),mapTest.getTableMap());
        ArrayList<String[]> analyzeTbl = pda.analyze();
        System.out.println("The analysis table:");
        for(String[] item:analyzeTbl){
            for(String value:item){
                System.out.print(value+"\t");
            }
            System.out.println();
        }

        System.out.println("\nAfter analyzing...");
        if(pda.getState().equals(PDALL1.State.SUCCEED))
            System.out.println("The sentence is right!");
        else
            System.out.println("The sentence is wrong!");


    }
}
