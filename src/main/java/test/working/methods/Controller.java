package test.working.methods;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import test.working.Main;
import test.working.classes.*;
import test.working.classes.Error;
import test.working.util.ForecastUtil;
import test.working.util.SelectUtil;
import test.working.util.SetToStringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    public Mapping mapping;
    public Map<Character, ArrayList<String>> map;
    public Map<Character, ArrayList<Character>> firstSet;
    public Map<Character,ArrayList<Character>> followSet;
    public Map<Character,ArrayList<Character[]>> selectSets;
    public boolean judgeLL1;
    public Character[][] table;
    public Map<Integer[], SingleRule> tableMap;

    public String string;
    public ArrayList<String[]> analysisTable;

    public TableView<String[]> forecastTable; // 预测分析表
    public TableView<AnalysisResult> resultTable; // 结果生成表

    public TableColumn<AnalysisResult, String> StepColumn; // 步骤
    public TableColumn<AnalysisResult, String> StackColumn; // 栈中元素
    public TableColumn<AnalysisResult, String> StringColumn; // 剩余字符串
    public TableColumn<AnalysisResult, String> MapColumn; // 对应的映射

    public Label pathLabel; // 文件路径
    public TextArea code; // 待分析的代码
    public TextArea firstArea; // first集文本框
    public TextArea followArea; // follow集文本框
    public TextArea forecastArea; // 预测分析表
    public TextField stringField; // 待分析的字符串

    private static ObservableList<String[]> forecastResults;
    private static ObservableList<AnalysisResult> analysisResults;
    private Main main;

    public void calSets(ActionEvent actionEvent){
        // 导入文件中的代码
        System.out.println(main.getLoadFile().getPath());

        if (main.getLoadFile() == null) {
            pathLabel.setText("未导入文件");
        } else{
            mapping = new Mapping();
            map = new HashMap<>();
            map =mapping.mapInit(main.getLoadFile().getPath(),map);
            firstSet = mapping.findAllFirstSet(map);
            followSet = mapping.findAllFollowtSet(map);
            selectSets = mapping.findAllSelectSets(map);
            judgeLL1 = SelectUtil.isLL1(map);

            if(judgeLL1 == false){
                new Error(Error.Type.LL1Error);
                firstArea.setText("不是LL1文法！");
                followArea.setText("不是LL1文法！");
            }else{
                firstArea.setText(SetToStringUtil.toString(firstSet));
                followArea.setText(SetToStringUtil.toString(followSet));
            }
        }

    }

    public void forecast(ActionEvent actionEvent) {

        if (main.getLoadFile() == null) {
            code.setText("未导入文件");
            new Error(Error.Type.FileError);
        } else {
//            if (forecastResults != null)
//                forecastResults.clear(); //本来有内容，则先删除
            if(judgeLL1){
                StringBuilder tmpBld = new StringBuilder(code.getText());
                tmpBld.append("\n该文法是LL1文法！\n");
                code.setText(tmpBld.toString());

                table = mapping.tableInit(map);
                tableMap = mapping.analyzeTableMap(map,table);

                ForecastUtil.analyzeTable(table,tableMap);

                String[][] forecastTbl = ForecastUtil.analyzeTable(table,tableMap);
                StringBuilder strTbl = new StringBuilder();
                int row = 0;
                for(String[] rows:forecastTbl){
                    row++;
                    for(String cols:rows){
                        strTbl.append(cols+"\t");
                        if(cols.equals("null") && row == 1)
                            continue;
                        if(cols.equals("null") || row == 1)
                            strTbl.append("\t");
                    }
                    strTbl.append("\n");
                }
                forecastArea.setText(strTbl.toString());

//                forecastTable = ForecastUtil.toTable();
//                forecastResults = FXCollections.observableList(ForecastUtil.toList(forecastTbl));
//                forecastTable = new TableView<>();
//                forecastTable.setItems(forecastResults); //把observableList的数据填充到tableView
            }

        }
    }

    public void analyze(ActionEvent actionEvent){
        if (main.getLoadFile() == null) {
            pathLabel.setText("未导入文件");
        } else {
            if (analysisResults != null)
                analysisResults.clear(); //本来有内容，则先删除
            string = stringField.getText();
            analysisTable = new PDALL1(string,table,tableMap).analyze();

            List<AnalysisResult> analysisRstLst = new ArrayList<>();
            boolean isSentence = false;
            for(String[] item:analysisTable){
                AnalysisResult analysisRst = new AnalysisResult(item[0],item[1],item[2],item[3]);
                analysisRstLst.add(analysisRst);
                if(item[1].equals("$") && item[2].equals("$"))
                    isSentence = true;
            }

            if(isSentence == false){
                analysisRstLst = new ArrayList<>();
                analysisRstLst.add(new AnalysisResult("无","无","无","无"));

                StringBuilder tmpBld = new StringBuilder(code.getText());
                tmpBld.append("\n"+string);
                tmpBld.append("\n不是该文法中的句子！");
                code.setText(tmpBld.toString());
            }else{
                StringBuilder tmpBld = new StringBuilder(code.getText());
                tmpBld.append("\n"+string);
                tmpBld.append("\n是该文法中的句子！");
                code.setText(tmpBld.toString());
            }

            analysisResults = FXCollections.observableArrayList(analysisRstLst);
            resultTable.setItems(analysisResults); //把observableList的数据填充到tableView
        }
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    // cellFactory将tableView的每一行填充数据
    private void initialize() {
        // 在界面上初始化表格中每一列的内容
        StepColumn.setCellValueFactory(
                cellData ->  cellData.getValue().stepProperty());
        StackColumn.setCellValueFactory(
                cellData -> cellData.getValue().stackProperty());
        StringColumn.setCellValueFactory(
                cellData -> cellData.getValue().stringProperty());
        MapColumn.setCellValueFactory(
                cellData -> cellData.getValue().mapProperty());

    }

    public void setMainApp(Main mainApp) {
        this.main = mainApp;
    }

    // 从文件中提取代码
    public void setCode(File file) {
        BufferedReader in = null;
        try{
            pathLabel.setText("路径："+file.getPath());

            int currentLine = 1;
            in = new BufferedReader(new FileReader(file));
            String s;
            StringBuilder sb = new StringBuilder(); // 用StringBuilder因为之后方便append
            // 按行读取文件内容
            while ((s = in.readLine()) != null) {
                sb.append(currentLine < 10 ? currentLine + "  |  " : currentLine + " |  ");  //对齐行号
                sb.append(s);
                sb.append("\n");
                currentLine++;
            }
            code.setText(sb.toString());

            // 做一些清空的初始化工作
            firstArea.setText("");
            followArea.setText("");
            forecastArea.setText("");
            stringField.setText("");
            List<AnalysisResult> analysisRstLst = new ArrayList<>();
            analysisRstLst.add(new AnalysisResult("无","无","无","无"));
            analysisResults = FXCollections.observableArrayList(analysisRstLst);
            resultTable.setItems(analysisResults);
        }catch(IOException ioe){
            new Error(Error.Type.FileError);
//                System.exit(0);
        }finally {
            try{
                in.close();
            }catch(IOException ioe){
                new Error(Error.Type.FileError);
//                System.exit(0);
            }

        }
    }
}


