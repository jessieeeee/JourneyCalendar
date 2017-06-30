package journeycalendar.jessie.com.journeycalendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.Calendar;
import java.util.List;

import journeycalendar.jessie.com.calendarlib.journey.DateUtil;
import journeycalendar.jessie.com.calendarlib.journey.all.CalendarDay;
import journeycalendar.jessie.com.calendarlib.journey.all.MonthCalendarController;
import journeycalendar.jessie.com.calendarlib.journey.all.MonthCalendarView;


/**
 * @date 创建时间:2017/2/21
 * @author 编写人:JessieK
 */
public class CalendarListPopwindowPreview {

    private static PopupWindow pop;
    private View vPop;
    private static Context context;
    private List<String> dates;
    private MonthCalendarView monthCalendarView;
    public void showPop(View floatView) {
        if (pop != null && !pop.isShowing()) {
            pop.showAsDropDown(floatView);
        }
    }

    public boolean isShow(){
        return pop.isShowing();
    }
    public void dismissPop(){
        pop.dismiss();
    }

    public void setSelect(CalendarDay calendarDay){
        monthCalendarView.setSelected(calendarDay);
    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = monthCalendarView.getLinearLayoutManager().findFirstVisibleItemPosition();
        int lastItem = monthCalendarView.getLinearLayoutManager().findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            monthCalendarView.scrollToPosition(n);
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = monthCalendarView.getChildAt(n - firstItem).getTop();
            monthCalendarView.scrollBy(0, top);
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            monthCalendarView.scrollToPosition(n);
        }

    }

    public void goMonth(){
        int offset=0;
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        if(year== monthCalendarView.getStartYear()){
            offset=calendar.get(Calendar.MONTH)- DateUtil.getFirstMonth();
        }else{
            offset=(12-(DateUtil.getFirstMonth()-1))+calendar.get(Calendar.MONTH)-1;
        }
        moveToPosition(offset);
    }

    private void selectPhoto(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        vPop = inflater.inflate(R.layout.view_popwindow_calendar_select, null);
        pop = new PopupWindow(vPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        pop.setBackgroundDrawable(new ColorDrawable(0));
        pop.setFocusable(false);
        pop.setOutsideTouchable(false);

        monthCalendarView = (MonthCalendarView) vPop.findViewById(R.id.pop_pickerView);

        monthCalendarView.setController(new MonthCalendarController() {
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
    }

    public void setFlagDates(List<String> dates){
        monthCalendarView.setFlagDates(dates);
    }


    public interface CalendarSelectListener {
        void onCalendarSelect(int year, int month, int day);
    }


    private CalendarSelectListener listener;

    public void setOnCalendarSelectListener(CalendarSelectListener listener) {
        this.listener = listener;
    }


    private CalendarListPopwindowPreview preview;


    public CalendarListPopwindowPreview(Context c) {
        context = c;
        selectPhoto(context);
    }
}
