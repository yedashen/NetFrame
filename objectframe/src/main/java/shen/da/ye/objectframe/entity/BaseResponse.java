package shen.da.ye.objectframe.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 10:24
 **/

public class BaseResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("msg")
    public String msg;
}
