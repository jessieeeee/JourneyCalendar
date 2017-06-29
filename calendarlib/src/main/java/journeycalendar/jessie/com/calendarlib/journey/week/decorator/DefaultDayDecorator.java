package journeycalendar.jessie.com.calendarlib.journey.week.decorator;

import android.annotation.TargetApi;
import android.content.Context;
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
    private final int selectedBgColor;
    private final int selectedTextColor;
    private int todayDateTextColor;
    private int textColorPre;
    private int textColorNormal;
    private float textSize;
    private boolean drawRoundRect;
    private String flagTextStr;
    private int flagPreBgColor;
    private int flagNormalBgColor;
    private int flagTextColor;

    public DefaultDayDecorator(Context context,
                               @ColorInt int selectedBgColor,
                               @ColorInt int selectedTextColor,
                               @ColorInt int todayDateTextColor,
                               @ColorInt int textColorPre,
                               @ColorInt int textColorNormal,
                               @ColorInt int flagPreBgColor,
                               @ColorInt int flagNormalBgColor,
                               @ColorInt int flagTextColor,
                               String flagTextStr,
                               float textSize,
                               boolean drawRoundRect) {
        this.context = context;
        this.selectedBgColor = selectedBgColor;
        this.selectedTextColor = selectedTextColor;
        this.todayDateTextColor = todayDateTextColor;
        this.textColorPre = textColorPre;
        this.textColorNormal = textColorNormal;
        this.textSize = textSize;
        this.drawRoundRect = drawRoundRect;
        this.flagPreBgColor=flagPreBgColor;
        this.flagNormalBgColor=flagNormalBgColor;
        this.flagTextColor=flagTextColor;
        this.flagTextStr=flagTextStr;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void decorate(View view, TextView dayTextView,
                         DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime) {
        //DateTime dt = new DateTime();
        Drawable todayShape,selectShape;
        if(drawRoundRect){
            todayShape = ContextCompat.getDrawable(context, R.drawable.today_rect);
            selectShape = ContextCompat.getDrawable(context, R.drawable.select_rect);
        }else{
            todayShape = ContextCompat.getDrawable(context, R.drawable.today_circle);
            selectShape = ContextCompat.getDrawable(context, R.drawable.select_circle);
        }
        selectShape.setColorFilter(selectedBgColor, PorterDuff.Mode.SRC_ATOP);
        todayShape.setColorFilter(todayDateTextColor, PorterDuff.Mode.SRC_ATOP);

        // solidCircle.mutate().setAlpha(200);
        //holoCircle.mutate().setAlpha(200);

        DateTime calendarStartDate = DateTime.now();
        if (selectedDateTime != null && !selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) { //当前时间未选中
            if (dateTime.toLocalDate().isBefore(calendarStartDate.toLocalDate())) {//在今天之前
                dayTextView.setTextColor(textColorPre);
                dayTextView.setBackground(null);
            } else if (dateTime.toLocalDate().isAfter(calendarStartDate.toLocalDate())) {//今天之后
                dayTextView.setTextColor(textColorNormal);
                dayTextView.setBackground(null);
            } else {//就是今天
                dayTextView.setBackground(todayShape);
                dayTextView.setTextColor(todayDateTextColor);
            }
        } else if (selectedDateTime != null && selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) {//当前时间选中
            dayTextView.setBackground(selectShape);
            dayTextView.setTextColor(selectedTextColor);
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
                    flagText.setText(flagTextStr);
                    flagText.setTextColor(flagTextColor);
                    if (dateTime.toLocalDate().isBefore(calendarStartDate.toLocalDate())) {//已经过了
                        flagText.setBackgroundColor(flagPreBgColor);
                    } else {//还没有过
                        flagText.setBackgroundColor(flagNormalBgColor);
                    }
                    flagText.setVisibility(View.VISIBLE);

                }
            }
        }

    }

}
