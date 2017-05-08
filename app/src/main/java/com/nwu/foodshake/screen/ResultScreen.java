package com.nwu.foodshake.screen;

import android.app.Activity;

import com.nwu.foodshake.page.ResultPage;
import com.nwu.yelpapi.pojo.Business;

public class ResultScreen implements Screen {
    //----------------------------------------------------------------------------------------------
    private Business mBusiness;

    //----------------------------------------------------------------------------------------------
    public ResultScreen() {

    }

    //----------------------------------------------------------------------------------------------
    public ResultScreen(Business business) {
        mBusiness = business;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void showPage(Activity activity) {
        new ResultPage(activity, this).show();
    }

    //----------------------------------------------------------------------------------------------
    public Business getBusiness() {
        return mBusiness;
    }

    //----------------------------------------------------------------------------------------------
    public void setBusiness(Business business) {
        mBusiness = business;
    }

    //----------------------------------------------------------------------------------------------
}
