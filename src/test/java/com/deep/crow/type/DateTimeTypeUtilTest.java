package com.deep.crow.type;

import com.deep.crow.model.*;
import com.deep.crow.headbe.TypeUtil;
import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <h2>日期格式的数据填充</h2>
 *
 * @author Create by liuwenhao on 2022/5/11 11:40
 */
public class DateTimeTypeUtilTest  extends TestCase {

    private static void testExample() {
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        localDateTimes.add(LocalDateTime.now());
        localDateTimes.add(LocalDateTime.now().plusMinutes(10));
        List<LocalDate> localDates = new ArrayList<>();
        localDates.add(LocalDate.now());
        localDates.add(LocalDate.now().plusDays(10));
        List<LocalTime> localTimes = new ArrayList<>();
        localTimes.add(LocalTime.now());
        localTimes.add(LocalTime.now().plusMinutes(10));
        List<Date> dates = new ArrayList<>();
        dates.add(new Date());
        dates.add(new Date(2022, Calendar.NOVEMBER,12,52,12,1));

        List<Object> list = new ArrayList<>();
        list.add(localDates);
        list.add(dates);
        list.add(localDateTimes);
        list.add(localTimes);
        list.add(dates);

        DateBasic dateBasic = TypeUtil.fillClass(list, DateBasic.class, false);
        System.out.println(dateBasic);
    }
}