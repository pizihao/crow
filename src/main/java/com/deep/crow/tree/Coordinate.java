package com.deep.crow.tree;

import java.util.Objects;

/**
 * @author Create by liuwenhao on 2022/4/13 0:52
 */
public class Coordinate {

    /**
     * 类似于坐标轴上的y轴
     */
    int y;

    /**
     * 类似于坐标轴上的x轴
     */
    int x;

    Coordinate(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return y == that.y && x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }
}