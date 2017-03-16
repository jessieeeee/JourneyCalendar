package journeycalendar.jessie.com.calendarlib.journey.week.decorator;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.List;

import journeycalendar.jessie.com.calendarlib.R;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.OtherUtils;


/**
 * @author 编写人:JessieK
 * @date 创建时间:2017/2/21
 */
public class DefaultDayDecorator implements DayDecorator {

    private Context context;
    private final int selectedDateColor;
    private final int todayDateColor;
    private int todayDateTextColor;
    private int textColor;
    private float textSize;

    public DefaultDayDecorator(Context context,
                               @ColorInt int selectedDateColor,
                               @ColorInt int todayDateColor,
                               @ColorInt int todayDateTextColor,
                               @ColorInt int textColor,
                               float textSize) {
        this.context = context;
        this.selectedDateColor = selectedDateColor;
        this.todayDateColor = todayDateColor;
        this.todayDateTextColor = todayDateTextColor;
        this.textColor = textColor;
        this.textSize = textSize;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void decorate(View view, TextView dayTextView,
                         DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime) {
        //DateTime dt = new DateTime();

        Drawable holoCircle = ContextCompat.getDrawable(context, R.drawable.holo_circle);
        Drawable solidCircle = ContextCompat.getDrawable(context, R.drawable.solid_circle);

        holoCircle.setColorFilter(selectedDateColor, PorterDuff.Mode.SRC_ATOP);
        solidCircle.setColorFilter(todayDateColor, PorterDuff.Mode.SRC_ATOP);
        // solidCircle.mutate().setAlpha(200);
        //holoCircle.mutate().setAlpha(200);

        DateTime calendarStartDate = DateTime.now();
        if (selectedDateTime != null && !selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) { //当前时间未选中
            if (dateTime.toLocalDate().isBefore(calendarStartDate.toLocalDate())) {//在今天之前
                dayTextView.setTextColor(context.getResources().getColor(R.color.default_light_gray));
                dayTextView.setBackground(null);
            } else if (dateTime.toLocalDate().isAfter(calendarStartDate.toLocalDate())) {//今天之后
                dayTextView.setTextColor(textColor);
                dayTextView.setBackground(null);
            } else {//就是今天
                dayTextView.setBackground(holoCircle);
                dayTextView.setTextColor(this.todayDateTextColor);
            }
        } else if (selectedDateTime != null && selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) {//当前时间选中
            dayTextView.setBackground(solidCircle);
            dayTextView.setTextColor(Color.WHITE);
        }
        float size = textSize;
        if (size == -1)
            size = dayTextView.getTextSize();
        Log.v("jessie", dayTextView.getText().toString());
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    @Override
    public void drawFlag(TextView flagText, DateTime dateTime, List<String> flagDates) {
        if (flagDates != null) {
            Iterator<String> it = flagDates.iterator();
            DateTime calendarStartDate = DateTime.now();
            while (it.hasNext()) {
                String date = it.next();
                String curDate = OtherUtils.formatDate(dateTime.toDate(), "yyyy-MM-dd");
                if (curDate.equals(date)) {//当前为目标日期
                    if (dateTime.toLocalDate().isBefore(calendarStartDate.toLocalDate())) {//已经过了
                        flagText.setBackgroundColor(context.getResources().getColor(R.color.default_light_gray));
                    } else {//还没有过
                        flagText.setBackgroundColor(context.getResources().getColor(R.color.default_orange));
                    }
                    flagText.setVisibility(View.VISIBLE);
                }
            }
        }

    }

}
