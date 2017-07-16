package lc.nsu.edu.cn.mysafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.db.dao.BlackNumberDao;
import lc.nsu.edu.cn.mysafe.db.domain.BlackNumberInfo;
import lc.nsu.edu.cn.mysafe.view.SettingItemView;

/**
 * Created by 刘畅 on 2017/7/15.
 */
public class BlackNumberActivity extends Activity{
    private Button bt_add;
    private ListView lv_blacknumber;
    private BlackNumberDao mDao;
    private List<BlackNumberInfo> mBlackNumberList;
    private MyAdapter mAdapter;
    private int mode;
    
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter = new MyAdapter();
            lv_blacknumber.setAdapter(mAdapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);

        initUI();
        initData();
        initBlacknumber();
    }

    private void initBlacknumber() {
        SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mDao = BlackNumberDao.getInstance(getApplicationContext());
                mBlackNumberList = mDao.findAll();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        bt_add= (Button) findViewById(R.id.bt_add);
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);
        dialog.setView(view,0,0,0,0);

        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)){
                    mDao.insert(phone, mode+"");
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                    blackNumberInfo.phone = phone;
                    blackNumberInfo.mode = mode+"";
                    mBlackNumberList.add(0,blackNumberInfo);
                    if (mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(), "请输入拦截号码", Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
                
        dialog.show();
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mBlackNumberList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBlackNumberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.listview_blacknumber, null);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            TextView iv_delete = (TextView) view.findViewById(R.id.iv_delete);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDao.delete(mBlackNumberList.get(position).phone);
                    mBlackNumberList.remove(position);
                    if (mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            tv_phone.setText(mBlackNumberList.get(position).phone);
            int mode = Integer.parseInt(mBlackNumberList.get(position).mode);
            switch (mode){
                case 1:
                    tv_mode.setText("拦截短信");
                    break;
                case 2:
                    tv_mode.setText("拦截电话");
                    break;
                case 3:
                    tv_mode.setText("拦截所有");
                    break;
            }
            return view;
        }
    }
}
