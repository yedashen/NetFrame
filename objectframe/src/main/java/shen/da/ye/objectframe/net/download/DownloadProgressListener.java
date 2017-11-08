package shen.da.ye.objectframe.net.download;

/**
 *@author ChenYe
 */

public interface DownloadProgressListener {
    /**
     * 这个是更新首页的ProgressDialog
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    void update(long bytesRead, long contentLength, boolean done);
}
