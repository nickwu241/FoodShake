package com.nwu.foodshake;

import android.os.Handler;
import android.util.Log;

import com.nwu.yelpapi.YelpV3API;
import com.nwu.yelpapi.YelpV3APIProvider;
import com.nwu.yelpapi.pojo.AccessToken;
import com.nwu.yelpapi.pojo.Business;
import com.nwu.yelpapi.pojo.SearchResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpRequestHandler {
    //----------------------------------------------------------------------------------------------
    public enum Status {
        NEEDS_AUTHENTICATION,
        STANDBY,
        BUSY
    }

    //----------------------------------------------------------------------------------------------
    private static final String TAG = "YelpRequestHandler";
    private final YelpV3APIProvider mYelpApiProvider;

    private Handler mHandler;
    private YelpV3API mYelp;
    private Status mStatus;

    //----------------------------------------------------------------------------------------------
    public YelpRequestHandler(String client_id, String client_secret) {
        mStatus = Status.NEEDS_AUTHENTICATION;
        mYelpApiProvider = new YelpV3APIProvider(client_id, client_secret);
        authenticate();
    }

    //----------------------------------------------------------------------------------------------
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    //----------------------------------------------------------------------------------------------
    public void authenticate() {
        mYelpApiProvider.getAccessToken().enqueue(
                new YelpCallback<AccessToken>(Constants.RESPONSE_AUTHENTICATION) {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (response.isSuccessful()) {
                            mYelp = mYelpApiProvider.getAPI(response.body().access_token);
                        }
                        super.onResponse(call, response);
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------
    public void search(Map<String, String> parameters) {
        mStatus = Status.BUSY;
        mYelp.search(parameters).enqueue(
                new YelpCallback<SearchResponse>(Constants.RESPONSE_SEARCH)
        );
    }

    //----------------------------------------------------------------------------------------------
    public void business(String id) {
        mStatus = Status.BUSY;
        mYelp.business(id, null).enqueue(
                new YelpCallback<Business>(Constants.RESPONSE_BUSINESS)
        );
    }

    //----------------------------------------------------------------------------------------------
    public Status getStatus() {
        return mStatus;
    }

    //----------------------------------------------------------------------------------------------
    private class YelpCallback<T> implements Callback<T> {
        private final int callbackSuccessCode;

        public YelpCallback(int successCode) {
            callbackSuccessCode = successCode;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (Debug.ON) Log.i(TAG, "OnResponse success: " + response.isSuccessful());

            if (mHandler == null) {
                return;
            }

            mStatus = response.isSuccessful() || mStatus != Status.NEEDS_AUTHENTICATION ?
                    Status.STANDBY : Status.NEEDS_AUTHENTICATION;

            final int code = response.isSuccessful() ? callbackSuccessCode : Constants.RESPONSE_FAILED;
            final Object data = response.isSuccessful() ? response.body() : response;
            mHandler.sendMessage(mHandler.obtainMessage(code, data));
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (Debug.ON) Log.e(TAG, "onFailure", t);
            if (mStatus != Status.NEEDS_AUTHENTICATION) {
                mStatus = Status.STANDBY;
            }
            mHandler.sendMessage(mHandler.obtainMessage(Constants.RESPONSE_ERROR));
        }
    }

    //----------------------------------------------------------------------------------------------
}
