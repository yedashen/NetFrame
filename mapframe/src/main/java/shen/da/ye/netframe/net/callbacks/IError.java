package shen.da.ye.netframe.net.callbacks;

/**
 * @author ChenYe
 *         created by on 2017/11/6 0006. 10:22
 **/

public interface IError {

    /**
     * 网络是好的，但是请求接口返回的不是想要的数据
     * @param code
     * @param msg
     */
    void onError(int code, String msg);
}
