package journeycalendar.jessie.com.calendarlib.journey.week.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import journeycalendar.jessie.com.calendarlib.R;
import journeycalendar.jessie.com.calendarlib.journey.DateUtil;
import journeycalendar.jessie.com.calendarlib.journey.week.adapter.PagerAdapter;
import journeycalendar.jessie.com.calendarlib.journey.week.eventbus.BusProvider;
import journeycalendar.jessie.com.calendarlib.journey.week.eventbus.Event;
import journeycalendar.jessie.com.calendarlib.journey.week.fragment.WeekFragment;


/**
 * @author 编写人:JessieK
 * @date 创建时间:2017/2/21
 */
public class WeekPager extends ViewPager {
    private PagerAdapter adapter;
    private int pos;
    private boolean check;
    public static int NUM_OF_PAGES;
    private TypedArray typedArray;


    public WeekPager(Context context) {
        super(context);
        initialize(null);
    }

    public WeekPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() {
                //Force rerendering so the week is drawn again when you return to the view after
                // back button press.
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initialize(AttributeSet attrs) {
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekCalendar);
//            NUM_OF_PAGES = typedArray.getInt(R.styleable.WeekCalendar_numOfPages, 100);
            NUM_OF_PAGES= DateUtil.countPageNum();
        }
        setId(idCheck());
        if (!isInEditMode()) {
            initPager(DateUtil.getCurDayDateTime());
            BusProvider.getInstance().register(this);
        }
    }


    private void initPager(DateTime dateTime) {
//        pos = NUM_OF_PAGES / 2;
        pos = DateUtil.countCurPage();
        adapter = new PagerAdapter(((FragmentActivity) getContext())
                .getSupportFragmentManager(), dateTime);
        setAdapter(adapter);
        addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (!check)
                    if (position < pos){
                        adapter.swipeBack();
                    }
                    else if (position > pos){
                        adapter.swipeForward();
                    }

                pos = position;
                check = false;

            }

        });
        setOverScrollMode(OVER_SCROLL_NEVER);
        setCurrentItem(pos);
        if (WeekFragment.selectedDateTime == null)
            WeekFragment.selectedDateTime = DateUtil.getCurDayDateTime();
    }

    @Subscribe
    public void setCurrentPage(Event.SetCurrentPageEvent event) {
        check = true;
        if (event.getDirection() == 1)
            adapter.swipeForward();
        else
            adapter.swipeBack();
        setCurrentItem(getCurrentItem() + event.getDirection());

    }

    @Subscribe
    public void reset(Event.ResetEvent event) {
        WeekFragment.selectedDateTime = DateUtil.getCurDayDateTime();
        //WeekFragment.CalendarStartDate = new DateTime();
        initPager(DateUtil.getCurDayDateTime());
    }

    @Subscribe
    public void setSelectedDate(Event.SetSelectedDateEvent event) {
        WeekFragment.selectedDateTime = event.getSelectedDate();
        initPager(event.getSelectedDate());
    }

    @Subscribe
    public void setStartDate(Event.SetStartDateEvent event) {
        WeekFragment.selectedDateTime = event.getStartDate();
        initPager(event.getStartDate());
    }

    private int idCheck() {
        int id = 0;
        while (true) {
            if (findViewById(++id) == null) break;
        }
        return id;
    }
}
