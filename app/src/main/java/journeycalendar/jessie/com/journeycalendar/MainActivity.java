package journeycalendar.jessie.com.journeycalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import journeycalendar.jessie.com.calendarlib.journey.all.CalendarDay;
import journeycalendar.jessie.com.calendarlib.journey.week.WeekCalendar;
import journeycalendar.jessie.com.calendarlib.journey.week.listener.OnDateClickListener;
import journeycalendar.jessie.com.calendarlib.journey.week.listener.OnWeekChangeListener;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.DensityUtil;
import journeycalendar.jessie.com.calendarlib.journey.week.tools.OtherUtils;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rl_bar;
    private ImageView img_back;//退出
    private TextView journey_month;//月份
    private TextView journey_month_click;//全月点击按钮
    private WeekCalendar weekCalendar;//周日历
    private TextView journey_data_title;//当前选择日历标题
    private EmptyRecyclerView journey_events;//日程事件列表
    private LinearLayout ly_default;//缺省页
    private List<JourneyDate> journeyDates;//行程日期列表
    private JourneyListAdapter journeyListAdapter;
    private CalendarListPopwindowPreview preview;
    private DateTime clickDateTime;
    private Context context;
    private TextView text_today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        initView();
        initRecycleView();
        setDefault();
        setListener();
        clickDateTime=new DateTime();
        journey_month.setText((new Date().getMonth() + 1 )+ "");
        journey_data_title.setText(OtherUtils.formatDate(new Date()));
        weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                clickDateTime=dateTime;
                journey_month.setText(dateTime.getMonthOfYear() + "");
                journey_data_title.setText(OtherUtils.formatDate(dateTime.toDate()));
                journeyListAdapter.setData(setCurJourneyList(dateTime.toDate()));//设置当天的行程数据
            }

        });
        weekCalendar.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {
                journey_month.setText(firstDayOfTheWeek.getMonthOfYear() + "");
                Toast.makeText(context, "Week changed: " + firstDayOfTheWeek +
                        " Forward: " + forward, Toast.LENGTH_SHORT).show();
            }
        });
        preview = new CalendarListPopwindowPreview(context);
        preview.setOnCalendarSelectListener(new CalendarListPopwindowPreview.CalendarSelectListener() {
            @Override
            public void onCalendarSelect(int year, int month, int day) {
                journey_month.setText((month + 1) + "");
                journey_data_title.setText(year + "年" + (month + 1) + "月" + day + "号");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day);
                DateTime dateTime=new DateTime(calendar);
                clickDateTime=dateTime;
                weekCalendar.setSelectedDate(dateTime);
                journeyListAdapter.setData(setCurJourneyList(dateTime.toDate()));//设置当天的行程数据
                preview.dismissPop();
            }
        });
        setData();
        journeyListAdapter.setData(setCurJourneyList(new Date()));//设置当天的行程数据
    }

    private void setListener() {
        text_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preview != null && preview.isShow()) {
                    preview.goMonth();
                } else {
                    journey_data_title.setText(OtherUtils.formatDate(new Date()));
                    weekCalendar.reset();
                    journey_month.setText((new Date().getMonth() + 1) + "");
                    weekCalendar.setSelectedDate(new DateTime());
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preview.isShow()){
                    preview.dismissPop();
                }else{
                    finish();
                }
            }
        });
        journey_month_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview.goMonth();
                Calendar click=clickDateTime.toCalendar(new Locale("zh", "ZH"));
                preview.setSelect(new CalendarDay(click.get(Calendar.YEAR),click.get(Calendar.MONTH),click.get(Calendar.DAY_OF_MONTH)));
                preview.showPop(rl_bar);
            }
        });
    }

    private void initView() {
        text_today= (TextView) findViewById(R.id.text_today);
        img_back= (ImageView) findViewById(R.id.img_back);
        rl_bar= (RelativeLayout) findViewById(R.id.rl_bar);
        journey_month = (TextView) findViewById(R.id.journey_month);
        journey_month_click = (TextView) findViewById(R.id.journey_month_click);
        weekCalendar = (WeekCalendar) findViewById(R.id.weekCalendar);
        journey_data_title = (TextView) findViewById(R.id.journey_data_title);
        journey_events = (EmptyRecyclerView) findViewById(R.id.journey_events);
        ly_default = (LinearLayout) findViewById(R.id.ly_default);
    }

    //设置缺省页
    private void setDefault() {
        journey_events.setEmptyView(ly_default);
    }

    //设置行程列表
    private void initRecycleView() {
        journey_events.setLayoutManager(new LinearLayoutManager(context));
        journey_events.setEmptyView(ly_default);
        journey_events.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, DensityUtil.dip2px(context,10), getResources().getColor(R.color.default_bg)));
        journeyListAdapter = new JourneyListAdapter();
        journey_events.setAdapter(journeyListAdapter);
    }

    //设置当前行程列表
    private List<JourneyBean> setCurJourneyList(Date date) {
        String dateStr = OtherUtils.formatDate(date, "yyyy-MM-dd");
        Iterator<JourneyDate> it = journeyDates.iterator();
        while (it.hasNext()) {
            JourneyDate journeyDate = it.next();
            if (dateStr.equals(journeyDate.getDate())) {
                return journeyDate.getJourneyBeens();
            }
        }
        return null;
    }

    //设置数据
    private void setData() {
        journeyDates = new ArrayList<>();

        for(int i=0;i<10;i++){
            DateTime dateTime=new DateTime();
            int days= (int) (Math.random()*30);
            days=days*(days%2==0?1:-1);
            dateTime=dateTime.plusDays(days);
            DateTimeFormatter dateTimeFormat= DateTimeFormat.forPattern("yyyy-MM-dd");
            String dateStr=dateTime.toString(dateTimeFormat);
            JourneyDate journeyDate = new JourneyDate();
            journeyDate.setDate(dateStr);
            journeyDate.getJourneyBeens().add(new JourneyBean(1, "加班1", "所属活动1"));
            journeyDate.getJourneyBeens().add(new JourneyBean(2, "加班2", "所属活动2"));
            journeyDate.getJourneyBeens().add(new JourneyBean(3, "加班3", "所属活动3"));
            journeyDates.add(journeyDate);
        }


        List<String> dates = getFlags();
        weekCalendar.setFlagList(dates);
        preview.setFlagDates(dates);
    }

    //设置行标记
    private List<String> getFlags() {
        List<String> dates = new ArrayList<>();
        Iterator<JourneyDate> it = journeyDates.iterator();
        while (it.hasNext()) {
            JourneyDate journeyDate = it.next();
            dates.add(journeyDate.getDate());
        }
        return dates;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (preview != null && preview.isShow()) {
                preview.dismissPop();
            } else {
                finish();
            }
        }
        return true;
    }


    //模拟数据封装
    public class JourneyDate {
        private String date;
        private List<JourneyBean> journeyBeens;

        public JourneyDate() {
            journeyBeens = new ArrayList<>();
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<JourneyBean> getJourneyBeens() {
            return journeyBeens;
        }

        public void setJourneyBeens(List<JourneyBean> journeyBeens) {
            this.journeyBeens = journeyBeens;
        }
    }
}
