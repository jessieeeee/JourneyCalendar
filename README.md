# JourneyCalendar
##support 
###week and month calendar
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
        compile 'com.github.jessieeeee:JourneyCalendar:-SNAPSHOT'
	}
</pre>

##How to Use
1. weekCalendar
### date click 
<pre>
weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                clickDateTime=dateTime;
                journey_month.setText(dateTime.getMonthOfYear() + "");
                journey_data_title.setText(OtherUtils.formatDate(dateTime.toDate()));
                journeyListAdapter.setData(setCurJourneyList(dateTime.toDate()));//设置当天的行程数据
            }

        });
  
</pre>
### week change
<pre>
      weekCalendar.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {
                journey_month.setText(firstDayOfTheWeek.getMonthOfYear() + "");
                Toast.makeText(context, "Week changed: " + firstDayOfTheWeek +
                        " Forward: " + forward, Toast.LENGTH_SHORT).show();
            }
        });
</pre>

### set today
<pre>
weekCalendar.reset();
</pre>
### set selected date
<pre>
weekCalendar.setSelectedDate(dateTime);
</pre>
### set bottom flag
<pre>
 weekCalendar.setFlagList(dates);
</pre>

2. monthCalendar
### init popwindow with monthCalendar
<pre>
preview = new CalendarListPopwindowPreview(context);
</pre>

### date click
<pre>
 preview.setOnCalendarSelectListener(new CalendarListPopwindowPreview.CalendarSelectListener() {
            @Override
            public void onCalendarSelect(int year, int month, int day) {
            
            }
        });
</pre>

### show popwindow with monthCalendar
<pre>
preview.showPop(view);
</pre>

### dismiss popwindow with monthCalendar
<pre>
preview.dismissPop();
</pre>

### go current month 
<pre>
preview.goMonth();
</pre>

### set selected date
<pre>
preview.setSelect(calendarDay);
</pre>

### set bottom flag
<pre>
 preview.setFlagDates(dates);
</pre>
