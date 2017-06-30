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
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import journeycalendar.jessie.com.calendarlib.R;
import journeycalendar.jessie.com.calendarlib.journey.DateUtil;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.DensityUtil;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.OtherUtils;

class MonthView extends View {

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

    protected  int DEFAULT_HEIGHT = DensityUtil.dip2px(getContext(),16);
    protected static final int DEFAULT_NUM_ROWS = 6;
    protected static int DAY_SELECTED_CIRCLE_SIZE;
    protected int DAY_SEPARATOR_WIDTH = DensityUtil.dip2px(getContext(),33);
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;
    protected int MIN_HEIGHT = DensityUtil.dip2px(getContext(),3);
    protected static int MONTH_LINE_SIZE;
    protected static int MONTH_HEADER_SIZE;
    protected static int MONTH_TITLE_TEXT_SIZE;

    protected int mPadding = dip2px(getContext(),10);

    private String mMonthTitleTypeface;//月份标题风格
    protected Paint flagTextPaint;//标记字画笔
    protected Paint flagPreBgPaint;//已过标记背景画笔
    protected Paint flagNormalBgPaint;//标记背景画笔
    protected Paint mMonthLinePaint;//月份标题线画笔
    protected Paint mMonthNumPaint;//日期画笔
    protected Paint mMonthTitlePaint;//月份标题画笔
    protected Paint todayCiclePaint;//今天圆圈画笔
    protected Paint mSelectedCirclePaint;//选择圆圈画笔

    protected int mMonthTitleColor;//月份标题颜色
    protected int mMonthLineColor;//月份标题线颜色
    protected int mNormalDayColor;//日期颜色
    protected int mPreviousDayColor;//已过日期颜色
    protected int mSelectedBgColor;//选择背景颜色
    protected int mSelectedTextColor;//选择文字颜色
    protected int todayTextColor;//今日日期颜色
    protected int flagPreBgColor;//已过标记背景色
    protected int flagNormalBgColor;//标记背景色
    protected int flagTextColor;//标记文字色
    protected String flag; //标记文本
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

    private int mNumRows = DEFAULT_NUM_ROWS;


    private OnDayClickListener mOnDayClickListener;

