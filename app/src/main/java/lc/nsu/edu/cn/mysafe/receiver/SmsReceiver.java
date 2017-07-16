package lc.nsu.edu.cn.mysafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.service.LocationService;
import lc.nsu.edu.cn.mysafe.utils.ConstantValue;
import lc.nsu.edu.cn.mysafe.utils.SpUtil;

/**
 * Created by 刘畅 on 2017/7/12.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        if (open_security){
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects){
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();
                if (messageBody.contains("#*alarm*#")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.snjs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                if (messageBody.contains("#*location*#")){
                    context.startService(new Intent(context, LocationService.class));
                }
            }
        }
    }
}
