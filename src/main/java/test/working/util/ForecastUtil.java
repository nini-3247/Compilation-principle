package test.working.util;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import test.working.classes.SingleRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ForecastUtil {
    private static String[][] forecastTbl;
    private static List<String[]> forecastLst;

    public ForecastUtil(){
        forecastLst = new ArrayList<>();
    }

    public static String[][] analyzeTable(Character[][] table, Map<Integer[], SingleRule> tableMap){
        int rowLen = table.length;
        int colLen = table[0].length;
        forecastTbl = new String[rowLen][colLen];
        for(int i = 0;i < rowLen;i++ ){
            for(int j = 0;j < colLen;j++){
                forecastTbl[i][j] = String.valueOf(table[i][j]);
            }
        }
        for(Map.Entry<Integer[],SingleRule> entry:tableMap.entrySet()){
            Integer[] loc = entry.getKey();
            String srule = entry.getValue().toString();

            int row = loc[0];
            int col = loc[1];
            forecastTbl[row][col] = srule;
        }

        return forecastTbl;
    }

    public static List<String[]> toList(String[][] table){
        forecastTbl = table;
        forecastLst = Arrays.asList(forecastTbl);
        return forecastLst;
    }

    public static TableView<List<String>> toTable(){
        TableView<List<String>> table = new TableView<>();

        for(String[] values:forecastTbl){
            // Add extra columns if necessary:
            for (int i = table.getColumns().size(); i < forecastTbl[0].length; i++) {
                TableColumn<List<String>, String> col = new TableColumn<>("Column "+(i+1));
//                col.setMinWidth(80);
//                TableColumn col = new TableColumn<>("Column "+(i+1));

                final int colIndex = i ;
                col.setCellValueFactory(/*new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>(){
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> cellData) {
                        return new ReadOnlyStringWrapper(cellData.getValue()[colIndex]);
                    }
                }*/data -> {
                    List<String> rowValues = data.getValue();
                    String cellValue ;
                    if (colIndex < rowValues.size()) {
                        cellValue = rowValues.get(colIndex);
                    } else {
                        cellValue = "" ;
                    }
                    return new ReadOnlyStringWrapper(cellValue);
                });
                table.getColumns().add(col);
            }

            // add row:
          table.getItems().add(Arrays.asList(values));
        }

        return table;
    }
}
