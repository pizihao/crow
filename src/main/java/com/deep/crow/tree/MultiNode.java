package com.deep.crow.tree;

import com.deep.crow.task.serial.SerialMulti;

import java.util.Objects;

/**
 * 树的节点
 *
 * @author Create by liuwenhao on 2022/4/13 0:13
 */
public class MultiNode {

    /**
     * 串行化链
     */
    SerialMulti<?> serialMulti;

    /**
     * 坐标
     */
    Coordinate coordinate;

    /**
     * 父节点
     */
    MultiNode parent;

    public MultiNode(SerialMulti<?> serialMulti, Coordinate coordinate, MultiNode parent) {
        this.serialMulti = serialMulti;
        this.coordinate = coordinate;
        this.parent = parent;
    }

    public MultiNode(SerialMulti<?> serialMulti, Coordinate coordinate) {
        this(serialMulti, coordinate, null);
    }

    public MultiNode(SerialMulti<?> serialMulti, int y, int x, MultiNode parent) {
        this.coordinate = new Coordinate(y, x);
        this.serialMulti = serialMulti;
        this.parent = parent;
    }

    public MultiNode(SerialMulti<?> serialMulti, int y, int x) {
        this(serialMulti, y, x, null);
    }

    public MultiNode serialMulti(SerialMulti<?> serialMulti) {
        this.serialMulti = serialMulti;
        return this;
    }

    public MultiNode coordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public SerialMulti<?> getSerialMulti() {
        return serialMulti;
    }

    public void setSerialMulti(SerialMulti<?> serialMulti) {
        this.serialMulti = serialMulti;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public MultiNode getParent() {
        return parent;
    }

    public void setParent(MultiNode parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiNode multiNode = (MultiNode) o;
        return coordinate.equals(multiNode.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }
}