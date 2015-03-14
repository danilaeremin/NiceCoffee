package com.danilaeremin.nicecoffee.core;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.danilaeremin.nicecoffee.MainActivity;
import com.danilaeremin.nicecoffee.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by danilaeremin on 10.03.15.
 */
public class Utils {
    private final static String LOG_TAG = Utils.class.getSimpleName();

    public static String makeRequest (Uri URL) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        try {
            Uri builtUri = URL;

            java.net.URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return jsonStr;
    }

    public static long getUnixTime () {
        return System.currentTimeMillis() / 1000L;
    }

    public static Integer getBasicCategory (int pos) {
        Integer category_id[] = new Integer[]{
                1093, //Кофе
                1095, //Кофемашины
                1097, //Расходники
                1102, //Сиропы
                1094, //Подарки
                1103, //Сладости
                1096, //Аксессуары
                1101  //Чай
        };

        return category_id[pos];
    }

    public static String br2nl(String html) {
        return html
                .replace("\r", "")
                .replace("<p>", "")
                .replace("</p>", "\n")
                .replace("<br />", "\n")
                .replace("<br/>", "\n")
                .replace("<br>", "\n")
                .replace("\n\n", "\n")
                .replace("\n\n", "\n");
    }

    public static void setActivityName (ActionBarActivity a, String name) {
        ActionBar actionBar = a.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(name);
    }
}
