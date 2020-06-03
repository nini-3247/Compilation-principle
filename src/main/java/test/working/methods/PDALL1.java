package test.working.methods;

import test.working.util.CharArrayUtil;
import test.working.classes.SingleRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;

public class PDALL1 {
    private Stack<Character> stack;
    private String string;
    private int ptr;
    private Map<Integer[],SingleRule> tableMap;
    private Character[][] table;
    enum State{ERROR,SUCCEED};
    private State state;

    public State getState() {
        return state;
    }

    public PDALL1(String string, Character[][] table, Map<Integer[], SingleRule> tableMap){
        stack = new Stack<>();
        stack.push('$'); // 在栈底放上$
        stack.push('S'); // 文法的开始符号入栈
        this.string = string+'$';// 在字符串结尾放上$
        ptr = 0;
        this.table = table;
        this.tableMap = tableMap;
        state = State.ERROR;
    }

    public ArrayList<String[]> analyze(){
        ArrayList<String[]> analyzeTbl;
        analyzeTbl = new ArrayList<>();
        // 存放步骤、栈中内容、剩余字符串、对应的规则信息
        String[] item;
        int step = 0;
        StringBuilder stackInfo;
        String nowStr;
        SingleRule srule;
        // 记录是否分析结束
        boolean isEnd = false;
        while(isEnd == false){
            // 各项信息初始化
            item = new String[4];
            step++;
            stackInfo = new StringBuilder();
            nowStr = string.substring(ptr);
            srule = null;
            // 将栈中内容放入StringBuilder
            for(Character ch:stack){
                stackInfo.append(ch);
            }
//            System.out.println(stackInfo.toString());
            // 记录当前输入符号和栈顶
            Character nowChar = string.charAt(ptr);
            Character stkTop = stack.peek();
            // 栈顶为$，即栈空，则判断当前输入符是否也为$
            // 若是，则分析成功，结束分析；若不是，则出错
            if(stkTop.equals('$')){
                if(nowChar.equals('$')){
                    state = State.SUCCEED;
                    isEnd = true;
                }
                else{
                    state = State.ERROR;
                    isEnd = true;
                }
            }
            // 当前栈顶是非终结符号，和当前输入符匹配
            // 匹配则出栈，输入符指针后移；否则出错
            else if(Character.isUpperCase(stkTop) == false){
                if(nowChar.equals(stkTop)){
                    stack.pop();
                    ptr++;
                }
                else{
                    state = State.ERROR;
                    isEnd = true;
                }
            }
            // 当前栈顶是非终结符号
            else{
                boolean canFind = false;
                for(Map.Entry<Integer[],SingleRule> entry:tableMap.entrySet()){
                    int row = new CharArrayUtil(table).getIndex2(stkTop);
                    int col = new CharArrayUtil(table[0]).getIndex(nowChar);
                    Integer[] tableLoc = entry.getKey();
                    Integer[] nowLoc = {row,col};
                    srule = entry.getValue();
                    // 利用预测分析表找到相应规则，即把规则右部对应的字符串中字符依次出栈
                    if(Arrays.equals(tableLoc,nowLoc)){
                        canFind = true;
                        Character key = srule.getKey();
                        String value = srule.getValue();
                        stack.pop();
                        for(int i = value.length()-1;i >= 0;i--){
                            Character ch = value.charAt(i);
                            if(ch.equals('#')){
                                break;
                            }
                            stack.push(ch);
                        }
                        break;
                    }
                }
                // 否则出错
                if(canFind == false){
                    state = State.ERROR;
                    isEnd = true;
                }
            }
            // 保存各项信息
            item[0] = String.valueOf(step);
            item[1] = stackInfo.toString();
            item[2] = nowStr;
            if(srule == null)
                item[3] = null;
            else
                item[3] = srule.toString();
            analyzeTbl.add(item);
        }
        return analyzeTbl;
    }

}
