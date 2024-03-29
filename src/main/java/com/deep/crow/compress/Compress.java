package com.deep.crow.compress;

/**
 * 对象压缩器 用部分代表整体，将一个大的对象压缩成小的对象，并且小对象可以兼容大对象的类型
 *
 * @author Create by liuwenhao on 2022/6/2 13:31
 */
interface Compress {

  /**
   * 压缩 这是一个压缩对象的过程，即有一个可以完全兼容的对象来代表一个大的对象 例如对于集合对象：就是将其中的一个拿出来<br>
   *
   * @return T
   * @author liuwenhao
   * @date 2022/6/2 14:27
   */
  <T> T compress();

  /**
   * 检查
   *
   * @return boolean
   * @author liuwenhao
   * @date 2022/6/2 14:27
   */
  boolean check();
}
