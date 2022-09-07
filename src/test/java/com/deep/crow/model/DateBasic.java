package com.deep.crow.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/** @author Create by liuwenhao on 2022/5/12 11:02 */
public class DateBasic {

  private List<Date> dates;

  private List<LocalDateTime> localDateTimes;

  private List<LocalDate> localDates;

  private List<Date> dates1;
  private List<LocalTime> localTimes;

  public List<Date> getDates() {
    return dates;
  }

  public void setDates(List<Date> dates) {
    this.dates = dates;
  }

  public List<LocalDateTime> getLocalDateTimes() {
    return localDateTimes;
  }

  public void setLocalDateTimes(List<LocalDateTime> localDateTimes) {
    this.localDateTimes = localDateTimes;
  }

  public List<LocalDate> getLocalDates() {
    return localDates;
  }

  public void setLocalDates(List<LocalDate> localDates) {
    this.localDates = localDates;
  }

  public List<LocalTime> getLocalTimes() {
    return localTimes;
  }

  public void setLocalTimes(List<LocalTime> localTimes) {
    this.localTimes = localTimes;
  }

  public List<Date> getDates1() {
    return dates1;
  }

  public void setDates1(List<Date> dates1) {
    this.dates1 = dates1;
  }

  @Override
  public String toString() {
    return "DateBasic{"
        + "dates="
        + dates
        + ", localDateTimes="
        + localDateTimes
        + ", localDates="
        + localDates
        + ", dates1="
        + dates1
        + ", localTimes="
        + localTimes
        + '}';
  }
}
