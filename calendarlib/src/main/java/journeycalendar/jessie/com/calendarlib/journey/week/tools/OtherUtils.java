/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package journeycalendar.jessie.com.calendarlib.journey.week.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 创建时间:2017/2/21
 * @author 编写人:JessieK
 */
public class OtherUtils {

    /**
     * 格式(yyyy-MM-dd)
     */
    public static final String DATE_PATTERN_1 = "yyyy-MM-dd";
    /**
     * 格式(yyyy-MM)
     */
    public static final String DATE_PATTERN_2 = "yyyy-MM";

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date 日期
     * @return 日期
     */
    public static String formatMonth(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern("yyyy-MM");
        return df.format(date);
    }

    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date 日期
     * @return 日期
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern("yyyy年MM月dd号");
        return df.format(date);
    }


    /**
     * 根据指定的格式对日期进行格式化处理
     *
     * @param date   日期
     * @param format 格式
     * @return 日期
     */
    public static String formatDate(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(format);
        return df.format(date);
    }

    public static Date formatDateStr(String dateStr,String format) throws ParseException {
        if (null == dateStr) {
            return null;
        }
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern(format);
        return df.parse(dateStr);
    }
}
