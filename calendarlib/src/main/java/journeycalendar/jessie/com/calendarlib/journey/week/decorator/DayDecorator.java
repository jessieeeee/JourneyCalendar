package journeycalendar.jessie.com.calendarlib.journey.week.decorator;

import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.List;

/**
 * @date 创建时间:2017/2/21
 * @author 编写人:JessieK
 */
public interface DayDecorator {
    void decorate(View view, TextView dayTextView, DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime);
    void drawFlag(TextView flagText, DateTime dateTime, List<String> flagDates);
}
