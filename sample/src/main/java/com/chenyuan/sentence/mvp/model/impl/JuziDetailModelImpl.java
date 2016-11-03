package com.chenyuan.sentence.mvp.model.impl;

import android.content.Context;

import com.chenyuan.sentence.http.Api;
import com.chenyuan.sentence.http.ServiceFactory;
import com.chenyuan.sentence.http.service.AllarticleService;
import com.chenyuan.sentence.mvp.model.IJuziDetailModel;
import com.chenyuan.sentence.mvp.model.entity.SceneListDetail;
import com.chenyuan.sentence.mvp.model.entity.SentenceImageText;
import com.chenyuan.sentence.mvp.presenter.callback.OnJuziDetailListener;
import com.chenyuan.sentence.util.DocParseUtil;
import com.chenyuan.sentence.util.StringUtil;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuziDetailModelImpl implements IJuziDetailModel {

    private AllarticleService allarticleService;

    private OnJuziDetailListener mListener;

    private Context mContext;

    @Override
    public void loadOriginal(Context context, boolean isFrist, String url, OnJuziDetailListener listener) {
        this.mContext = context;
        this.mListener = listener;

        this.allarticleService = ServiceFactory.getInstance().createService(AllarticleService.class, Api.BASE_URL_ORIGINAL);

        loadData(isFrist, url);
    }

    private void loadData(final boolean isFrist, String url) {

        Call<ResponseBody> call = allarticleService.loadJuziDetail(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                InputStream inputStream = response.body().byteStream();

                String result = StringUtil.inToString(inputStream);
                System.out.println(result);

                SceneListDetail sceneListDetail = null;
                try {
                    sceneListDetail = DocParseUtil.parseJuziDetail(isFrist, result);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mListener.onSuccess(sceneListDetail);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mListener.onError(t);
            }
        });

    }


}