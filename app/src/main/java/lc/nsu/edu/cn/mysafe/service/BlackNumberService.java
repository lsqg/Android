package lc.nsu.edu.cn.mysafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lc.nsu.edu.cn.mysafe.db.dao.BlackNumberDao;

/**
 * Created by 刘畅 on 2017/7/16.
 */

public class BlackNumberService extends Service {
    private InnerSmsReceiver mInnerSmsReceiver;
    private BlackNumberDao mDao;
    private TelephonyManager mTM;

    @Override
    public void onCreate() {
        mDao = BlackNumberDao.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        mInnerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mInnerSmsReceiver, intentFilter);

        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        MyPhoneStateListener mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    endCall(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void endCall(String phone) {
        int mode = mDao.getMode(phone);
        if (mode == 1 || mode ==3){
            try {
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                Method method = clazz.getMethod("getService", String.class);
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class InnerSmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = sms.getOriginatingAddress();
                int mode = mDao.getMode(originatingAddress);
                if (mode == 1 || mode ==3){
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mInnerSmsReceiver!=null){
            unregisterReceiver(mInnerSmsReceiver);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
