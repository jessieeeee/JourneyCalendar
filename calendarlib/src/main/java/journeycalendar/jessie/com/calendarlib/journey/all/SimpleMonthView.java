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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import journeycalendar.jessie.com.calendarlib.R;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.DensityUtil;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.OtherUtils;

class SimpleMonthView extends View {

    public static final String VIEW_PARAMS_HEIGHT = "height";
    public static final String VIEW_PARAMS_MONTH = "month";
    public static final String VIEW_PARAMS_YEAR = "year";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_DAY = "selected_begin_day";
//    public static final String VIEW_PARAMS_SELECTED_LAST_DAY = "selected_last_day";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_MONTH = "selected_begin_month";
//    public static final String VIEW_PARAMS_SELECTED_LAST_MONTH = "selected_last_month";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_YEAR = "selected_begin_year";
//    public static final String VIEW_PARAMS_SELECTED_LAST_YEAR = "selected_last_year";
    public static final String VIEW_PARAMS_WEEK_START = "week_start";

    private static final int SELECTED_CIRCLE_ALPHA = 128;
    protected  int DEFAULT_HEIGHT = DensityUtil.dip2px(getContext(),16);
    protected static final int DEFAULT_NUM_ROWS = 6;
    protected static int DAY_SELECTED_CIRCLE_SIZE;
    protected int DAY_SEPARATOR_WIDTH = DensityUtil.dip2px(getContext(),33);
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;
    protected int MIN_HEIGHT = DensityUtil.dip2px(getContext(),3);
    protected static int MONTH_DAY_LABEL_TEXT_SIZE;
    protected static int MONTH_HEADER_SIZE;
    protected static int MONTH_LABEL_TEXT_SIZE;

    protected int mPadding = dip2px(getContext(),10);

    private String mDayOfWeekTypeface;
    private String mMonthTitleTypeface;
    protected Paint flagTextPaint;//标记字颜色
    protected Paint flagBgPaint;//标记背景颜色
    protected Paint mMonthDayLabelPaint;
    protected Paint mMonthNumPaint;
    protected Paint mMonthTitleBGPaint;
    protected Paint mMonthTitlePaint;
    protected Paint todayCiclePaint;//今天圆圈
    protected Paint mSelectedCirclePaint;
    protected int mCurrentDayTextColor;
    protected int mMonthTextColor;
    protected int mDayTextColor;
    protected int mDayNumColor;
    protected int mMonthTitleBGColor;
    protected int mPreviousDayColor;
    protected int mSelectedDaysColor;
    protected int flagBgColor;//标记背景色
    protected int flagTextColor;//标记文字色
    private final StringBuilder mStringBuilder;

    protected boolean mHasToday = false;
    protected boolean mIsPrev = false;
    protected int mSelectedBeginDay = -1;
    protected int mSelectedBeginMonth = -1;
    protected int mSelectedBeginYear = -1;
    private List<String> dates;
    protected int mToday = -1;
    protected int mWeekStart = 1;
    protected int mNumDays = 7;
    protected int mNumCells = mNumDays;
    private int mDayOfWeekStart = 0;
    protected int mMonth;
    protected Boolean mDrawRect;
    protected int mRowHeight = DEFAULT_HEIGHT;
    protected int mWidth;
    protected int mYear;
    final Time today;

    private final Calendar mCalendar;
    private final Calendar mDayLabelCalendar;
    private final Boolean isPrevDayEnabled;

    private int mNumRows = DEFAULT_NUM_ROWS;

    private DateFormatSymbols mDateFormatSymbols = new DateFormatSymbols();

    private OnDayClickListener mOnDayClickListener;

