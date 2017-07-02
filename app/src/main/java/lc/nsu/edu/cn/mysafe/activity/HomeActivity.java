package lc.nsu.edu.cn.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import lc.nsu.edu.cn.mysafe.R;

/**
 * Created by 刘畅 on 2017/6/27.
 */
public class HomeActivity extends Activity{
    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mTitlePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        initData();
    }

    private void initData() {
        mTitleStr = new String[]{
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };
        mTitlePic = new int[]{
                R.drawable.home1,R.drawable.home2,R.drawable.home3,R.drawable.home4,R.drawable.home5,R.drawable.home6,R.drawable.home7,R.drawable.home8,R.drawable.home9,
        };
        gv_home.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleStr[position]);
            iv_icon.setImageResource(mTitlePic[position]);
            return view;
        }
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
