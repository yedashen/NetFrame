package shen.da.ye.objectframe;

import android.app.Application;
import android.content.Context;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 10:53
 **/

public class ObjectNetFrameApp extends Application {

    public static Context mShareInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mShareInstance = getApplicationContext();
    }
}
