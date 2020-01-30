package com.suneesh.trading.core;

import static java.lang.Math.abs;

public class BlackScholesPricing {

//    private static final double P = 0.2316419;
//    private static final double B1 = 0.319381530;
//    private static final double B2 = -0.356563782;
//    private static final double B3 = 1.781477937;
//    private static final double B4 = -1.821255978;
//    private static final double B5 = 1.330274429;
//
//    public static double[] calculate(boolean c,
//                                     double s, double k, double r, double t, double v) {
//
//        double[] p = new double[6];
//
//        double d1 = d1(s, k, r, t, v);
//        double d2 = d2(s, k, r, t, v);
//
//        double sd1 = standardNormalDistribution(d1);
//        double cd1 = cumulativeDistribution(d1, sd1);
//        double thetaLeft = -(s * sd1 * v) / (2 * sqrt(t));
//
//        if (c) {
//
//            double cd2 = cumulativeDistribution(d2);
//
//// price
//            p[0] = s * cd1 – k * exp(-r * t) * cd2;
//
//// delta
//            p[1] = cd1;
//
//// theta
//            double thetaRight = r * k * exp(-r * t) * cd2;
//            p[4] = thetaLeft – thetaRight;
//
//// rho
//            p[5] = k * t * exp(-r * t) * cd2;
//
//        } else {
//
//            double pcd1 = cumulativeDistribution(-d1);
//            double pcd2 = cumulativeDistribution(-d2);
//
//// price
//            p[0] = k * exp(-r * t) * pcd2 – s * pcd1;
//
//// delta
//            p[1] = cd1 – 1;
//
//// theta
//            double thetaRight = r * k * exp(-r * t) * pcd2;
//            p[4] = thetaLeft + thetaRight;
//
//// rho
//            p[5] = -k * t * exp(-r * t) * pcd2;
//
//        }
//
//// gamma
//        p[2] = sd1 / (s * v * sqrt(t));
//
//// vega
//        p[3] = s * sd1 * sqrt(t);
//
//        return p;
//
//    }
//
//    private static double d1(double s, double k, double r, double t, double v) {
//        double top = log(s / k) + (r + pow(v, 2) / 2) * t;
//        double bottom = v * sqrt(t);
//        return top / bottom;
//    }
//
//    private static double d2(double s, double k, double r, double t, double v) {
//        return d1(s, k, r, t, v) – v * sqrt(t);
//    }
//
//    public static double cumulativeDistribution(double x) {
//        return cumulativeDistribution(x, standardNormalDistribution(x));
//    }
//
//    public static double cumulativeDistribution(double x, double sdx) {
//        double t = 1 / (1 + P * abs(x));
//        double t1 = B1 * pow(t, 1);
//        double t2 = B2 * pow(t, 2);
//        double t3 = B3 * pow(t, 3);
//        double t4 = B4 * pow(t, 4);
//        double t5 = B5 * pow(t, 5);
//        double b = t1 + t2 + t3 + t4 + t5;
//        double cd = 1 – sdx * b;
//        return x < 0 ? 1 – cd : cd;
//    }
//
//    public static double standardNormalDistribution(double x) {
//        double top = exp(-0.5 * pow(x, 2));
//        double bottom = sqrt(2 * PI);
//        return top / bottom;
//    }

}

