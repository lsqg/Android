package lc.nsu.edu.cn.mysafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import lc.nsu.edu.cn.mysafe.R;

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

}
