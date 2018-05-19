# JourneyCalendar
- ![image](http://oqujmbgen.bkt.clouddn.com/%E5%91%A8%E6%97%A5%E5%8E%86%E4%B8%8E%E6%9C%88%E6%97%A5%E5%8E%86%E7%9A%84%E8%81%94%E5%8A%A8%E5%AE%9E%E7%8E%B0.gif)
## support 
### week and month calendar
### two modes
1. Dynamic setting range
Based on the current month, for example, previous three months and next six months
2. Custom range
such as 2019-1 to 2019-2
## Using library in your application
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
	<pre>
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	</pre>
### Step 2. Add the dependency
<pre>
dependencies {
       implementation 'com.github.jessieeeee:JourneyCalendar:v1.0.2'
	}
</pre>

## How to Use
### Configuration tool
- DateUtil
you should set this before container render
```java
DateUtil.setCurState(DYNAMIC); //Dynamic setting range
// if you set dynamic range, you should set these
setMonthRange(3,6); //previous three months,next six months
DateUtil.setCurState(CUSTOM); //custom range
// if you set custom range, you should set these
DateUtil.setStartYear(2019);
DateUtil.setEndYear(2019);
DateUtil.setStartMonth(1);
DateUtil.setEndMonth(2);
// you can set today , but the call must be after set range (dynamic or custom)
DateUtil.setCurDay(2019,1,12);
```
### attrs list
```xml
<resources>
    <!-- 周日历-->
    <declare-styleable name="WeekCalendar">
        <!--日期文字大小-->
        <attr name="dayTextSize_week" format="dimension"/>
        <!--过去日期颜色-->
        <attr name="previousTextColor_week" format="color"/>
        <!--未来日期颜色-->
        <attr name="normalTextColor_week" format="color" />
        <!--星期文字大小-->
        <attr name="weekTextSize" format="dimension"/>
        <!--星期文字颜色-->
        <attr name="weekTextColor" format="color"/>
        <!--选择星期文字颜色-->
        <attr name="selectedTextColor_week" format="color"/>
        <!--选择背景颜色-->
        <attr name="selectedBgColor_week" format="color"/>
        <!--  是否隐藏星期-->
        <attr name="hideWeekNum"  format="boolean"/>
        <!--今日文字颜色-->
        <attr name="todayTextColor_week" format="color"/>
        <!--今日与选择日是否为矩形-->
        <attr name="isRoundRect_week" format="boolean"/>
        <!--标记文本-->
        <attr name="flagTextStr_week" format="string"/>
        <!--标记文本颜色-->
        <attr name="flagTextColor_week" format="color"/>
        <!--标记文本已过背景颜色-->
        <attr name="flagPreBgColor_week" format="color"/>
        <!--标记文本未来背景颜色-->
        <attr name="flagNormalBgColor_week" format="color"/>
    </declare-styleable>

    <!--月日历-->
    <declare-styleable name="MonthCalendarView">
        <!--标记文本-->
        <attr name="flagTextStr_month" format="string"/>
        <!--今日文字颜色-->
        <attr name="todayTextColor_month" format="color"/>
        <!--选择日期背景颜色-->
        <attr name="selectedBgColor_month" format="color"/>
        <!--选择日期文字-->
        <attr name="selectedTextColor_month" format="color"/>
        <!--过去日期颜色-->
        <attr name="previousDayTextColor_month" format="color"/>
        <!--未来日期颜色-->
        <attr name="normalDayTextColor_month" format="color" />
        <!--月份标题颜色-->
        <attr name="monthTitleColor" format="color" />
        <!--月份标题线颜色-->
        <attr name="monthLineColor" format="color" />
        <!--日期文字大小-->
        <attr name="dayTextSize_month" format="dimension"/>
        <!--月份标题文字大小-->
        <attr name="monthTitleTextSize" format="dimension" />
        <!--月份标题线粗细-->
        <attr name="monthLineTextSize" format="dimension"/>
        <!--月份标题高度-->
        <attr name="headerMonthHeight" format="dimension" />
        <!--选择日背景大小-->
        <attr name="selectedDayBgRadius" format="dimension" />
        <!--日历高度-->
        <attr name="calendarHeight" format="dimension" />
        <!--今日与选择日是否为矩形-->
        <attr name="isRoundRect_month" format="boolean" />
        <!--标记文本颜色-->
        <attr name="flagTextColor_month" format="color"/>
        <!--标记文本已过背景颜色-->
        <attr name="flagPreBgColor_month" format="color"/>
        <!--标记文本未来背景颜色-->
        <attr name="flagNormalBgColor_month" format="color"/>
    </declare-styleable>
</resources>
```
### weekCalendar
- xml
```xml
 <journeycalendar.jessie.com.calendarlib.journey.week.WeekCalendar
             android:id="@+id/weekCalendar"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:gravity="center"
             android:orientation="vertical"
             app:previousTextColor_week="@color/default_light_gray"
             app:normalTextColor_week="@color/default_black"
             app:dayTextSize_week="14sp"
             app:flagTextStr_week="@string/flag"
             app:selectedTextColor_week="@color/white"
             app:selectedBgColor_week="@color/default_blue"
             app:todayTextColor_week="@color/default_blue"
             app:weekTextColor="@color/default_gray"
             app:weekTextSize="12sp"
             app:isRoundRect_week="false"
            >
 
         </journeycalendar.jessie.com.calendarlib.journey.week.WeekCalendar>
```
- java
```java
    weekCalendar = WeekCalendar.newInstance(context)
                  .setSelectedBgColor(ContextCompat.getColor(context, R.color.colorAccent))
                  .setDayTextColorPre( Color.GRAY)
                  .setDayTextColorNormal(ContextCompat.getColor(context, R.color.colorPrimary))
                  .setDaysTextSize(12)
                  .setTodayDateTextColor(Color.RED)
                  .setSelectedTextColor(Color.WHITE)
                  .setFlagPreBgColor(ContextCompat.getColor(context,R.color.default_black))
                  .setFlagNormalBgColor(ContextCompat.getColor(context, R.color.default_blue))
                  .setFlagTextColor(Color.WHITE)
                  .setFlagTextStr("行")
                  .setDrawRoundRect(true)
                  .build();
```
- date click 
```java
weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                clickDateTime=dateTime;
                journey_month.setText(dateTime.getMonthOfYear() + "");
                journey_data_title.setText(OtherUtils.formatDate(dateTime.toDate()));
                journeyListAdapter.setData(setCurJourneyList(dateTime.toDate()));//设置当天的行程数据
            }

        });
  
```
- week change
```java
      weekCalendar.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {
                journey_month.setText(firstDayOfTheWeek.getMonthOfYear() + "");
                Toast.makeText(context, "Week changed: " + firstDayOfTheWeek +
                        " Forward: " + forward, Toast.LENGTH_SHORT).show();
            }
        });
```

- set today
```java
weekCalendar.reset();
```
- set selected date
```java
weekCalendar.setSelectedDate(dateTime);
```
- set bottom flag
```java
 weekCalendar.setFlagList(dates);
```

### monthCalendar
- xml
```xml
<journeycalendar.jessie.com.calendarlib.journey.all.MonthCalendarView
        android:id="@+id/pop_pickerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/default_bg"
        app:normalDayTextColor_month="@color/default_black"
        app:previousDayTextColor_month="@color/default_light_gray"
        app:selectedBgColor_month="@color/default_blue"
        app:selectedTextColor_month="@color/white"
        app:todayTextColor_month="@color/default_blue"
        app:isRoundRect_month="false"
        app:stackFromEnd="true"
        app:dayTextSize_month="12sp"
        app:flagNormalBgColor_month="@color/default_orange"
        app:flagPreBgColor_month="@color/default_light_gray"
        app:flagTextStr_month="@string/flag"
        app:flagTextColor_month="@color/white"
        app:monthLineTextSize="12sp"
        app:monthTitleTextSize="10sp">

    </journeycalendar.jessie.com.calendarlib.journey.all.MonthCalendarView>
```
- use in popwindow
```java

   LayoutInflater inflater = LayoutInflater.from(context);
          vPop = inflater.inflate(R.layout.view_popwindow_calendar_select, null);
          pop = new PopupWindow(vPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
  //        pop.setBackgroundDrawable(new ColorDrawable(0));
          pop.setFocusable(false);
          pop.setOutsideTouchable(false);
  
          dayPickerView = (DayPickerView) vPop.findViewById(R.id.pop_pickerView);
  
          dayPickerView.setController(new DatePickerController() {
              @Override
              public int getMaxYear() {
                  return DateUtil.getEndYear();
              }
  
              @Override
              public void onDayOfMonthSelected(int year, int month, int day) {
                  if (listener != null) {
                      listener.onCalendarSelect(year, month, day);
                  }
                  if (pop != null && pop.isShowing()) {
  //                    pop.dismiss();
                  }
              }
  
          });

```
