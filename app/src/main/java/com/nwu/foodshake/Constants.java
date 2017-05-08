package com.nwu.foodshake;

public interface Constants {
    //---------------
    // Response Codes
    //----------------------------------------------------------------------------------------------
    int RESPONSE_FAILED = -2;
    int RESPONSE_ERROR = -1;
    int RESPONSE_AUTHENTICATION = 1;
    int RESPONSE_SEARCH = 2;
    int RESPONSE_BUSINESS = 3;

    //-----------------
    // Permission Codes
    //----------------------------------------------------------------------------------------------
    int PERMISSION_COARSE_LOCATION = 1;
    int PERMISSION_FINE_LOCATION = 2;

    //------------
    // Error Codes
    //----------------------------------------------------------------------------------------------
    int ERROR_NETWORK = 1;
    int ERROR_LOCATION = 2;
    int ERROR_NO_RESTAURANT = 3;

    //--------------
    // Error Strings
    //----------------------------------------------------------------------------------------------
    String ES_NETWORK = "NETWORK ERROR\nUnable to establish internet connection";
    String ES_LOCATION = "LOCATION ERROR\nUnable to access your location";
    String ES_NO_RESTAURANT = "NO RESTAURANTS FOUND\nTry modifying settings";

    //-----------------------
    // SharedPreferences Keys
    //----------------------------------------------------------------------------------------------
    String KEY_RADIUS = "radius";
    String KEY_PRICE = "price";
    String KEY_CUISINE_TYPE = "cuisine_type";
    String KEY_OPEN_NOW = "open_now";

    //-------------------
    // Vibration patterns
    //----------------------------------------------------------------------------------------------
    long VIBRATION_PATTERN_USER_FEEDBACK[] =
            {50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};

    //----------------------------------------------------------------------------------------------
}
