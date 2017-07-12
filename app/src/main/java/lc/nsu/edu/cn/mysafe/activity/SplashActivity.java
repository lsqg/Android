package lc.nsu.edu.cn.mysafe.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import lc.nsu.edu.cn.mysafe.R;
import lc.nsu.edu.cn.mysafe.utils.ConstantValue;
import lc.nsu.edu.cn.mysafe.utils.SpUtil;
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
    private RelativeLayout rl_root;
    private TextView tv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    private static final String tag = "SplashActivity";
    private ProgressDialog progressDialog;

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
                downloadAPK();
            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadAPK() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.i(tag, String.valueOf(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)));
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe.apk";
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i(tag, "下载成功");
                    progressDialog.dismiss();
                    File file = responseInfo.result;
                    installAPK(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(tag, String.valueOf(e));
                    Log.i(tag, "下载失败");
                    progressDialog.dismiss();
                }

                @Override
                public void onStart() {
                    Log.i(tag, "刚刚开始下载");
                    progressDialog = new ProgressDialog(SplashActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(true);
                    progressDialog.setIcon(R.drawable.icon1);
                    progressDialog.setMessage("正在下载");
                    progressDialog.show();
                    super.onStart();
                }

                @Override
                public void onLoading(final long total, final long current, boolean isUploading) {
                    Log.i(tag, "下载中。。。。。");
                    Log.i(tag, "total = " + total);
                    Log.i(tag, "current = " + current);
                    progressDialog.setMax((int) total);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            if (current < total){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setProgress((int) current);
                                    }
                                });
                            }else {
                                progressDialog.dismiss();
                            }
                        }
                    }).start();
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装对应APK
     * @param file 安装文件
     */
    private void installAPK(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT < 23) {
            uri = Uri.fromFile(file);
        }else {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            uri = Uri.parse("file://" + file.getAbsolutePath());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
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
        initAnimation();
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 获取数据
     */
    private void initData() {
        //1，应用版本名称
        tv_version_name.setText("版本名称:" + getVersionName());
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
        if(SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)){
            checkVersion();
        }else {
            Toast.makeText(this, "检查更新已关闭", Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,3000);
        }
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
                        mDownloadUrl = jsonObject.getString("apkurl");

                        Log.i(tag, versionCode);
                        Log.i(tag, mVersionDes);
                        Log.i(tag, mDownloadUrl);

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
        rl_root = (RelativeLayout) findViewById(R.id.activity_splash);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    downloadAPK();
                }
                break;
        }
    }
}
