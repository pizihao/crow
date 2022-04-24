package com.deep.crow.type;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/**
 * <h2>泛型的上限和下限</h2>
 * 例如：<br>
 * {@code  <? extends Class>}<br>
 * {@code  <? super Class>}<br>
 * 仿照{@link sun.reflect.generics.reflectiveObjects.WildcardTypeImpl}
 *
 * @author Create by liuwenhao on 2022/4/24 15:03
 */
@SuppressWarnings("unused")
public class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    private WildcardTypeImpl(Type[] upper, Type[] lower) {
        this.upperBounds = upper;
        this.lowerBounds = lower;
    }

    public static WildcardTypeImpl make(Type[] upper, Type[] lower) {
        return new WildcardTypeImpl(upper, lower);
    }

    @Override
    public Type[] getUpperBounds() {
        return this.upperBounds.clone();
    }

    @Override
    public Type[] getLowerBounds() {
        return this.lowerBounds.clone();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WildcardType)) {
            return false;
        } else {
            WildcardType wildcardType = (WildcardType) obj;
            return Arrays.equals(this.upperBounds, wildcardType.getUpperBounds())
                && Arrays.equals(this.lowerBounds, wildcardType.getLowerBounds());
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.upperBounds) ^ Arrays.hashCode(this.lowerBounds);
    }

    public String toString() {
        StringBuilder stringBuilder;
        Type[] types;
        if (this.lowerBounds.length == 0) {
            if (this.upperBounds.length == 0 || Object.class == this.upperBounds[0]) {
                return "?";
            }
            types = this.upperBounds;
            stringBuilder = new StringBuilder("? extends ");
        } else {
            types = this.lowerBounds;
            stringBuilder = new StringBuilder("? super ");
        }
        for (int i = 0; i < types.length; ++i) {
            if (i > 0) {
                stringBuilder.append(" & ");
            }
            stringBuilder.append(types[i] instanceof Class ? ((Class<?>) types[i]).getName() : types[i].toString());
        }
        return stringBuilder.toString();
    }
}