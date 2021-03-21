package com.technek.parrotnight.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Misc {

    public static double removeNull(String val) {
        if (val == null) {
            return 0d;
        }

        if (val.isEmpty()) {
            return 0d;
        }

        return Double.valueOf(val);
    }


    public static double round(final double value, int places) {
        if (places < 0) {
            places = 2;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
