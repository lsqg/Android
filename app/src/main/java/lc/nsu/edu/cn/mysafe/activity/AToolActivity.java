package lc.nsu.edu.cn.mysafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.engine.SmsBackUp;

/**
 * Created by 刘畅 on 2017/7/13.
 */
public class AToolActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
    }

    public void queryAddress(View view) {
        startActivity(new Intent(this, QueryAddressActivity.class));
    }

    public void smsBackup(View view){
        showSmsBackUpDialog();
    }

    private void showSmsBackUpDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.icon1);
        progressDialog.setTitle("短信备份");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sms.xml";
                SmsBackUp.backup(getApplicationContext(),path,progressDialog);
            }
        }.start();
    }
}
