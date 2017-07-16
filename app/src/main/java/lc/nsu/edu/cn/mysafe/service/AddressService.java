package lc.nsu.edu.cn.mysafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import lc.nsu.edu.cn.mysafe.R;

/**
 * Created by 刘畅 on 2017/7/16.
 */
public class AddressService extends Service{
    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View mViewToast;
    private WindowManager mWM;
    private String mAddress;
    private TextView tv_toast;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_toast.setText(mAddress);
        }
    };

    @Override
    public void onCreate() {
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        super.onCreate();
    }

    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mWM!=null && mViewToast!=null)
                    mWM.removeView(mViewToast);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    ShowToast(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void ShowToast(String incomingNumber) {
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.gravity = Gravity.LEFT;
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        mWM.addView(mViewToast, mParams);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);

        query(incomingNumber);
    }

    private void query(final String incomingNumber) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mAddress = "模拟机";
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        if (mTM!=null && mPhoneStateListener!=null){
            mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
