package lc.nsu.edu.cn.mysafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.utils.ConstantValue;
import lc.nsu.edu.cn.mysafe.utils.SpUtil;

/**
 * Created by 刘畅 on 2017/7/6.
 */
public class Setup3Activity extends BaseSetupActivity{
    private EditText ed_phone_number;
    private Button bt_select_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String phone = ed_phone_number.getText().toString();
        if (!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else {
            Toast.makeText(getApplicationContext(), "请输入安全号码", Toast.LENGTH_LONG).show();
        }
    }

    private void initUI() {
        if (ContextCompat.checkSelfPermission(Setup3Activity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Setup3Activity.this, Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(Setup3Activity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
            }
        }
        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number);
        String phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        ed_phone_number.setText(phone);
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null) {
            String phone = data.getStringExtra("phone");
            phone = phone.replace("-", "").replace(" ", "").trim();
            ed_phone_number.setText(phone);
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
