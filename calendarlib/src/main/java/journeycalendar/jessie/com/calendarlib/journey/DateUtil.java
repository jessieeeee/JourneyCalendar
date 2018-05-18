package journeycalendar.jessie.com.calendarlib.journey;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 编写人:JessieK
 * @date 创建时间:2017/2/23
 * @description 描述:日期工具类
 */


public class DateUtil {

    private static Calendar curDay = Calendar.getInstance();//当前日
    private static DateTime curDayDateTime;

    // 动态日历范围需要以下设置
    private static int preMonthNum = 3;//前三个月
    private static int nextMonthNum = 6;//后六个月

    // 指定日期范围需要以下设置
    private static int startMonth = 0 ; //开始月份
    private static int startYear = 2019 ;//开始年份
    private static int endMonth = 1 ; //结束月份
    private static int endYear = 2019 ; //结束年份

    public static int DYNAMIC = 0; //动态设置日历范围，以当前日为基准，前X个月，后Y个月
    public static int CUSTOM = 1;  //指定日期范围，2019-1 到 2019-2月
    private static int curState = DYNAMIC; // 当前模式默认动态范围

    static {
//        setCurState(CUSTOM);
//        setStartYear(2019);
//        setEndYear(2020);
//        setStartMonth(1);
//        setEndMonth(1);
    }
    /**
     * 设置当前模式
     * @param curState
     */
    public static void setCurState(int curState) {
        DateUtil.curState = curState;
        if (curState == CUSTOM) {
           setCurDay(2019,1,12);
        }
    }

    /**
     * 自定义范围，开始月份
     * @param startMonth
     */
    public static void setStartMonth(int startMonth) {
        if (startMonth-1 < 0){
            startMonth = 0;
        } else {
            startMonth = startMonth - 1;
        }
        DateUtil.startMonth = startMonth;
    }

    /**
     * 自定义范围，开始年份
     * @param startYear
     */
    public static void setStartYear(int startYear) {
        DateUtil.startYear = startYear;
    }

    /**
     * 自定义范围，结束月份
     * @param endMonth
     */
    public static void setEndMonth(int endMonth) {
        if (endMonth-1 < 0) {
            endMonth = 0;
        } else {
            endMonth = endMonth - 1;
        }
        DateUtil.endMonth = endMonth;
    }

    /**
     * 自定义范围，结束年份
     * @param endYear
     */
    public static void setEndYear(int endYear) {
        if (endYear - startYear < 0) {
            endYear = startYear;
        }
        DateUtil.endYear = endYear;
    }

    /***
     * 设置当前的日期
     * @param year 年
     * @param month 月
     * @param day 日
     */
    public static void setCurDay(int year,int month,int day){
        if (month-1 < 0){
            month = 0;
        } else {
            month = month-1;
        }
        curDay.set(year,month,day);
    }

    /**
     * 设置前X个月
     * @param pre
     */
    public static void setPreMonthNum(int pre) {
        preMonthNum = pre;
    }

    /**
     * 设置后Y个月
     * @param next
     */
    public static void setNextMonthNum(int next) {
        nextMonthNum = next;
    }


    /**
     * 需要展示的月份数
     * @return
     */
    public static int showMonthNum() {
        // 如果当前是动态展示
        if (curState == DYNAMIC) {
            return preMonthNum + nextMonthNum + 1;
        } else { //如果当前是指定范围展示
            return (endYear - startYear)*12 + (endMonth - startMonth + 1);
        }
    }

    public static Date getCurWeekDayDate() {
        return getCurWeekDay().getTime();
    }

    public static DateTime getCurWeekDayDateTime() {
        if (curDayDateTime==null){
            curDayDateTime=new DateTime(curDay);
        }
        return curDayDateTime;
    }

    public static Calendar getCurWeekDay() {
       return curDay;
    }

