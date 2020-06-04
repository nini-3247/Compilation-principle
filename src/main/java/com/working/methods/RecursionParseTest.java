package com.working.methods;

public class RecursionParseTest {
    private static int ptr; // 句子的指针：静态全局变量，使得每次操作都对该指针进行修改
    private String sentence; // 待分析的句子

    // 默认解析句子char
    public RecursionParseTest(){
        ptr = 0;
        sentence = "char";
    }

    //当传递参数时，句子的指针先赋值为0
    public RecursionParseTest(String sen){
        ptr = 0;
        sentence = sen;
    }

    // 解析开始
    public void start(String str){
        System.out.println("Parsing " + str + "...");
    }
    //解析结束
    public void end(){
        System.out.println("——————————");
    }

    // 解析成功
    public void success(){
        System.out.println("The sentence is right!");
    }
    // 解析失败
    public void error(){
        System.out.println("An error occurs in the sentence!");
        System.exit(0);
    }

    // 匹配单词
    public void match(String str){
        int ind = 0;
        int senLen = sentence.length();
        int strLen = str.length();
        if(ptr >= senLen || strLen > senLen) // 指针超出范围或要解析的单词比句子还长，出错
            error();

        while(ptr < senLen && ind < strLen && // 指针未超出范围
                sentence.charAt(ptr) == str.charAt(ind)){ // 且单词的每个字母一一匹配，则继续检查下一个字母
            ind++;
            ptr++;
        }
        if(ptr <= senLen && ind >= strLen){ // 句子未结束或刚结束，且匹配的单词结束，则匹配成功
            return;
        } else
            error();
    }

    /* 文法 */
    /* type->simple|^id|array[simple]type */

    public void type(){
        if(ptr < sentence.length()){ // 先判断指针是否超出范围
            char nowChar = sentence.charAt(ptr);
            if(nowChar == 'n' || nowChar == 'b' || nowChar == 'c')
                simple();
            else if(nowChar == '^'){
                ptr++;
                match("id");
            }
            else if(nowChar == 'a'){
                match("array");
                match("[");
                simple();
                match("]");
                type();
            }else
                error();
        }else //若每个字符都匹配，则匹配成功
            success();
    }

    /* 文法 */
    /* simple->number|boolean|char */

    public void simple(){
        if(ptr < sentence.length()){ // 先判断指针是否超出范围
            char nowChar = sentence.charAt(ptr);
            if(nowChar == 'n'){
                match("number");
                return;
            }
            else if(nowChar == 'b'){
                match("boolean");
                return;
            }
            else if(nowChar == 'c'){
                match("char");
                return;
            }else
                error();
        }else //若每个字符都匹配，则匹配成功
            success();
    }

    public static void main(String[] args) {
        RecursionParseTest test1 = new RecursionParseTest();
        test1.start("test1");
        test1.type();
        test1.success();
        test1.end();

        RecursionParseTest test2 = new RecursionParseTest("array[number]^id");
        test2.start("test2");
        test2.type();
        test2.success();
        test2.end();

        RecursionParseTest test3 = new RecursionParseTest("^id");
        test3.start("test3");
        test3.type();
        test3.success();
        test3.end();

        RecursionParseTest test4 = new RecursionParseTest("array");
        test4.start("test4");
        test4.type();
        test4.success();
        test4.end();
    }
}
