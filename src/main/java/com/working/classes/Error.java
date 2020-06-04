package com.working.classes;

public class Error {
    public enum Type{FileError,LeftError,LoopError,LL1Error};

    public Error(Type type){
        switch (type){
            case FileError:
                System.out.println("文件操作失败！");
                break;
            case LeftError:
                System.out.println("规则左部的字符多于一个，不是2型文法！");
                break;
            case LoopError:
                System.out.println("文法中有环路，不是2型文法！");
                break;
            case LL1Error:
                System.out.println("不是LL1文法！");
                break;
        }
    }

}
