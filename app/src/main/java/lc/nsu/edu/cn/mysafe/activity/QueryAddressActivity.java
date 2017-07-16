package lc.nsu.edu.cn.mysafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import lc.nsu.edu.cn.mysafe.R;

/**
 * Created by 刘畅 on 2017/7/13.
 */
public class QueryAddressActivity extends Activity{
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private String mAddress;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_query_result.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        initUI();
    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)){
                    query(phone);
                }else {
                    Toast.makeText(getApplicationContext(), "请输入查询号码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void query(final String phone) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mAddress = "模拟器";
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
