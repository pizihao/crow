package com.deep.crow.completable;

import com.deep.crow.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <h2>返回值为Tuple的执行方式</h2>
 * <p>最终的返回值为{@link Tuple}，其中的元素可以是不同的是数据类型</p>
 * <p>不会针对name进行任务的去重处理，执行结果的顺序就是装入任务的顺序</p>
 * 示例：
 * <pre>
 * {@code
 *        Tuple tuple = CofHelper.buildSupplyTuple()
 *             .register(() -> 1)
 *             .register(() -> "test")
 *             .register((@NotNull Supplier<Object>) ArrayList::new)
 *             .exec();
 *         Integer one = tuple.get(0); // 1
 *         String two = tuple.get(1); // test
 *         List three = tuple.get(2); // []
 * }
 * </pre>
 *
 * @author Create by liuwenhao on 2021/11/26 11:51
 */
class CofSupplyTuple extends CofSupply<Tuple> {

    @Override
    public Tuple exec(Predicate<CofTask<Supplier<Object>>> predicate) {
        if (Objects.isNull(supplies) || supplies.isEmpty()) {
            return new Tuple();
        }
        List<CofTask<Supplier<Object>>> list = supplies.stream().filter(predicate).collect(Collectors.toList());
        return new Tuple(perform(list).stream().map(c -> c.getCompletableFuture().join()).toArray());
    }

    protected CofSupplyTuple() {
        this.supplies = new ArrayList<>();
    }

    protected CofSupplyTuple(ExecutorService executorService) {
        this.supplies = new ArrayList<>();
        this.executorService = executorService;
    }

    protected CofSupplyTuple(List<CofTask<Supplier<Object>>> supplies, ExecutorService executorService) {
        this.supplies = Objects.nonNull(supplies) ? supplies : new ArrayList<>();
        this.executorService = executorService;
    }

    @Override
    public Cof<Supplier<Object>, Tuple> register(Supplier<Object> s, ExecutorService e) {
        supplies.add(CofTask.buildSupply().task(s).executorService(e));
        return this;
    }

    @Override
    public Cof<Supplier<Object>, Tuple> register(Supplier<Object> s) {
        supplies.add(CofTask.buildSupply().task(s));
        return this;
    }
}