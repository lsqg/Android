package lc.nsu.edu.cn.mysafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import lc.nsu.edu.cn.mysafe.db.BlackNumberOpenHelper;
import lc.nsu.edu.cn.mysafe.db.domain.BlackNumberInfo;

/**
 * Created by 刘畅 on 2017/7/15.
 */

public class BlackNumberDao extends Object {
    private BlackNumberOpenHelper blackNumberOpenHelper;

    private BlackNumberDao(Context context){
        blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    private static BlackNumberDao blackNumberDao = null;
    public static BlackNumberDao getInstance(Context context){
        if (blackNumberDao == null){
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }

    public void insert(String phone, String mode){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        db.insert("blacknumber", null,values);
        db.close();
    }

    public void delete(String phone){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        db.delete("blacknumber", "phone = ?",new String[]{phone});
        db.close();
    }

    public void update(String phone, String mode){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        db.update("blacknumber", contentValues, "phone = ?", new String[]{phone});
        db.close();
    }

    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
        List<BlackNumberInfo> blackNumberList = new ArrayList<BlackNumberInfo>();
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            blackNumberList.add(blackNumberInfo);
        }
        cursor.close();
        db.close();

        return blackNumberList;
    }

    public int getMode(String phone){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        int mode = 0;
        Cursor cursor = db.query("blacknumber", new String[]{"mode"},"phone = ?",new String[]{phone},null,null,null);
        if (cursor.moveToNext()){
            mode = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return mode;
    }
}