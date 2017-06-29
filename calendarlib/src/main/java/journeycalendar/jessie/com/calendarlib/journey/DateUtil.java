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
    private static int preMonthNum=3;//前三个月
    private static int nextMonthNum=6;//后六个月

    public static void setPreMonthNum(int pre) {
        preMonthNum = pre;
    }

    public static void setNextMonthNum(int next) {
        nextMonthNum = next;
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
        DateTime endDay=DateTime.now();
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
        Calendar c= Calendar.getInstance();
        int firstMonth= (c.get(Calendar.MONTH)-preMonthNum<0)?12+c.get(Calendar.MONTH)-preMonthNum:c.get(Calendar.MONTH)-preMonthNum;
        return firstMonth;
    }

    //获得结束月 后6个月
    public static int getLastMonth(){
        Calendar c= Calendar.getInstance();
        int lastMonth= (c.get(Calendar.MONTH)+nextMonthNum>12)?c.get(Calendar.MONTH)-12:c.get(Calendar.MONTH)+nextMonthNum;
        return lastMonth;
    }

    //开始年 前三个月
    public static int getStartYear(){
        Calendar c= Calendar.getInstance();
        if(c.get(Calendar.MONTH)-preMonthNum<0){
            return c.get(Calendar.YEAR)-1;
        }else{
            return c.get(Calendar.YEAR);
        }
    }

    //结束年 后6个月
    public static int getEndYear(){
        Calendar c= Calendar.getInstance();
        if(c.get(Calendar.MONTH)+nextMonthNum>12){
            return c.get(Calendar.YEAR)+1;
        }else{
            return c.get(Calendar.YEAR);
        }
    }
}
