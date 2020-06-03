package test.working.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AnalysisResult {
    private final StringProperty step;
    private final StringProperty stack;
    private final StringProperty string;
    private final StringProperty map;

    public AnalysisResult() {
        this(null,null,null,null);
    }

    public AnalysisResult(String step, String stack, String string, String map) {
        // 用property来绑定数据，实现双向监听
        this.step = new SimpleStringProperty(step);
        this.stack = new SimpleStringProperty(stack);
        this.string = new SimpleStringProperty(string);
        this.map = new SimpleStringProperty(map);
    }

    public String getStep() {
        return step.get();
    }

    public StringProperty stepProperty() {
        return step;
    }

    public void setStep(String step) {
        this.step.set(step);
    }


    public String getStack() {
        return stack.get();
    }

    public StringProperty stackProperty() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack.set(stack);
    }


    public String getString() {
        return string.get();
    }

    public StringProperty stringProperty() {
        return string;
    }

    public void setString(String string) {
        this.string.set(string);
    }


    public String getMap() {
        return map.get();
    }

    public StringProperty mapProperty() {
        return map;
    }

    public void setMap(String map) {
        this.map.set(map);
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "step=" + step +
                ", stack=" + stack +
                ", string=" + string +
                ", map=" + map +
                '}';
    }
}
