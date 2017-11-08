package shen.da.ye.objectframe;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shen.da.ye.objectframe.entity.LoginInEntity;
import shen.da.ye.objectframe.net.RestCreator;
import shen.da.ye.objectframe.net.download.DownloadCreator;
import shen.da.ye.objectframe.ui.DownloadDialog;
import shen.da.ye.objectframe.util.FileUtil;
import shen.da.ye.objectframe.util.Logger;
import shen.da.ye.objectframe.util.StringUtils;

/**
 * @author ChenYe
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "cy===MainActivity";
    private int mCurrentProgress = 0;
    private static final int PROGRESS_MAX = 100;
    private DownloadDialog mDownloadDialog;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "ss", 20, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        registerReceiver();
    }

    /**
     * @param view
     */
    public void request(View view) {
        WeakHashMap<String, String> paramMap = new WeakHashMap<>();
        RestCreator.getService()
                .getList(paramMap)
                .subscribe(s -> {

                }, e -> {
                    //这里面是出错的,要么是网络出错，要么是接口请求返回值不是我们需要的
                    Log.e(TAG, e.getMessage());
                });
    }


    public void sendObject(View view) {
        LoginInEntity entity = new LoginInEntity();
        entity.setUserName("账号");
        entity.setPassword("密码");
        entity.setSessionId(UUID.randomUUID().toString().replaceAll("-", ""));
        RestCreator.getService()
                .login(entity)
                .subscribe(baseResponse -> {
                }, e -> {
                });
    }

    public void download(View view) {
        mDownloadDialog = new DownloadDialog(this);
        mDownloadDialog.show();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/voice.apk";
        File file = new File(path);
        DownloadCreator.getService()
                .downloadApk()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtil.writeFile(inputStream, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InputStream>() {
                    @Override
                    public void onCompleted() {
                        finishNotification();
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                    }
                });

    }

    /**
     * 如果是上传图片记得压缩
     * ByteArrayOutputStream os = new ByteArrayOutputStream();
     * bitmap.compress(Bitmap.CompressFormat.JPEG, 30, os);
     * RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), os.toByteArray());
     * bitmap.recycle();
     * TODO 也可以直接把file放到RequestBody.create的第二个参数
     *
     * @param view
     */
    public void upload(View view) {
        ByteArrayOutputStream os = getByteArrayOutputStream("");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), os.toByteArray());
        RestCreator.getService()
                .uploadAccessory(requestBody)
                .subscribe(baseResponse -> {
                }, e -> {
                });
    }

    public ByteArrayOutputStream getByteArrayOutputStream(String fileName) {
        ByteArrayOutputStream os = null;
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            os = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int ch;
            int i = 0;
            while ((ch = bis.read()) != -1) {
                bos.write(ch);
                if (i++ == 100) {
                    bos.flush();
                    i = 0;
                }
            }
            bos.flush();    //提交文件流，很关键
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return os;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("message_progress")) {
                long currentRead = intent.getLongExtra("currentRead", 0);
                long total = intent.getLongExtra("total", 0);
                int progress = (int) ((currentRead * 100) / total);
                if (progress > mCurrentProgress) {
                    mCurrentProgress = progress;
                    Logger.e("progress", progress + "");
                    if (progress == 1) {
                        showNotification();
                    } else {
                        sendNotification(progress, currentRead, total);
                    }

                    if (progress == PROGRESS_MAX) {
                        mCurrentProgress = 0;
                        finishNotification();
                        dismiss();
                    } else {
                        if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
                            mDownloadDialog.setProgress(progress);
                        }
                    }
                }
            }
        }
    };

    private void dismiss() {
        if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
            mDownloadDialog.dismiss();
        }
    }

    private void registerReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("message_progress");
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void showNotification() {
        Logger.e("main", "创建了通知栏");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("现场管控")
                .setContentText("0M")
                .setAutoCancel(true);

        mNotificationManager.notify(0, mNotificationBuilder.build());
    }

    private void finishNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(0);
            mNotificationBuilder.setProgress(0, 0, false);
            mNotificationBuilder.setContentText("下载完毕");
            mNotificationManager.notify(0, mNotificationBuilder.build());
        }
    }

    private void sendNotification(int progress, long read, long total) {
        if (mNotificationBuilder != null) {
            Logger.e("main", "send了通知栏");
            //第三个参数控制动画形态
            mNotificationBuilder.setProgress(100, progress, false);
            mNotificationBuilder.setContentText(StringUtils.getDataSize(read) + "/" +
                    StringUtils.getDataSize(total));
            mNotificationManager.notify(0, mNotificationBuilder.build());
        }
    }
}
