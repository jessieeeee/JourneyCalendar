package journeycalendar.jessie.com.journeycalendar;

/**
 * @author 编写人:JessieK
 * @date 创建时间:2017/2/21
 * @description 描述:行程数据封装类
 */

public class JourneyBean {
    private int index;//序号
    private String title;//标题
    private String campaignName;//活动名称

    public JourneyBean(int index, String title, String campaignName) {
        this.index = index;
        this.title = title;
        this.campaignName = campaignName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
