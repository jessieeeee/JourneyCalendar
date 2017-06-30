# JourneyCalendar
- ![image](https://github.com/jessieeeee/JourneyCalendar/blob/master/sample.gif)
## support 
### week and month calendar
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
### week change
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

### set today
```java
weekCalendar.reset();
```
### set selected date
```java
weekCalendar.setSelectedDate(dateTime);
```
### set bottom flag
```java
 weekCalendar.setFlagList(dates);
```

2. monthCalendar
## use in popwindow
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
                  Log.e("kosmos", "onDayOfMonthSelected:" + day + " / " + month + " / " + year);
                  if (listener != null) {
                      listener.onCalendarSelect(year, month, day);
                  }
                  if (pop != null && pop.isShowing()) {
  //                    pop.dismiss();
                  }
              }
  
          });

```
