package test.working;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import test.working.classes.AnalysisResult;
import test.working.methods.Controller;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage; // 窗口
    private BorderPane rootLayout; // 上下左右中的布局
//    private ObservableList<String[]> forecastResults; // 预测分析表的可观察列表
    private ObservableList<AnalysisResult> analysisResults; // 分析结果的可观察列表
    private static File loadFile;  // 待分析的文件
    private static Controller controller; // 事件处理

    @Override
    /**
     * 初始化窗口；通过内部的main方法自动调用
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("语法分析器");
        initRootLayout();
        showOverview();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Main() {
//        forecastResults = FXCollections.observableArrayList();
        analysisResults = FXCollections.observableArrayList();

        // 向表格中添加输出样例（初始均为无）
//        forecastResults.add(new String[6]);
        analysisResults.add(new AnalysisResult("无", "无", "无", "无"));
    }

    /**
     * 初始化RootLayout
     */
    public void initRootLayout() {
        try {
            // 加载root_layout.fxml
            FXMLLoader loader = new FXMLLoader();
//            System.out.println(getClass().getResource("/"));
            loader.setLocation(getClass().getResource("/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            // 创建并展示窗口
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化RootLayout中的Overview
     */
    public void showOverview() {
        try {
            // 加载Overview.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Overview.fxml"));
            AnchorPane grmContents = (AnchorPane) loader.load();
            // 把Overview放在RootLayout中间
            rootLayout.setCenter(grmContents);
            // 添加事件控制
            controller = loader.getController();
            controller.setMainApp(this);
//            controller.forecastTable.setItems(forecastResults);
            controller.resultTable.setItems(analysisResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /*public ObservableList<String[]> getForecastResults() {
        return forecastResults;
    }*/

    public ObservableList<AnalysisResult> getAnalysisResults() {
        return analysisResults;
    }

    // 加载文本文件
    public void load(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        // 只可选择文本文件
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // 显示文件打开对话框
        loadFile = fileChooser.showOpenDialog(primaryStage);
        // 把observableList的数据填充到tableView
//        controller.forecastTable.setItems(forecastResults);
        controller.resultTable.setItems(analysisResults);
        controller.setCode(loadFile);
    }

    public File getLoadFile() {
        return loadFile;
    }
}

