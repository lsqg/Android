package lc.nsu.edu.cn.mysafe.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.service.AddressService;
import lc.nsu.edu.cn.mysafe.service.BlackNumberService;
import lc.nsu.edu.cn.mysafe.utils.ConstantValue;
import lc.nsu.edu.cn.mysafe.utils.ServiceUtil;
import lc.nsu.edu.cn.mysafe.utils.SpUtil;
import lc.nsu.edu.cn.mysafe.view.SettingItemView;

/**
 * Created by 刘畅 on 2017/7/3.
 */
public class SettingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        initUpdate();
        initAddress();
        initBlacknumber();
    }

    private void initBlacknumber() {
        final SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
        boolean isRunning = ServiceUtil.isRunning(this, "lc.nsu.edu.cn.mysafe.service.BlackNumberService");
        siv_blacknumber.setCheck(isRunning);

        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 19){
                }else {
                    Toast.makeText(getApplicationContext(), "android4.4以上不支持短信拦截功能", Toast.LENGTH_SHORT).show();
                }
                boolean isCheck = siv_blacknumber.isCheck();
                siv_blacknumber.setCheck(!isCheck);
                if (!isCheck){
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
                }else {
                    stopService(new Intent(getApplicationContext(), BlackNumberService.class));
                }
            }
        });
    }

    private void initAddress() {
        final SettingItemView siv_address = (SettingItemView)findViewById(R.id.siv_address);
        boolean isRunning = ServiceUtil.isRunning(this, "lc.nsu.edu.cn.mysafe.service.AddressService");
        siv_address.setCheck(isRunning);
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                    } else {
                        ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
                    }
                }
                boolean ischeck = siv_address.isCheck();
                siv_address.setCheck(!ischeck);
                if (!ischeck){
                    startService(new Intent(getApplicationContext(), AddressService.class));
                }else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView)findViewById(R.id.siv_update);
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    } else {
                        ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                }
                boolean isCheck = siv_update.isCheck();
                siv_update.setCheck(!isCheck);
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
            }
        });
    }

}
