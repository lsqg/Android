package lc.nsu.edu.cn.mysafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import lc.nsu.edu.cn.mysafe.R;

/**
 * Created by 刘畅 on 2017/7/4.
 */

public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/lc.nsu.edu.cn.mysafe";
    private String tag = "SettingItemView";
    private String mDestitle;
    private String mDesoff;
    private String mDeson;
    private CheckBox cb_box;
    private TextView tv_des;
    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        initAttrs(attrs);

        tv_title.setText(mDestitle);
    }

    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

    }

    /**
     * @return 返回当前SettingItemView是否选中状态
     */
    public boolean isCheck(){
        return cb_box.isChecked();
    }

    public void setCheck(boolean isCheck){
        cb_box.setChecked(isCheck);
        if (isCheck){
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }
}
