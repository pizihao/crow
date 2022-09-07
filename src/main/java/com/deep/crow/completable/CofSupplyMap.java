package com.deep.crow.completable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 返回值为Map的执行方式
 *
 * <p>通过容器中的任务的name为唯一标识，或者最终需要执行的任务
 *
 * <p>获取任务的结果时需通过装入任务时定义的name来获取执行结果 示例：
 *
 * <pre>{@code
 * Map<String, Object> map = CofHelper.buildSupplyMap()
 *     .register(() -> 1,"one")
 *     .register(() -> "test","two")
 *     .register(ArrayList::new,"three")
 *     .exec();
 * Integer one = (Integer) map.get("one"); // 1
 * String two = (String) map.get("two"); // test
 * List three = (List) map.get("three"); // []
 * }</pre>
 *
 * @deprecated {@link com.deep.crow.multi.Multi}
 * @author Create by liuwenhao on 2021/11/26 11:45
 */
@Deprecated
@SuppressWarnings("unused deprecated")
class CofSupplyMap extends CofSupply<Map<String, Object>> {

  @Override
  public Map<String, Object> exec(Predicate<CofTask<Supplier<Object>>> predicate) {
    if (Objects.isNull(supplies) || supplies.isEmpty()) {
      return new HashMap<>();
    }
    // 去重
    Set<CofTask<Supplier<Object>>> supplyList =
        supplies.stream()
            .filter(predicate)
            .filter(c -> c.getName() != null)
            .collect(Collectors.toSet());
    // 获取结果
    return perform(supplyList).stream()
        .collect(
            Collectors.toMap(
                Signature::getName, c -> c.getCompletableFuture().join(), (f, l) -> l));
  }

  protected CofSupplyMap() {
    this.supplies = new ArrayList<>();
  }

  protected CofSupplyMap(ExecutorService executorService) {
    this.supplies = new ArrayList<>();
    this.executorService = executorService;
  }

  protected CofSupplyMap(
      List<CofTask<Supplier<Object>>> supplies, ExecutorService executorService) {
    this.supplies = Objects.nonNull(supplies) ? supplies : new ArrayList<>();
    this.executorService = executorService;
  }

  @Override
  public Cof<Supplier<Object>, Map<String, Object>> register(
      Supplier<Object> s, ExecutorService e, String n) {
    supplies.add(CofTask.buildSupply().task(s).executorService(e).name(n));
    return this;
  }

  @Override
  public Cof<Supplier<Object>, Map<String, Object>> register(Supplier<Object> s, String n) {
    supplies.add(CofTask.buildSupply().task(s).name(n));
    return this;
  }
}
