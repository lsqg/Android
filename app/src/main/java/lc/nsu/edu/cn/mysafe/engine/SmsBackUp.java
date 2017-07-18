package lc.nsu.edu.cn.mysafe.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by 刘畅 on 2017/7/17.
 */

public class SmsBackUp extends Object {

    public static void backup(Context context, String path, ProgressDialog pd) {
        try {
            File file = new File(path);
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                    new String[]{"address", "date", "type", "body"},
                    null, null, null);
            FileOutputStream fos = new FileOutputStream(file);
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(fos, "utf-8");
            newSerializer.startDocument("utf-8", true);
//            newSerializer.startTag(null, );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
