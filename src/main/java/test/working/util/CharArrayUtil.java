package test.working.util;

import java.nio.channels.ClosedChannelException;

public class CharArrayUtil {
    private static Character[] charArray;
    private static Character[][] charTable;

    public CharArrayUtil(Character[] charArray){
        this.charArray = charArray;

    }
    public CharArrayUtil(Character[][] charTable){
        this.charTable = charTable;
    }

    public static int getIndex(Character ch){
        for(int i = 1;i < charArray.length;i++){
            if(charArray[i].equals(ch))
                return i;
        }
        return -1;
    }

    public static int getIndex2(Character ch){
        for(int i = 1;i < charTable.length;i++){
            if(charTable[i][0].equals(ch))
                return i;
        }
        return -1;
    }
}
