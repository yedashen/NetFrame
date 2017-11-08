package shen.da.ye.objectframe.net;

import java.util.WeakHashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;
import shen.da.ye.objectframe.entity.BaseResponse;
import shen.da.ye.objectframe.entity.LoginInEntity;

/**
 * @author ChenYe
 *         created by on 2017/11/6 0006. 14:26
 *         <p>
 *         如果你的后台接收的是对象的话，那么你的入参就不能跟restService一样写了
 **/

public interface RestService {

    /**
     * 这个接口传过去的是对象
     * BASE_URL："http://127.0.0.1:50112/web/",这个是内网，你们是测不通的
     *
     * @param entity
     * @return
     */
    @POST("loginService")
    Observable<BaseResponse> login(@Body LoginInEntity entity);

    /**
     * 这个是测试正常接口，传过去的是map
     * 测这个接口的时候的BASE_URL是http://admin.hkshijue.com/
     *
     * @return
     */
    @GET("ModelWeb/AppModel/GetCooperateBusiness")
    Observable<String> getList(@QueryMap WeakHashMap<String, String> param);



    /**
     * 上传附件接口，后台要我传的参数就是这样命名的，别喷我
     * @param requestBody
     * @return
     */
    @Multipart
    @POST("insertFjjl")
    Observable<BaseResponse> uploadAccessory(@Part("file") RequestBody requestBody);
}
