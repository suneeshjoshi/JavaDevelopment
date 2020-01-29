package com.suneesh.trading.engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BlackScholesPricingTest {

    @Test
    public void testGreeks() {

        boolean c;
        double s, k, r, t, v;
        double[] p;

        c = true;
        s = 56.25;
        k = 55;
        r = 0.0285;
        t = 0.34;
        v = 0.28;

        p = BlackScholesGreeks2.calculate(c, s, k, r, t, v);

        assertEquals(4.561, round(p[0], 3), 0);
        assertEquals(0.610, round(p[1], 3), 0);
        assertEquals(0.042, round(p[2], 3), 0);
        assertEquals(12.587, round(p[3], 3), 0);
        assertEquals(-6.030, round(p[4], 3), 0);
        assertEquals(10.110, round(p[5], 3), 0);

        c = false;
        p = BlackScholesGreeks2.calculate(c, s, k, r, t, v);

        assertEquals(2.781, round(p[0], 3), 0);
        assertEquals(-0.390, round(p[1], 3), 0);
        assertEquals(0.042, round(p[2], 3), 0);
        assertEquals(12.587, round(p[3], 3), 0);
        assertEquals(-4.478, round(p[4], 3), 0);
        assertEquals(-8.409, round(p[5], 3), 0);

    }

    static double round(double d, int places) {
        int factor = (int) Math.pow(10, places);
        return (double) Math.round(d * factor) / factor;
    }

}
