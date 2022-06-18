package com.deep.crow.task.mix;

import com.deep.crow.exception.CrowException;
import com.deep.crow.multi.Multi;
import com.deep.crow.multi.MultiHelper;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * <h2>混合执行过程</h2>
 *
 * @author Create by liuwenhao on 2022/6/14 15:59
 */
public class MixMulti {

    /**
     * 线程池
     */
    ExecutorService executorService;

    /**
     * 混合任务集合
     */
    List<MixTask> mixTasks = new ArrayList<>();

    /**
     * 任务名称列表，验证是否存在
     */
    Set<String> taskName = new HashSet<>();

    /**
     * 任务尾结点集合，尾结点的前置节点不能是尾结点
     */
    List<MixTask> tailMixTasks = new ArrayList<>();

    /**
     * 开始节点
     */
    MixTask headTask;

    /**
     * <h2>添加任务。前置任务默认为开始节点</h2>
     *
     * @param name     当前任务标识
     * @param runnable 任务
     * @return MixMulti
     * @author liuwenhao
     * @date 2022/6/18 17:20
     */
    public MixMulti add(String name, Runnable runnable) {
        Multi<Void> multi = MultiHelper.runAsync(executorService, runnable);
        MixTask mixTask = new RunnableMixTask(name, multi);
        addTask(mixTask);
        return this;
    }

    /**
     * <h2>添加任务。并声明前置任务</h2>
     *
     * @param name     当前任务标识
     * @param runnable 任务
     * @param preName  前置任务
     * @return MixMulti
     * @author liuwenhao
     * @date 2022/6/18 17:20
     */
    public MixMulti add(String name, Runnable runnable, String... preName) {
        Multi<Void> multi = MultiHelper.runAsync(executorService, runnable);
        MixTask mixTask = new RunnableMixTask(name, multi);
        for (String s : preName) {
            mixTask.addPreName(s);
        }
        addTask(mixTask);
        return this;
    }


    /**
     * <h2>添加任务</h2>
     *
     * @param mixTask 任务实体
     * @author liuwenhao
     * @date 2022/6/18 17:34
     */
    private synchronized void addTask(MixTask mixTask) {
        checkName(mixTask.name());
        checkPreName(mixTask.pre());

    }

    /**
     * <h2>验证任务标识是否已经存在</h2>
     *
     * @param name 任务名称
     * @author liuwenhao
     * @date 2022/6/18 17:27
     */
    private void checkName(String name) {
        if (taskName.contains(name)) {
            CrowException.of("任务名称{}已存在", name);
        }
    }

    /**
     * <h2>验证前置任务是否已经存在</h2>
     * 如果不存在则异常
     *
     * @param preName 前置任务集合
     * @author liuwenhao
     * @date 2022/6/18 17:38
     */
    private void checkPreName(Set<String> preName) {
        boolean flag = true;
        String name = null;

        for (String s : preName) {
            boolean contains = taskName.contains(s);
            if (!contains) {
                flag = false;
                name = s;
                break;
            }
        }
        if (!flag) {
            CrowException.of("前置任务{}不存在", name);
        }
    }

}