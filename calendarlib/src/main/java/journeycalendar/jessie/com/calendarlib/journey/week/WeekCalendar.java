package journeycalendar.jessie.com.calendarlib.journey.week;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.util.List;

import journeycalendar.jessie.com.calendarlib.R;
import journeycalendar.jessie.com.calendarlib.journey.week.decorator.DayDecorator;
import journeycalendar.jessie.com.calendarlib.journey.week.decorator.DefaultDayDecorator;
import journeycalendar.jessie.com.calendarlib.journey.week.eventbus.BusProvider;
import journeycalendar.jessie.com.calendarlib.journey.week.eventbus.Event;
import journeycalendar.jessie.com.calendarlib.journey.week.listener.OnDateClickListener;
import journeycalendar.jessie.com.calendarlib.journey.week.listener.OnWeekChangeListener;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.DensityUtil;
import journeycalendar.jessie.com.calendarlib.journey.week.view.WeekPager;


/**
 * Created by nor on 12/6/2015.
 */
public class WeekCalendar extends LinearLayout {
    private static final String TAG = "WeekCalendarTAG";
    private OnDateClickListener listener;
    private TypedArray typedArray;
    private GridView daysName;
    private DayDecorator dayDecorator;
    private OnWeekChangeListener onWeekChangeListener;
    private List<String> flagDates;//标记列表
    private int selectedBgColor = ContextCompat.getColor(getContext(), R.color.colorAccent); //选择背景色
    private int dayTextColorPre = Color.WHITE; //已过去文字颜色
    private int dayTextColorNormal = ContextCompat.getColor(getContext(),R.color.default_light_gray); //未来文字颜色
    private float daysTextSize = -1; //文字大小
    private int todayDateTextColor = ContextCompat.getColor(getContext(), R.color.default_blue);//今日文字颜色
    private int selectedTextColor = ContextCompat.getColor(getContext(), R.color.white);//选择文字颜色
    private int flagPreBgColor = ContextCompat.getColor(getContext(), R.color.default_light_gray);//标记已过背景色
    private int flagNormalBgColor = ContextCompat.getColor(getContext(), R.color.default_orange);//标记未来背景色
    private int flagTextColor = ContextCompat.getColor(getContext(), R.color.white);//标记文字颜色
    private String flagTextStr="行";//标记文字
    private boolean drawRoundRect = false;//今日与选择日是否为矩形
    public void setFlagList(List<String> flagDates){
        this.flagDates=flagDates;
        BusProvider.getInstance().post(new Event.InvalidateEvent());
    }


    public WeekCalendar(Context context) {
        super(context);
        init(null);
    }

    public WeekCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public WeekCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    /**
     * 动态创建实例化
     * @param context
     * @return
     */
    public static WeekCalendar newInstance(Context context){
        WeekCalendar weekCalendar=new WeekCalendar(context);
        weekCalendar.init(null);
        return weekCalendar;
    }

    /**
     * 应用动态创建样式
     */
    public WeekCalendar build(){
        setDayDecorator(new DefaultDayDecorator(getContext(),
                selectedBgColor,
                selectedTextColor,
                todayDateTextColor,
                dayTextColorPre,
                dayTextColorNormal,
                flagPreBgColor,
                flagNormalBgColor,
                flagTextColor,
                flagTextStr,
                daysTextSize,
                drawRoundRect));
        return this;
    }

    public WeekCalendar setSelectedBgColor(int selectedBgColor) {
        this.selectedBgColor = selectedBgColor;
        return this;
    }

    public WeekCalendar setDayTextColorPre(int dayTextColorPre) {
        this.dayTextColorPre = dayTextColorPre;
        return this;
    }

    public WeekCalendar setDayTextColorNormal(int dayTextColorNormal) {
        this.dayTextColorNormal = dayTextColorNormal;
        return this;
    }

    public WeekCalendar setDaysTextSize(float daysTextSize) {
        this.daysTextSize = daysTextSize;
        return this;
    }

    public WeekCalendar setTodayDateTextColor(int todayDateTextColor) {
        this.todayDateTextColor = todayDateTextColor;
        return this;
    }