    public SimpleMonthView(Context context, TypedArray typedArray) {
        super(context);

        Resources resources = context.getResources();
        mDayLabelCalendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mDayOfWeekTypeface = resources.getString(R.string.sans_serif);
        mMonthTitleTypeface = resources.getString(R.string.sans_serif);
        mCurrentDayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorCurrentDay, resources.getColor(R.color.normal_day));
        mMonthTextColor = typedArray.getColor(R.styleable.DayPickerView_colorMonthName, resources.getColor(R.color.normal_day));
        mDayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorDayName, resources.getColor(R.color.normal_day));
        mDayNumColor = typedArray.getColor(R.styleable.DayPickerView_colorNormalDay, resources.getColor(R.color.normal_day));
        mPreviousDayColor = typedArray.getColor(R.styleable.DayPickerView_colorPreviousDay, resources.getColor(R.color.normal_day));
        mSelectedDaysColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayBackground, resources.getColor(R.color.selected_day_background));
        mMonthTitleBGColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayText, resources.getColor(R.color.selected_day_text));
        flagTextColor= Color.WHITE;
        flagBgColor=getResources().getColor(R.color.default_orange);
        mDrawRect = typedArray.getBoolean(R.styleable.DayPickerView_drawRoundRect, false);

        mStringBuilder = new StringBuilder(50);

        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeDay, resources.getDimensionPixelSize(R.dimen.text_size_day));
        MONTH_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeMonth, resources.getDimensionPixelSize(R.dimen.text_size_month));
        MONTH_DAY_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeDayName, resources.getDimensionPixelSize(R.dimen.text_size_day_name));
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.DayPickerView_headerMonthHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));
        DAY_SELECTED_CIRCLE_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_selectedDayRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_radius));

        mRowHeight = ((typedArray.getDimensionPixelSize(R.styleable.DayPickerView_calendarHeight, resources.getDimensionPixelOffset(R.dimen.calendar_height)) - MONTH_HEADER_SIZE) / 6);

        isPrevDayEnabled = typedArray.getBoolean(R.styleable.DayPickerView_enablePreviousDay, true);

        initView();

    }

    //设置flag标记日期
    public void setFlagDates(List<String> flagDates){
        this.dates=flagDates;
    }

    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + mNumCells) / mNumDays;
        int remainder = (offset + mNumCells) % mNumDays;
        return (dividend + (remainder > 0 ? 1 : 0));
    }

    private void drawMonthDayLabels(Canvas canvas) {
        int y = MONTH_HEADER_SIZE - (MONTH_DAY_LABEL_TEXT_SIZE / 2);
        int dayWidthHalf = (mWidth - mPadding * 2) / (mNumDays * 2);

        for (int i = 0; i < mNumDays; i++) {
            int calendarDay = (i + mWeekStart) % mNumDays;
            int x = (2 * i + 1) * dayWidthHalf + mPadding;
            mDayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            canvas.drawText(mDateFormatSymbols.getShortWeekdays()[mDayLabelCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(Locale.getDefault()), x, y, mMonthDayLabelPaint);
        }
    }

    //绘制每月标题
    private void drawMonthTitle(Canvas canvas) {
        StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        Rect rect=new Rect();
        mMonthTitlePaint.getTextBounds(String.valueOf(stringBuilder),0,stringBuilder.length(),rect);
        int x = mWidth / mNumDays -mPadding;
        int y = (MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) / 2 ;
        canvas.drawLine(0, y-rect.height()-MIN_HEIGHT, mWidth, y-rect.height()-MIN_HEIGHT, mMonthDayLabelPaint);
        canvas.drawText(stringBuilder.toString(), x, y, mMonthTitlePaint);
        canvas.drawLine(0, y+MIN_HEIGHT, mWidth, y+MIN_HEIGHT, mMonthDayLabelPaint);
    }

    private int findDayOffset() {
        return (mDayOfWeekStart < mWeekStart ? (mDayOfWeekStart + mNumDays) : mDayOfWeekStart)
            - mWeekStart;
    }

    private String getMonthAndYearString() {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        mStringBuilder.setLength(0);
        long millis = mCalendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }

    private void onDayClick(SimpleMonthAdapter.CalendarDay calendarDay) {
        if (mOnDayClickListener != null && (isPrevDayEnabled || !((calendarDay.month == today.month) && (calendarDay.year == today.year) && calendarDay.day < today.monthDay))) {
            mOnDayClickListener.onDayClick(this, calendarDay);
        }
    }

    private boolean sameDay(int monthDay, Time time) {
        return (mYear == time.year) && (mMonth == time.month) && (monthDay == time.monthDay);
    }

    private boolean prevDay(int monthDay, Time time) {
        return ((mYear < time.year)) || (mYear == time.year && mMonth < time.month) || (mMonth == time.month && monthDay < time.monthDay);
    }

    //绘制日期
    protected void drawMonthNums(Canvas canvas) {

        int y = (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
        int paddingDay = (mWidth - 2 * mPadding) / (2 * mNumDays);
        int dayOffset = findDayOffset();
        int day = 1;

        while (day <= mNumCells) {
            int x = paddingDay * (1 + dayOffset * 2) + mPadding;

            //天数
            String dayStr= String.format("%d", day);
            //标记
            String flag="行";
            //测量字符串天数
            Rect dayRect=new Rect();
            mMonthTitlePaint.getTextBounds(dayStr,0,dayStr.length(),dayRect);
            //测量字符串标记
            Rect flagRect=new Rect();
            mMonthTitlePaint.getTextBounds(flag,0,flag.length(),flagRect);

            //已经过了的日期
            if (isPrevDayEnabled && prevDay(day, today)) {
                mMonthNumPaint.setColor(mPreviousDayColor);
                if(isFlag(mYear,mMonth,day)){
                    //绘制方形背景
                    canvas.drawRect(x-flagRect.width()/2-MIN_HEIGHT/2,y+DEFAULT_HEIGHT-MIN_HEIGHT/2,x+flagRect.width()/2+MIN_HEIGHT/2,y+flagRect.height()+DEFAULT_HEIGHT+MIN_HEIGHT/2,mMonthNumPaint);
                    //绘制字体
                    canvas.drawText(flag, x-flagRect.width()/2-MIN_HEIGHT/2, y+flagRect.height()+DEFAULT_HEIGHT, flagTextPaint);
                }
            }else{
                //当前日期是今天
                if (mHasToday && (mToday == day)) {
                    mMonthNumPaint.setColor(getResources().getColor(R.color.default_blue));
                    mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE, todayCiclePaint);
                } else {  //不是今天
                    mMonthNumPaint.setColor(mDayNumColor);
                    mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                if(isFlag(mYear,mMonth,day)){
                    //绘制方形背景
                    canvas.drawRect(x-flagRect.width()/2-MIN_HEIGHT/2,y+DEFAULT_HEIGHT-MIN_HEIGHT/2,x+flagRect.width()/2+MIN_HEIGHT/2,y+flagRect.height()+DEFAULT_HEIGHT+MIN_HEIGHT/2,flagBgPaint);
                    //绘制字体
                    canvas.drawText(flag, x-flagRect.width()/2-MIN_HEIGHT/2, y+flagRect.height()+DEFAULT_HEIGHT, flagTextPaint);
                }

            }
            //选择的日期
            if ((mMonth == mSelectedBeginMonth && mSelectedBeginDay == day && mSelectedBeginYear == mYear) ) {
                if (mDrawRect) {
                    RectF rectF = new RectF(x - DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE, x + DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
                    canvas.drawRoundRect(rectF, 10.0f, 10.0f, mSelectedCirclePaint);
                } else{
                    mMonthNumPaint.setColor(flagTextColor);
                    canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE, mSelectedCirclePaint);
                }
            }

            canvas.drawText(dayStr, x, y, mMonthNumPaint);
            dayOffset++;
            if (dayOffset == mNumDays) {
                dayOffset = 0;
                y += mRowHeight;
            }
            day++;
        }
    }

    //是否需要绘制标记
    private boolean isFlag(int year,int month,int day){
        if(dates!=null){
            //遍历当前天是否需要绘制
            Calendar c = Calendar.getInstance();//获取一个日历实例
            c.set(year, month, day);//设定日历的日期
            Date curDay=c.getTime();
            Iterator<String> it=dates.iterator();
            while(it.hasNext()){
                String date=it.next();
                String curDayStr=OtherUtils.formatDate(curDay,"yyyy-MM-dd");
                if(date.equals(curDayStr)){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }

    }

    public SimpleMonthAdapter.CalendarDay getDayFromLocation(float x, float y) {
        int padding = mPadding;
        if ((x < padding) || (x > mWidth - mPadding)) {
            return null;
        }
//        (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
        int yDay = (int) (y - MONTH_HEADER_SIZE + DAY_SEPARATOR_WIDTH) / mRowHeight;
        int day = 1 + ((int) ((x - padding) * mNumDays / (mWidth - padding - mPadding)) - findDayOffset()) + yDay * mNumDays;

        if (mMonth > 11 || mMonth < 0 || CalendarUtils.getDaysInMonth(mMonth, mYear) < day || day < 1)
            return null;

        return new SimpleMonthAdapter.CalendarDay(mYear, mMonth, day);
    }

    protected void initView() {
        mMonthTitlePaint = new Paint();
        mMonthTitlePaint.setAntiAlias(true);
        mMonthTitlePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        mMonthTitlePaint.setTypeface(Typeface.create(mMonthTitleTypeface, Typeface.BOLD));
        mMonthTitlePaint.setColor(mMonthTextColor);
        mMonthTitlePaint.setTextAlign(Align.CENTER);
        mMonthTitlePaint.setStyle(Style.FILL);

        mMonthTitleBGPaint = new Paint();
        mMonthTitleBGPaint.setFakeBoldText(true);
        mMonthTitleBGPaint.setAntiAlias(true);
        mMonthTitleBGPaint.setColor(mMonthTitleBGColor);
        mMonthTitleBGPaint.setTextAlign(Align.CENTER);
        mMonthTitleBGPaint.setStyle(Style.FILL);

        mSelectedCirclePaint = new Paint();
        mSelectedCirclePaint.setFakeBoldText(true);
        mSelectedCirclePaint.setAntiAlias(true);
        mSelectedCirclePaint.setColor(mSelectedDaysColor);
        mSelectedCirclePaint.setTextAlign(Align.CENTER);
        mSelectedCirclePaint.setStyle(Style.FILL);

        mMonthDayLabelPaint = new Paint();
        mMonthDayLabelPaint.setAntiAlias(true);
        mMonthDayLabelPaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
        mMonthDayLabelPaint.setColor(mDayTextColor);
        mMonthDayLabelPaint.setTypeface(Typeface.create(mDayOfWeekTypeface, Typeface.NORMAL));
        mMonthDayLabelPaint.setStyle(Style.FILL);
        mMonthDayLabelPaint.setTextAlign(Align.CENTER);
        mMonthNumPaint = new Paint();
        mMonthNumPaint.setAntiAlias(true);
        mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        mMonthNumPaint.setStyle(Style.FILL);
        mMonthNumPaint.setTextAlign(Align.CENTER);
        mMonthNumPaint.setFakeBoldText(false);

        flagTextPaint=new Paint();
        flagTextPaint.setAntiAlias(true);
        flagTextPaint.setColor(flagTextColor);
        flagTextPaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
        flagBgPaint=new Paint();
        flagBgPaint.setAntiAlias(true);
        flagBgPaint.setColor(flagBgColor);

        todayCiclePaint=new Paint();
        todayCiclePaint.setAntiAlias(true);
        todayCiclePaint.setStrokeWidth(4);
        todayCiclePaint.setColor(mSelectedDaysColor);
        todayCiclePaint.setStyle(Style.STROKE);
    }

    protected void onDraw(Canvas canvas) {
        drawMonthTitle(canvas);
//        drawMonthDayLabels(canvas);
        drawMonthNums(canvas);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows + MONTH_HEADER_SIZE);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            SimpleMonthAdapter.CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
            if (calendarDay != null) {
                onDayClick(calendarDay);
            }
        }
        return true;
    }

    public void reuse() {
        mNumRows = DEFAULT_NUM_ROWS;
        requestLayout();
    }

    public void setMonthParams(HashMap<String, Integer> params) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            throw new InvalidParameterException("You must specify month and year for this view");
        }
        setTag(params);

        if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
            mRowHeight = params.get(VIEW_PARAMS_HEIGHT);
            if (mRowHeight < MIN_HEIGHT) {
                mRowHeight = MIN_HEIGHT;
            }
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_DAY)) {
            mSelectedBeginDay = params.get(VIEW_PARAMS_SELECTED_BEGIN_DAY);
        }
