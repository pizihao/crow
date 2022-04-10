package com.deep.crow.completable;

import cn.net.nova.common.utils.CollectionTools;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <h2>返回值为Map的执行方式</h2>
 * <p>通过容器中的任务的name为唯一标识，或者最终需要执行的任务</p>
 * <p>获取任务的结果时需通过装入任务时定义的name来获取执行结果</p>
 * 示例：
 * <pre>
 * {@code
 *         Map<String, Object> map = CofHelper.buildSupplyMap()
 *             .register(() -> 1,"one")
 *             .register(() -> "test","two")
 *             .register(ArrayList::new,"three")
 *             .exec();
 *         Integer one = (Integer) map.get("one"); // 1
 *         String two = (String) map.get("two"); // test
 *         List three = (List) map.get("three"); // []
 * }
 * </pre>
 *
 * @author Create by liuwenhao on 2021/11/26 11:45
 */
class CofSupplyMap extends CofSupply<Map<String, Object>> {

    @Override
    public Map<String, Object> exec(Predicate<CofTask<Supplier<Object>>> predicate) {
        if (Objects.isNull(supplies) || supplies.isEmpty()) {
            return Maps.newHashMap();
        }
        // 去重
        Set<CofTask<Supplier<Object>>> supplyList = supplies.stream()
            .filter(predicate)
            .filter(c -> c.getName() != null)
            .collect(Collectors.toSet());
        // 获取结果
        return CollectionTools.toMap(perform(supplyList), Signature::getName, c -> c.getCompletableFuture().join());
    }

    protected CofSupplyMap() {
        this.supplies = Lists.newArrayList();
    }

    protected CofSupplyMap(@Nullable ExecutorService executorService) {
        this.supplies = Lists.newArrayList();
        this.executorService = executorService;
    }

    protected CofSupplyMap(@Nullable List<CofTask<Supplier<Object>>> supplies, @Nullable ExecutorService executorService) {
        this.supplies = Objects.nonNull(supplies) ? supplies : Lists.newArrayList();
        this.executorService = executorService;
    }

    @Override
    public Cof<Supplier<Object>, Map<String, Object>> register(@NotNull Supplier<Object> s, @NotNull ExecutorService e, @NotNull String n) {
        supplies.add(CofTask.buildSupply().task(s).executorService(e).name(n));
        return this;
    }

    @Override
    public Cof<Supplier<Object>, Map<String, Object>> register(@NotNull Supplier<Object> s, @NotNull String n) {
        supplies.add(CofTask.buildSupply().task(s).name(n));
        return this;
    }
}