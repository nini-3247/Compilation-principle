package com.working.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 从文件中读取内容的实用类
 *
 */
public class FileReadUtil {
    public static List<String> ReadSymbols(String fileName) throws IOException {
        // 从缓冲区读字符，不用一点一点从文件中读
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s;
        List<String> list = new ArrayList<>();
        // 先赋值再判断，不会留下每行的第一个字符
        // 以行为单位，一行为arrayList的一个元素
        while ((s = in.readLine()) != null) list.add(s);
        in.close();
        return list;
    }

}
