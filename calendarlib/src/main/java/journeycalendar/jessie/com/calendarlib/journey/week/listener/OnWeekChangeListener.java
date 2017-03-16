package journeycalendar.jessie.com.calendarlib.journey.week.listener;

import org.joda.time.DateTime;

/**
 * @date 创建时间:2017/2/21
 * @author 编写人:JessieK
 */
public interface OnWeekChangeListener {

    void onWeekChange(DateTime firstDayOfTheWeek, boolean forward);
}
