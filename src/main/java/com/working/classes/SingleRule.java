package com.working.classes;

public class SingleRule {
    private Character key;
    private String value;

    public SingleRule(Character key,String value){
        this.key = key;
        this.value = value;
    }

    public Character getKey() {
        return key;
    }

    public void setKey(Character key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key+"->"+value;
    }
}
