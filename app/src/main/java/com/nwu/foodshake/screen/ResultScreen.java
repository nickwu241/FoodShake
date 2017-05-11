package com.nwu.foodshake.screen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import com.nwu.foodshake.R;
import com.nwu.foodshake.Util;
import com.nwu.foodshake.page.ResultPage;
import com.nwu.yelpapi.pojo.Business;
import com.nwu.yelpapi.pojo.Category;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class ResultScreen implements Screen {
    //----------------------------------------------------------------------------------------------
    public final String name;
    public final String categories;
    public final String price;
    public final String address;
    public final String phone;
    public final String numReviews;
    public final String hourToday;

    public final String url;
    public final String id;

    public final int stars;
    public Bitmap image;

    private final String mBusinessImageUrl;

    //----------------------------------------------------------------------------------------------
    public ResultScreen(Business business) {
        name = business.name;

        // TODO: repeated join logic here
        String displayCat = "";
        for (Category c : business.categories) {
            displayCat += c.title + ", ";
        }
        categories = displayCat.length() > 0 ?
                displayCat.substring(0, displayCat.length() - 2) : "";

        price = business.price;
        address = Util.join(business.location.display_address, "\n");
        phone = business.display_phone;
        numReviews = "based on " + business.review_count +
                (business.review_count == 1 ? " review" : " reviews");

        stars = getResourceFromRating(business.rating);

        // TODO: test this hour logic
        // for now, there's only 1 item in the hours array
        Business.Hour.Open open = business.hours.get(0).open.get(dayOfWeek());
        hourToday = open.start + " - " + open.end;

        url = business.url;
        id = business.id;

        mBusinessImageUrl = business.image_url;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void showPage(Activity activity) {
        new GetImageTask(activity, this).execute(mBusinessImageUrl);
    }

    //----------------------------------------------------------------------------------------------
    private int dayOfWeek() {
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY: return 0;
            case Calendar.TUESDAY: return 1;
            case Calendar.WEDNESDAY: return 2;
            case Calendar.THURSDAY: return 3;
            case Calendar.FRIDAY: return 4;
            case Calendar.SATURDAY: return 5;
            case Calendar.SUNDAY: return 6;
            default: throw new IllegalArgumentException("Invalid DAY OF WEEK");
        }
    }

    //----------------------------------------------------------------------------------------------
    private int getResourceFromRating(double rating) {
        final int flooredRating = (int) rating;
        final boolean whole = flooredRating == rating;
        switch (flooredRating) {
            case 0: return R.drawable.stars_small_0;
            case 1: return whole ? R.drawable.stars_small_1 : R.drawable.stars_small_1_half;
            case 2: return whole ? R.drawable.stars_small_2 : R.drawable.stars_small_2_half;
            case 3: return whole ? R.drawable.stars_small_3 : R.drawable.stars_small_3_half;
            case 4: return whole ? R.drawable.stars_small_4 : R.drawable.stars_small_4_half;
            case 5: return R.drawable.stars_small_5;
            default: throw new IllegalArgumentException("Invalid Rating: " + rating);
        }
    }

    //----------------------------------------------------------------------------------------------
    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        private final Activity mActivity;
        private ResultScreen mScreen;

        public GetImageTask(Activity activity, ResultScreen screen) {
            mActivity = activity;
            mScreen = screen;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                return BitmapFactory.decodeStream(new URL(url).openStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mScreen.image = result;
            new ResultPage(mActivity, mScreen).show();
        }
    }

    //----------------------------------------------------------------------------------------------
}