    public WeekCalendar setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return this;
    }

    public WeekCalendar setFlagPreBgColor(int flagPreBgColor) {
        this.flagPreBgColor = flagPreBgColor;
        return this;
    }

    public WeekCalendar setFlagNormalBgColor(int flagNormalBgColor) {
        this.flagNormalBgColor = flagNormalBgColor;
        return this;
    }

    public WeekCalendar setFlagTextColor(int flagTextColor) {
        this.flagTextColor = flagTextColor;
        return this;
    }

    public WeekCalendar setFlagTextStr(String flagTextStr) {
        this.flagTextStr = flagTextStr;
        return this;
    }

    public WeekCalendar setDrawRoundRect(boolean drawRoundRect) {
        this.drawRoundRect = drawRoundRect;
        return this;
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
            selectedBgColor = typedArray.getColor(R.styleable
                    .WeekCalendar_selectedBgColor_week, ContextCompat.getColor(getContext(), R.color
                    .colorAccent));
            dayTextColorPre = typedArray.getColor(R.styleable
                    .WeekCalendar_previousTextColor_week, Color.WHITE);
            dayTextColorNormal = typedArray.getColor(R.styleable.WeekCalendar_normalTextColor_week,ContextCompat.getColor(getContext(),R.color.default_light_gray));
            daysTextSize = typedArray.getDimension(R.styleable
                    .WeekCalendar_dayTextSize_week, -1);
            todayDateTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_todayTextColor_week, ContextCompat.getColor(getContext(), R.color.default_blue));
            selectedTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_selectedTextColor_week, ContextCompat.getColor(getContext(), R.color.white));
            flagPreBgColor=typedArray.getColor(R.styleable.WeekCalendar_flagPreBgColor_week,ContextCompat.getColor(getContext(), R.color.default_light_gray));
            flagNormalBgColor=typedArray.getColor(R.styleable.WeekCalendar_flagNormalBgColor_week,ContextCompat.getColor(getContext(), R.color.default_orange));
            flagTextColor=typedArray.getColor(R.styleable.WeekCalendar_flagTextColor_week,ContextCompat.getColor(getContext(), R.color.white));
            flagTextStr=typedArray.getString(R.styleable.WeekCalendar_flagTextStr_week);
            if(TextUtils.isEmpty(flagTextStr)){
                flagTextStr="行";
            }
            drawRoundRect = typedArray.getBoolean(R.styleable.WeekCalendar_isRoundRect_week,false);
            setDayDecorator(new DefaultDayDecorator(getContext(),
                    selectedBgColor,
                    selectedTextColor,
                    todayDateTextColor,
                    dayTextColorPre,
                    dayTextColorNormal,
                    flagPreBgColor,
                    flagNormalBgColor,
                    flagTextColor,
                    flagTextStr,
                    daysTextSize,
                    drawRoundRect));
        }
        setOrientation(VERTICAL);

        if (!typedArray.getBoolean(R.styleable.WeekCalendar_hideWeekNum, false)) {
            daysName = getDaysNames();
            daysName.setGravity(Gravity.CENTER);
            addView(daysName);
        }
        RelativeLayout.LayoutParams lpWeek = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(getContext(),92)- DensityUtil.dip2px(getContext(),92)/3);
        lpWeek.addRule(RelativeLayout.CENTER_IN_PARENT);
        WeekPager weekPager = new WeekPager(getContext(), attrs);
        addView(weekPager,lpWeek);
    }

    @Subscribe
    public void onDateClick(Event.OnDateClickEvent event) {
        if (listener != null)
            listener.onDateClick(event.getDateTime());
    }

    @Subscribe
    public void onDayDecorate(Event.OnDayDecorateEvent event) {
        if (dayDecorator != null) {
            dayDecorator.decorate(event.getView(), event.getDayTextView(), event.getDateTime(),
                    event.getFirstDay(), event.getSelectedDateTime());
            dayDecorator.drawFlag(event.getFlagText(),event.getDateTime(),flagDates);
        }
    }

    @Subscribe
    public void onWeekChange(Event.OnWeekChange event) {
        if (onWeekChangeListener != null) {
            onWeekChangeListener.onWeekChange(event.getFirstDayOfTheWeek(), event.isForward());
        }
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void setDayDecorator(DayDecorator decorator) {
        this.dayDecorator = decorator;
    }

    public void setOnWeekChangeListener(OnWeekChangeListener onWeekChangeListener) {
        this.onWeekChangeListener = onWeekChangeListener;
    }

    private GridView getDaysNames() {

        daysName = new GridView(getContext());
        daysName.setSelector(new StateListDrawable());
        daysName.setNumColumns(7);

        daysName.setAdapter(new BaseAdapter() {
            private String[] days = {"日","一","二","三","四","五","六"};

            public int getCount() {
                return days.length;
            }

            @Override
            public String getItem(int position) {
                return days[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @SuppressLint("InflateParams")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.week_day_grid_item, null);
                }
                TextView day = (TextView) convertView.findViewById(R.id.daytext);
                day.setText(days[position]);
                if (typedArray != null) {
                    day.setTextColor(typedArray.getColor(R.styleable.WeekCalendar_weekTextColor,
                            Color.WHITE));
                    day.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable
                            .WeekCalendar_weekTextSize, day.getTextSize()));
                }
                return convertView;
            }
        });
        return daysName;
    }

    public void reset() {
        BusProvider.getInstance().post(new Event.ResetEvent());
    }

    public void setSelectedDate(DateTime selectedDate) {
        BusProvider.getInstance().post(new Event.SetSelectedDateEvent(selectedDate));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }
}
