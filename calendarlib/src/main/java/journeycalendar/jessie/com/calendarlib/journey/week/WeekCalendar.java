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

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
            int selectedBgColor = typedArray.getColor(R.styleable
                    .WeekCalendar_selectedBgColor_weekview, ContextCompat.getColor(getContext(), R.color
                    .colorAccent));
            int dayTextColorPre = typedArray.getColor(R.styleable
                    .WeekCalendar_previousTextColor_weekview, Color.WHITE);
            int dayTextColorNormal = typedArray.getColor(R.styleable.WeekCalendar_normalTextColor_weekview,ContextCompat.getColor(getContext(),R.color.default_light_gray));
            float daysTextSize = typedArray.getDimension(R.styleable
                    .WeekCalendar_dayTextSize_weekview, -1);
            int todayDateTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_todayTextColor_weekview, ContextCompat.getColor(getContext(), R.color.default_blue));
            int selectedTextColor = typedArray.getColor(R.styleable
                    .WeekCalendar_selectedTextColor_weekview, ContextCompat.getColor(getContext(), R.color.white));
            int flagPreBgColor=typedArray.getColor(R.styleable.WeekCalendar_flagPreBgColor_weekview,ContextCompat.getColor(getContext(), R.color.default_light_gray));
            int flagNormalBgColor=typedArray.getColor(R.styleable.WeekCalendar_flagNormalBgColor_weekview,ContextCompat.getColor(getContext(), R.color.default_orange));
            int flagTextColor=typedArray.getColor(R.styleable.WeekCalendar_flagTextColor_weekview,ContextCompat.getColor(getContext(), R.color.white));
            String flagTextStr=typedArray.getString(R.styleable.WeekCalendar_flagTextStr_weekview);
            if(TextUtils.isEmpty(flagTextStr)){
                flagTextStr="行";
            }
            boolean drawRoundRect = typedArray.getBoolean(R.styleable.WeekCalendar_isRoundRect_weekview,false);
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
