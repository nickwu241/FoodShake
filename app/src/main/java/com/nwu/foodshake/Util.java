package com.nwu.foodshake;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Util {
    //----------------------------------------------------------------------------------------------
    public static <T> T getRandom(List<T> list) {
        return list.size() > 0 ? list.get(new Random().nextInt(list.size())) : null;
    }

    //----------------------------------------------------------------------------------------------
    public static String join(Collection<String> strings, String delim) {
        String result = "";
        for (String string : strings) {
            result += string + delim;
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    //----------------------------------------------------------------------------------------------
    public static String join(CharSequence[] sequences, String delim) {
        String result = "";
        for (CharSequence sequence : sequences) {
            result += sequence;
            result += delim;
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    //----------------------------------------------------------------------------------------------
}
