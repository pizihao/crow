package com.deep.crow.code;

/**
 * 812. 最大三角形面积
 * 给定包含多个点的集合，从其中取三个点组成三角形，返回能组成的最大三角形的面积
 *
 * @author Create by liuwenhao on 2022/5/15 18:10
 */
public class Real0 {
    public double largestTriangleArea(int[][] points) {
        double ans = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    ans = Math.max(ans, crossProduct(points[i], points[j], points[k]));
                }
            }
        }
        return ans;
    }

    public double crossProduct(int[] d1, int[] d2, int[] d3) {
        return Math.abs(d1[0] * d2[1] + d2[0] * d3[1] + d3[0] * d1[1] - d1[0] * d3[1] - d2[0] * d1[1] - d3[0] * d2[1]) / 2.0;
    }
}