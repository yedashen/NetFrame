package shen.da.ye.netframe.net.callbacks;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shen.da.ye.netframe.net.RestCreator;
import shen.da.ye.netframe.ui.loader.LatteLoader;
import shen.da.ye.netframe.ui.loader.LoaderStyle;

/**
 * @author ChenYe
 *         created by on 2017/11/6 0006. 10:25
 **/

public class RequestCallbacks implements Callback<String> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;

    public RequestCallbacks(IRequest request, ISuccess success,
                            IFailure failure, IError error, LoaderStyle loaderStyle) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.LOADER_STYLE = loaderStyle;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }

        onRequestFinish();
    }


    @Override
    public void onFailure(Call<String> call, Throwable t) {

        if (FAILURE != null) {
            FAILURE.onFailure();
        }

        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }

        onRequestFinish();
    }

    private void onRequestFinish() {
        if (LOADER_STYLE != null) {
            //这里可以弄一个postDelay来增加视觉效果
            RestCreator.getParams().clear();
            LatteLoader.stopLoading();
        }
    }
}
