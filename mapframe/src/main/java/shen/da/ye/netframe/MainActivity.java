package shen.da.ye.netframe;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.WeakHashMap;

import pub.devrel.easypermissions.EasyPermissions;
import shen.da.ye.netframe.net.RestClient;
import shen.da.ye.netframe.net.callbacks.IError;
import shen.da.ye.netframe.net.callbacks.IFailure;
import shen.da.ye.netframe.net.callbacks.ISuccess;
import shen.da.ye.netframe.ui.loader.LoaderStyle;

/**
 * @author ChenYe
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "cy==MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "ss", 20, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 正常的请求接口
     * 这个测试数据的BASE_URL是 http://admin.hkshijue.com/
     * 如果要测试这个接口记得去修改RestCreator的BASE_URL
     *
     * @param view
     */
    public void request(View view) {
        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("pageIndex", "0");
        params.put("PageSize", "5");
        RestClient
                .builder()
                .url("ModelWeb/AppModel/GetCooperateBusiness")
                .params(params)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "onSuccess");
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure");
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.e(TAG, "onError");
                    }
                })
                .loader(this, LoaderStyle.LineSpinFadeLoaderIndicator)
                .build()
                .get();
    }


    /**
     * 上传，这个我没测
     *
     * @param view
     */
    public void upload(View view) {
        RestClient
                .builder()
                .url("insertFjjl")
                .file(new File(""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "onSuccess");
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure");
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.e(TAG, "onError");
                    }
                })
                .loader(this, LoaderStyle.LineSpinFadeLoaderIndicator)
                .build()
                .upload();
    }

    /**
     * 下载,这里是边下载到内存一遍写到本地，你可以下一个比较大的东西去试试看看本地的那个文件是不是慢慢变大就知道了。
     * TODO 记住吧RestCreator的拦截去注释掉,给存储的读写权限
     * 下面的name如果是指定的那么就不需要extenssion，比如如果是下载更新的apk，那么你可能是知道
     * 名字的就不需要扩展名了，但是如果一开始不能确定下载的是什么，比如你想下载图片文件，那么你直接设置
     * extenssion为".png"
     * <p>
     * BASE_URL,我下面的接口你们是调不通的，因为是内网。"http://127.0.0.1:50112/web/";
     * NetWorkServlet?filename=voice.apk
     *
     * @param view
     */
    public void download(View view) {
        RestClient
                .builder()
                .url("NetWorkServlet?filename=voice.apk")
                .name("test.apk")
                .dir("DCIM")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "onSuccess");
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure");
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.e(TAG, "onError");
                    }
                })
                .loader(this, LoaderStyle.LineSpinFadeLoaderIndicator)
                .build()
                .download();
    }


}
