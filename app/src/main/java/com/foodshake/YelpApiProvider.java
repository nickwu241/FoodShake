package com.foodshake;

import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import java.io.IOException;

public class YelpApiProvider {
    private static YelpFusionApiFactory factory= new YelpFusionApiFactory();
    private static YelpFusionApi api = null;

    private YelpApiProvider() {
        
    }

    public static YelpFusionApi getYelpApi() throws IOException {
        if (api == null) {
            api = factory.createAPI(BuildConfig.app_id, BuildConfig.app_secret);
        }
        return api;
    }
}
