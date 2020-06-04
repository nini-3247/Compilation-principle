package com.working.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * 表格中的一条记录的数据结构
 *
 */
public class LexResult {
    private String symbol;
    private String TAG;

    public LexResult() {
        this(null, null);
    }

    public LexResult(String symbol, String TAG) {
        // 用property来绑定数据，实现双向监听
        this.symbol = symbol;
        this.TAG = TAG;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public String toString() {
        return "LexResult{" +
                "symbol=" + symbol +
                ", TAG=" + TAG +
                '}';
    }
}