//        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_DAY)) {
//            mSelectedLastDay = params.get(VIEW_PARAMS_SELECTED_LAST_DAY);
//        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_MONTH)) {
            mSelectedBeginMonth = params.get(VIEW_PARAMS_SELECTED_BEGIN_MONTH);
        }
//        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_MONTH)) {
//            mSelectedLastMonth = params.get(VIEW_PARAMS_SELECTED_LAST_MONTH);
//        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_YEAR)) {
            mSelectedBeginYear = params.get(VIEW_PARAMS_SELECTED_BEGIN_YEAR);
        }
//        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_YEAR)) {
//            mSelectedLastYear = params.get(VIEW_PARAMS_SELECTED_LAST_YEAR);
//        }

        mMonth = params.get(VIEW_PARAMS_MONTH);
        mYear = params.get(VIEW_PARAMS_YEAR);

        mHasToday = false;
        mToday = -1;

        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);

        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            mWeekStart = params.get(VIEW_PARAMS_WEEK_START);
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }

        mNumCells = CalendarUtils.getDaysInMonth(mMonth, mYear);
        for (int i = 0; i < mNumCells; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mHasToday = true;
                mToday = day;
            }

            mIsPrev = prevDay(day, today);
        }

        mNumRows = calculateNumRows();
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    public interface OnDayClickListener {
        void onDayClick(SimpleMonthView simpleMonthView, SimpleMonthAdapter.CalendarDay calendarDay);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        int scale = (int) context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}