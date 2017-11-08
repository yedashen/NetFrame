package shen.da.ye.objectframe.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import shen.da.ye.objectframe.R;

/**
 * @author ChenYe
 */

public class DownloadDialog extends Dialog {

    ProgressBar mProgressBar;
    TextView mProgressTv;

    public DownloadDialog(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_download_pg, null);
        setContentView(view);
        mProgressBar = view.findViewById(R.id.pb);
        mProgressTv = view.findViewById(R.id.pg_tv);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        mProgressTv.setText(progress + "/100");
    }

}