    //计算当前页数
    public static int countCurPage(){
        //初始化开始时间
        Calendar c= Calendar.getInstance();
        int startYear=getStartYear();
        int startMonth=getFirstMonth();
        c.set(startYear,startMonth,1);
        DateTime firstDay=new DateTime(c);
        if(firstDay.get(DateTimeFieldType.dayOfWeek())!=DateTimeConstants.SUNDAY){//不是星期天
            firstDay=firstDay.withDayOfWeek(DateTimeConstants.SUNDAY).plusDays(-7);//上个星期天
        }
        DateTime endDay=getCurWeekDayDateTime();
        if(endDay.get(DateTimeFieldType.dayOfWeek())!=DateTimeConstants.SUNDAY){//不是星期天
            endDay=endDay.withDayOfWeek(DateTimeConstants.SATURDAY);//当前周六
        }else{
            endDay=endDay.withDayOfWeek(DateTimeConstants.SATURDAY).plusDays(7);//下个周六
        }
        return diffWeek(firstDay.toDate(),endDay.toDate())-1;
    }

    //计算总页数
    public static int countPageNum(){
        //初始化开始时间
        Calendar c= Calendar.getInstance();
        int startYear=getStartYear();
        int startMonth=getFirstMonth();
        c.set(startYear,startMonth,1);
        DateTime firstDay=new DateTime(c);
        if(firstDay.get(DateTimeFieldType.dayOfWeek())!=DateTimeConstants.SUNDAY){//不是星期天
            firstDay=firstDay.withDayOfWeek(DateTimeConstants.SUNDAY).plusDays(-7);//上个星期天
        }
        Calendar end= Calendar.getInstance();
        //初始化结束时间
        int endYear=getEndYear();
        int endMonth=getLastMonth();
        end.set(endYear,endMonth,1);
        int endDayOfMonth=end.getActualMaximum(Calendar.DAY_OF_MONTH);
        end.set(endYear,endMonth,endDayOfMonth);
        DateTime endDay=new DateTime(end);
        if(endDay.get(DateTimeFieldType.dayOfWeek())!=DateTimeConstants.SUNDAY){//不是星期天
            endDay=endDay.withDayOfWeek(DateTimeConstants.SATURDAY);//当前周六
        }else{
            endDay=endDay.withDayOfWeek(DateTimeConstants.SATURDAY).plusDays(7);//下个周六
        }
        return diffWeek(firstDay.toDate(),endDay.toDate());
    }

    public static int diffWeek(Date start, Date end){
        int offsetDay= (int) ((end.getTime()-start.getTime())/(1000*60*60*24));
        return (offsetDay+2)/7 ;
    }

    //获得开始月 前三个月
    public static int getFirstMonth(){
        if (curState == DYNAMIC) {
            Calendar c= Calendar.getInstance();
            int firstMonth= (c.get(Calendar.MONTH)-preMonthNum<0)?12+c.get(Calendar.MONTH)-preMonthNum:c.get(Calendar.MONTH)-preMonthNum;
            return firstMonth;
        } else {
            return startMonth;
        }
    }

    //获得结束月 后6个月
    public static int getLastMonth(){
        if (curState == DYNAMIC) {
            Calendar c= Calendar.getInstance();
            int lastMonth= (c.get(Calendar.MONTH)+nextMonthNum>12)?c.get(Calendar.MONTH)-12:c.get(Calendar.MONTH)+nextMonthNum;
            return lastMonth;
        } else {
            return endMonth;
        }

    }

    //开始年 前三个月
    public static int getStartYear(){
       if (curState == DYNAMIC) {
           Calendar c= Calendar.getInstance();
           if(c.get(Calendar.MONTH)-preMonthNum<0){
               return c.get(Calendar.YEAR)-1;
           }else{
               return c.get(Calendar.YEAR);
           }
       } else {
           return startYear;
       }
    }


    //结束年 后6个月
    public static int getEndYear(){
        if (curState == DYNAMIC) {
            Calendar c= Calendar.getInstance();
            if(c.get(Calendar.MONTH)+nextMonthNum>12){
                return c.get(Calendar.YEAR)+1;
            }else{
                return c.get(Calendar.YEAR);
            }
        } else{
            return endYear;
        }
    }
}
