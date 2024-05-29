package pl.rsww.touroperator.price;

import java.math.MathContext;

public class PriceTools {
    private static final int PRECISION = 2;
    private static MathContext mathContext;

    public static MathContext getContext(){
        if(mathContext == null){
            mathContext = new MathContext(PRECISION);
        }
        return mathContext;
    }
}
