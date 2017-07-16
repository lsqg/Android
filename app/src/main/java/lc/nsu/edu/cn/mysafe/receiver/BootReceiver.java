package lc.nsu.edu.cn.mysafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import lc.nsu.edu.cn.mysafe.utils.ConstantValue;
import lc.nsu.edu.cn.mysafe.utils.SpUtil;

/**
 * Created by 刘畅 on 2017/7/12.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber()+"XXX";
        String sim_number = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
        if (!simSerialNumber.equals(sim_number)){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(
                    SpUtil.getString(context, ConstantValue.CONTACT_PHONE,""),
                    null,
                    "sim change!!",
                    null,
                    null
            );
        }

    }
}
