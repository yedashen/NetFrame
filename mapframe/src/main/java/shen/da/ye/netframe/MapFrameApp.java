package shen.da.ye.netframe;

import android.app.Application;
import android.content.Context;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 08:56
 **/

public class MapFrameApp extends Application {

    public static Context mShareInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mShareInstance = getApplicationContext();
    }
}
