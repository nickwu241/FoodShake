package com.foodshake;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

public class SearchReviewsTask extends AsyncTask<Void,Void,Void> {
    private Handler mHandler;

    public SearchReviewsTask(Handler handler) {
        mHandler = handler;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            AppDB.reviewsForSelected = ReviewsParser.getReviews(AppDB.selectedRestaurant.businessObj.getId());
        } catch (IOException e) {
            Log.e(Constants.TAG_EXCEPTION, e.getMessage());
            AppDB.reviewsForSelected = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mHandler.obtainMessage(Constants.MESSAGE_SEARCH_REVIEWS_SUCCESS).sendToTarget();
    }
}
