package GDLParser;

import java.util.ArrayList;

/**
 * Created by siavj on 15/10/2016.
 */
public class Production {

    int lhs;
    int[] rhs;

    public Production(int lhs, int[] rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String toString(){
        String x = "";
        x += lhs;
        x += "-> ";
        for (int y : rhs)
            x += y + ", ";
        return x;
    }
}