    public MonthView(Context context, TypedArray typedArray) {
        super(context);

        Resources resources = context.getResources();
        mCalendar = Calendar.getInstance();
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mMonthTitleTypeface = resources.getString(R.string.sans_serif);
        mMonthTitleColor = typedArray.getColor(R.styleable.MonthCalendarView_monthTitleColor, ContextCompat.getColor(context,R.color.normal_day));
        mMonthLineColor = typedArray.getColor(R.styleable.MonthCalendarView_monthLineColor, ContextCompat.getColor(context,R.color.normal_day));
        mNormalDayColor = typedArray.getColor(R.styleable.MonthCalendarView_normalDayTextColor_month, ContextCompat.getColor(context,R.color.normal_day));
        mPreviousDayColor = typedArray.getColor(R.styleable.MonthCalendarView_previousDayTextColor_month, ContextCompat.getColor(context,R.color.normal_day));
        mSelectedBgColor = typedArray.getColor(R.styleable.MonthCalendarView_selectedBgColor_month, ContextCompat.getColor(context,R.color.selected_day_background));
        mSelectedTextColor = typedArray.getColor(R.styleable.MonthCalendarView_selectedTextColor_month, ContextCompat.getColor(context,R.color.white));
        todayTextColor = typedArray.getColor(R.styleable.MonthCalendarView_todayTextColor_month,ContextCompat.getColor(context,R.color.default_blue));
        flagTextColor= typedArray.getColor(R.styleable.MonthCalendarView_flagTextColor_month,ContextCompat.getColor(context,R.color.white));
        flagPreBgColor=typedArray.getColor(R.styleable.MonthCalendarView_flagPreBgColor_month,ContextCompat.getColor(context,R.color.white));
        flagNormalBgColor=typedArray.getColor(R.styleable.MonthCalendarView_flagNormalBgColor_month,ContextCompat.getColor(context,R.color.default_orange));
        mDrawRect = typedArray.getBoolean(R.styleable.MonthCalendarView_isRoundRect_month, false);
        int preMonthNum = typedArray.getInteger(R.styleable.MonthCalendarView_preMonthNum_month,2);
        DateUtil.setPreMonthNum(preMonthNum);
        int nextMonthNum = typedArray.getInteger(R.styleable.MonthCalendarView_nextMonthNum_month,2);
        DateUtil.setNextMonthNum(nextMonthNum);

        flag = typedArray.getString(R.styleable.MonthCalendarView_flagTextStr_month);
        if(TextUtils.isEmpty(flag)){
            flag="行";
        }
        mStringBuilder = new StringBuilder(50);

        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.MonthCalendarView_dayTextSize_month, resources.getDimensionPixelSize(R.dimen.text_size_day));
        MONTH_TITLE_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.MonthCalendarView_monthTitleTextSize, resources.getDimensionPixelSize(R.dimen.text_size_month));
        MONTH_LINE_SIZE = typedArray.getDimensionPixelSize(R.styleable.MonthCalendarView_monthLineTextSize, resources.getDimensionPixelSize(R.dimen.text_size_day_name));
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.MonthCalendarView_headerMonthHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));
        DAY_SELECTED_CIRCLE_SIZE = typedArray.getDimensionPixelSize(R.styleable.MonthCalendarView_selectedDayBgRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_radius));

        mRowHeight = ((typedArray.getDimensionPixelSize(R.styleable.MonthCalendarView_calendarHeight, resources.getDimensionPixelOffset(R.dimen.calendar_height)) - MONTH_HEADER_SIZE) / 6);

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


    //绘制每月标题
    private void drawMonthTitle(Canvas canvas) {
        StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        Rect rect=new Rect();
        mMonthTitlePaint.getTextBounds(String.valueOf(stringBuilder),0,stringBuilder.length(),rect);
        int x = mWidth / mNumDays -mPadding;
        int y = (MONTH_HEADER_SIZE - MONTH_LINE_SIZE) / 2 ;
        canvas.drawLine(0, y-rect.height()-MIN_HEIGHT, mWidth, y-rect.height()-MIN_HEIGHT, mMonthLinePaint);
        canvas.drawText(stringBuilder.toString(), x, y, mMonthTitlePaint);
        canvas.drawLine(0, y+MIN_HEIGHT, mWidth, y+MIN_HEIGHT, mMonthLinePaint);
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

    private void onDayClick(CalendarDay calendarDay) {
        if (mOnDayClickListener != null) {
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
            //测量字符串天数
            Rect dayRect=new Rect();
            mMonthTitlePaint.getTextBounds(dayStr,0,dayStr.length(),dayRect);
            //测量字符串标记
            Rect flagRect=new Rect();
            mMonthTitlePaint.getTextBounds(flag,0,flag.length(),flagRect);

            //已经过了的日期
            if (prevDay(day, today)) {
                mMonthNumPaint.setColor(mPreviousDayColor);
                if(isFlag(mYear,mMonth,day)){
                    //绘制方形背景
                    canvas.drawRect(x-flagRect.width()/2-MIN_HEIGHT/2,y+DEFAULT_HEIGHT-MIN_HEIGHT/2,x+flagRect.width()/2+MIN_HEIGHT/2,y+flagRect.height()+DEFAULT_HEIGHT+MIN_HEIGHT/2,flagPreBgPaint);
                    //绘制字体
                    canvas.drawText(flag, x-flagRect.width()/2-MIN_HEIGHT/2, y+flagRect.height()+DEFAULT_HEIGHT, flagTextPaint);
                }
            }else{
                //当前日期是今天
                if (mHasToday && (mToday == day)) {
                    mMonthNumPaint.setColor(getResources().getColor(R.color.default_blue));
                    mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    if (mDrawRect) {
                        RectF rectF = new RectF(x - DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE, x + DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
                        canvas.drawRoundRect(rectF, 10.0f, 10.0f, todayCiclePaint);
                    }else{
                        canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE, todayCiclePaint);
                    }
                } else {  //不是今天
                    mMonthNumPaint.setColor(mNormalDayColor);
                    mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                if(isFlag(mYear,mMonth,day)){
                    //绘制方形背景
                    canvas.drawRect(x-flagRect.width()/2-MIN_HEIGHT/2,y+DEFAULT_HEIGHT-MIN_HEIGHT/2,x+flagRect.width()/2+MIN_HEIGHT/2,y+flagRect.height()+DEFAULT_HEIGHT+MIN_HEIGHT/2,flagNormalBgPaint);
                    //绘制字体
                    canvas.drawText(flag, x-flagRect.width()/2-MIN_HEIGHT/2, y+flagRect.height()+DEFAULT_HEIGHT, flagTextPaint);
                }

            }
            //选择的日期
            if ((mMonth == mSelectedBeginMonth && mSelectedBeginDay == day && mSelectedBeginYear == mYear) ) {
                mMonthNumPaint.setColor(mSelectedTextColor);
                if (mDrawRect) {
                    RectF rectF = new RectF(x - DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE, x + DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
                    canvas.drawRoundRect(rectF, 10.0f, 10.0f, mSelectedCirclePaint);
                } else{
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

    public CalendarDay getDayFromLocation(float x, float y) {
        int padding = mPadding;
        if ((x < padding) || (x > mWidth - mPadding)) {
            return null;
        }
//        (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
        int yDay = (int) (y - MONTH_HEADER_SIZE + DAY_SEPARATOR_WIDTH) / mRowHeight;
        int day = 1 + ((int) ((x - padding) * mNumDays / (mWidth - padding - mPadding)) - findDayOffset()) + yDay * mNumDays;

        if (mMonth > 11 || mMonth < 0 || CalendarUtils.getDaysInMonth(mMonth, mYear) < day || day < 1)
            return null;

        return new CalendarDay(mYear, mMonth, day);
    }

    protected void initView() {
        mMonthTitlePaint = new Paint();
        mMonthTitlePaint.setAntiAlias(true);
        mMonthTitlePaint.setTextSize(MONTH_TITLE_TEXT_SIZE);
        mMonthTitlePaint.setTypeface(Typeface.create(mMonthTitleTypeface, Typeface.BOLD));
        mMonthTitlePaint.setColor(mMonthTitleColor);
        mMonthTitlePaint.setTextAlign(Align.CENTER);
        mMonthTitlePaint.setStyle(Style.FILL);

        mSelectedCirclePaint = new Paint();
        mSelectedCirclePaint.setFakeBoldText(true);
        mSelectedCirclePaint.setAntiAlias(true);
        mSelectedCirclePaint.setColor(mSelectedBgColor);
        mSelectedCirclePaint.setTextAlign(Align.CENTER);
        mSelectedCirclePaint.setStyle(Style.FILL);

        mMonthLinePaint = new Paint();
        mMonthLinePaint.setAntiAlias(true);
        mMonthLinePaint.setTextSize(MONTH_LINE_SIZE);
        mMonthLinePaint.setColor(mMonthLineColor);
        mMonthLinePaint.setStyle(Style.FILL);
        mMonthLinePaint.setTextAlign(Align.CENTER);
        mMonthNumPaint = new Paint();
        mMonthNumPaint.setAntiAlias(true);
        mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        mMonthNumPaint.setStyle(Style.FILL);
        mMonthNumPaint.setTextAlign(Align.CENTER);
        mMonthNumPaint.setFakeBoldText(false);

        flagTextPaint=new Paint();
        flagTextPaint.setAntiAlias(true);
        flagTextPaint.setColor(flagTextColor);
        flagTextPaint.setTextSize(MONTH_LINE_SIZE);
        flagNormalBgPaint=new Paint();
        flagNormalBgPaint.setAntiAlias(true);
        flagNormalBgPaint.setColor(flagNormalBgColor);

        flagPreBgPaint=new Paint();
        flagPreBgPaint.setAntiAlias(true);
        flagPreBgPaint.setColor(flagPreBgColor);

        todayCiclePaint=new Paint();
        todayCiclePaint.setAntiAlias(true);
        todayCiclePaint.setStrokeWidth(4);
        todayCiclePaint.setColor(todayTextColor);
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
            CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
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
        void onDayClick(MonthView monthView, CalendarDay calendarDay);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        int scale = (int) context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}