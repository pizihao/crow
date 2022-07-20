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
    private final Class<?>[] upper;
    private final Class<?>[] lower;

    public WildcardTypeImpl(Class<?>[] lower, Class<?>[] upper) {
        this.lower = lower != null ? lower : new Class[0];
        this.upper = upper != null ? upper : new Class[0];

        checkArgs();
    }

    private void checkArgs() {
        if (lower.length == 0 && upper.length == 0) {
            throw new IllegalArgumentException("lower or upper can't be null");
        }

        checkArgs(lower);
        checkArgs(upper);
    }

    private void checkArgs(Class<?>[] args) {
        for (int i = 1; i < args.length; i++) {
            Class<?> clazz = args[i];
            if (!clazz.isInterface()) {
                throw new IllegalArgumentException(clazz.getName() + " not a interface!");
            }
        }
    }

    @Override
    public Type[] getUpperBounds() {
        return upper;
    }

    @Override
    public Type[] getLowerBounds() {
        return lower;
    }

    @Override
    public String toString() {
        if (upper.length > 0) {
            if (upper[0] == Object.class) {
                return "?";
            }
            return getTypeString("? extends ", upper);
        } else {
            return getTypeString("? super ", lower);
        }
    }

    private String getTypeString(String prefix, Class<?>[] type) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);

        for (int i = 0; i < type.length; i++) {
            if (i != 0) {
                sb.append(" & ");
            }
            sb.append(type[i].getName());
        }

        return sb.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WildcardTypeImpl that = (WildcardTypeImpl) o;

        return Arrays.equals(upper, that.upper) && Arrays.equals(lower, that.lower);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(upper);
        result = 31 * result + Arrays.hashCode(lower);
        return result;
    }
}