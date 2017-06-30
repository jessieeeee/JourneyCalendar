/***********************************************************************************
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2014 Robin Chutaux
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package journeycalendar.jessie.com.calendarlib.journey.all;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import journeycalendar.jessie.com.calendarlib.journey.DateUtil;

import static journeycalendar.jessie.com.calendarlib.journey.DateUtil.getFirstMonth;
import static journeycalendar.jessie.com.calendarlib.journey.DateUtil.getLastMonth;
import static journeycalendar.jessie.com.calendarlib.journey.DateUtil.getStartYear;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> implements MonthView.OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
    private final Context mContext;
    private final MonthCalendarController mController;
    private final Calendar calendar;
    private CalendarDay selectedDay;
    private final Integer firstMonth;
    private final Integer lastMonth;
    private List<String> dates;
    private int count= DateUtil.getPreMonthNum()+DateUtil.getNextMonthNum()+1;


    public MonthAdapter(Context context, MonthCalendarController monthCalendarController, TypedArray typedArray) {
        this.typedArray = typedArray;
        calendar = Calendar.getInstance();
        firstMonth = getFirstMonth();
        lastMonth = getLastMonth();
        mContext = context;
        mController = monthCalendarController;
        init();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final MonthView monthView = new MonthView(mContext, typedArray);
        return new ViewHolder(monthView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final MonthView v = viewHolder.monthView;
        final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
        int month;
        int year;

        month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        year = position / MONTHS_IN_YEAR + getStartYear() + ((firstMonth + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);

        int selectedFirstDay = (selectedDay==null?-1:selectedDay.day);
        int selectedFirstMonth = (selectedDay==null?-1:selectedDay.month);
        int selectedFirstYear = (selectedDay==null?-1:selectedDay.year);

        v.reuse();
        drawingParams.put(MonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, selectedFirstYear);
        drawingParams.put(MonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, selectedFirstMonth);
        drawingParams.put(MonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, selectedFirstDay);

        drawingParams.put(MonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(MonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(MonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
        if(dates!=null){
            v.setFlagDates(dates);
        }
        v.invalidate();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return count;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final MonthView monthView;

        public ViewHolder(View itemView, MonthView.OnDayClickListener onDayClickListener) {
            super(itemView);
            monthView = (MonthView) itemView;
            monthView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            monthView.setClickable(true);
            monthView.setOnDayClickListener(onDayClickListener);
        }
    }

    protected void init() {
        onDayTapped(new CalendarDay(System.currentTimeMillis()));
    }

    public void onDayClick(MonthView monthView, CalendarDay calendarDay) {
        if (calendarDay != null) {
            onDayTapped(calendarDay);
        }
    }

    //设置flag标记日期
    public void setFlagDates(List<String> flagDates){
        this.dates=flagDates;
        notifyDataSetChanged();
    }


    protected void onDayTapped(CalendarDay calendarDay) {
        mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
        setSelectedDay(calendarDay);
    }

    public void setSelectedDay(CalendarDay calendarDay) {
        this.selectedDay=calendarDay;
        notifyDataSetChanged();
    }

}