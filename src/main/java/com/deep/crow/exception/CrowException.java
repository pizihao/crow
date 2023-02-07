package com.deep.crow.exception;

import com.deep.crow.util.StrUtil;

/**
 * @author Create by liuwenhao on 2022/4/7 16:15
 */
public class CrowException extends RuntimeException {
  private static final long serialVersionUID = 4220666905005394823L;

  public CrowException() {
    super();
  }

  public CrowException(Throwable throwable) {
    super(throwable);
  }

  public CrowException(String message) {
    super(message);
  }

  public static CrowException exception(String message) {
    return new CrowException(message);
  }

  public static CrowException exception(Throwable throwable) {
    if (throwable instanceof CrowException) {
      return new CrowException(throwable.getMessage());
    }
    return new CrowException(throwable);
  }

  public static CrowException exception(String format, Object... elements) {
    return new CrowException(StrUtil.format(format, elements));
  }

  public static CrowException of(String msg) {
    throw CrowException.exception(msg);
  }

  public static CrowException of(String format, Object... elements) {
    throw CrowException.exception(format, elements);
  }

  public static CrowException of(Throwable throwable) {
    throw CrowException.exception(throwable);
  }
}
