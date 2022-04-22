package com.deep.crow.util;

import com.deep.crow.exception.CrowException;

import java.util.List;

/**
 * <h2>反射工具类</h2>
 *
 * @author Create by liuwenhao on 2022/4/22 19:25
 */
public class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * <h2>筛选结果类型</h2>
     * 在一个未知的结果集中匹配指定类型的项，匹配成功则直接返回<br>
     * 无匹配选项择抛出异常
     *
     * @param l     结果集
     * @param clazz 目标类
     * @return T
     * @author liuwenhao
     * @date 2022/4/22 19:33
     */
    @SuppressWarnings("unchecked")
    public <T> T screenType(List<?> l, Class<T> clazz) {
        for (Object o : l) {
            boolean instance = clazz.isInstance(o);
            if (instance) {
                return (T) o;
            }
        }
        throw CrowException.exception("无可匹配类型");
    }
}