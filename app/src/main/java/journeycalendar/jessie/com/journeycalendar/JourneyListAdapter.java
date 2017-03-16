package journeycalendar.jessie.com.journeycalendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author 编写人:JessieK
 * @date 创建时间:2017/2/21
 * @description 描述:行程适配器
 */


public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.ViewHolder> {
    public List<JourneyBean> journeyBeen;

    public void setData(List<JourneyBean> journeyBeen) {
        this.journeyBeen = journeyBeen;
        notifyDataSetChanged();
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.journey_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (journeyBeen != null) {
            viewHolder.events_position.setText(journeyBeen.get(position).getIndex() + "");
            viewHolder.events_title.setText(journeyBeen.get(position).getTitle());
            viewHolder.events_msg.setText(journeyBeen.get(position).getCampaignName());
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        if(journeyBeen!=null){
            return journeyBeen.size();
        }else{
            return 0;
        }

    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView events_position;
        public TextView events_title;
        public TextView events_msg;

        public ViewHolder(View view) {
            super(view);
            events_position = (TextView) view.findViewById(R.id.events_position);
            events_title = (TextView) view.findViewById(R.id.events_title);
            events_msg = (TextView) view.findViewById(R.id.events_msg);
        }
    }
}
