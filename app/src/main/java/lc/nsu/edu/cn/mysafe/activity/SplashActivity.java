package lc.nsu.edu.cn.mysafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.utils.StreamUtil;

public class SplashActivity extends AppCompatActivity {
    /**
     * 更新新版本的状态码
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入程序主界面的状态码
     */
    private static final int ENTER_HOME = 101;
    /**
     * url出错的状态码
     */
    private static final int URL_ERROR = 102;
    /**
     * io出错的状态码
     */
    private static final int IO_ERROR = 103;
    /**
     * json出错的状态码
     */
    private static final int JSON_ERROR = 104;
    private TextView tv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private static final String tag = "SplashActivity";

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url error", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(SplashActivity.this, "io error", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "json error", Toast.LENGTH_LONG).show();
                    enterHome();
                    break;

            }
        }
    };

    /**
     * t弹出对话框提醒升级
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.drawable.icon1);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initData();
    }

    /**
     * 获取数据
     */
    private void initData() {
        //1，应用版本名称
        tv_version_name.setText("版本名称：" + getVersionName());
        //检测服务端是否有更新
        //2,获取本地版本号
        mLocalVersionCode = getVersionCode();
        //3,获取服务器版本号（客户端发请求，服务端响应（json,xml））
        //http://100.0.101.15/updateinfo.html
        /*
            json内容
            1 版本的描述
            2 版本号
            3 下载地址
         */
        checkVersion();
    }

    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                long startTime = System.currentTimeMillis();
                Message msg = Message.obtain();
                try {
                    //1,封装url
                    URL url = new URL("http://100.0.101.15:8080/updateinfo.html");
                    //2，开启地址
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,设置请求头
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    connection.setRequestMethod("GET");
                    //4,获取响应码
                    if (connection.getResponseCode() == 200){
                        //5，以流的形式，获取数据
                        InputStream is = connection.getInputStream();
                        //6，将流转换为字符串（工具类封装）
                        String json = StreamUtil.streamToString(is);

                        Log.i(tag, json);
                        //7，json解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionCode = jsonObject.getString("code");
                        mVersionDes = jsonObject.getString("des");
                        String downloadUrl = jsonObject.getString("apkurl");

                        Log.i(tag, versionCode);
                        Log.i(tag, mVersionDes);
                        Log.i(tag, downloadUrl);

                        //8, 比对版本号，提示更新
                        if (mLocalVersionCode < Double.parseDouble(versionCode)){
                            //提示用户更新,提示框UI(要在主线程执行),消息机制
                            msg.what = UPDATE_VERSION;
                        }else {
                            msg.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally {
                    long endTime = System.currentTimeMillis();
                    if((endTime - startTime) < 4000){
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 返回版本号
     * @return 返回0代表异常，非0为正常
     */
    public int getVersionCode() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }

    /**
     * 获取Manifast文件中版本名称
     * @return  返回版本名称，返回null代表异常
     */
    public String getVersionName() {
        //获取包管理者,pm传0代表基本信息
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
