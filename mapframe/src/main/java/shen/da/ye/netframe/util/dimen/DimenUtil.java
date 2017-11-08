package shen.da.ye.netframe.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import shen.da.ye.netframe.MapFrameApp;

/**
 * @author ChenYe
 */

public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = MapFrameApp.mShareInstance.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = MapFrameApp.mShareInstance.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
