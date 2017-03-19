package com.foodshake;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;


import static org.junit.Assert.*;


public class YelpParserTests {
    YelpParser p;
    @Before
    public void createParseYelp() {
         p = new YelpParser();
    }


    @Test
    public void testAll() throws IOException{
        Map<String, String> testParam = new HashMap<>();
        testParam.put("type", "indian");
        testParam.put("radius", "15000");

        ArrayList<Restaurant> restaurants = p.businessSearch(testParam, null);
        ArrayList<Restaurant> empty = new ArrayList<Restaurant>();

        //assertArrayEquals("something wrong", ,restaurants);
    }

}
