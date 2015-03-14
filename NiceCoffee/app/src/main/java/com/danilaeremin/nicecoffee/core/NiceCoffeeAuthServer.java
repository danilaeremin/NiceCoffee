package com.danilaeremin.nicecoffee.core;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.danilaeremin.nicecoffee.R;
import com.danilaeremin.nicecoffee.data.models.CategoryModel;
import com.danilaeremin.nicecoffee.data.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by danilaeremin on 10.03.15.
 */

//startService(intent.putExtra("time", 4).putExtra("label", "Call 3") );

public class NiceCoffeeAuthServer extends IntentService {
    public final static String TASK_TYPE = "taskType";
    public final String PRODUCT_ID = "product_id";

    public final static String DB_TIME = "DbTime";

    public final static int UPDATE_DB = 0;
    public final static int FORCE_UPDATE_DB = 1;
    public final static int UPDATE_PRICE = 2;

    final String LOG_TAG = NiceCoffeeAuthServer.class.getSimpleName();

    public static String PRODUCT_LIST = "allProducts.php";
    public static String PRODUCT_WITH_PRODUCT_ID = "productById.php";
    public static String PRODUCT_PRICE_WITH_PRODUCT_ID = "price.php";

    public static String CATEGORY_LIST = "categoryList.php";
    public static String CATEGORY_WITH_PARENT_ID = "subcategoryByParentId.php";

    public static String GET_DEALS = "deals.php";

    public static String UPDATE_DB_TIME = "getTime.php";

    final String API_BASE_URL =
            "http://nicecoffee.ru/api/";

    public void forceUpdateDb() {
        Uri URL = Uri.parse(API_BASE_URL + PRODUCT_LIST).buildUpon()
                .build();
        String productList = Utils.makeRequest(URL);


        URL = Uri.parse(API_BASE_URL + CATEGORY_LIST).buildUpon()
                .build();
        String categoryList = Utils.makeRequest(URL);

        try {
            productListFromJSON(productList);
            categoryListFromJSON(categoryList);

            updateDbTime();
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void updatePrice(int product_id) {

    }

    public void updateDb() {
        Uri URL = Uri.parse(API_BASE_URL + UPDATE_DB_TIME).buildUpon()
                .build();
        String timeStr = Utils.makeRequest(URL);

        long time = Long.parseLong(timeStr);

        if (time > getDbTime()) {
            forceUpdateDb();
        }
    }


    public NiceCoffeeAuthServer() {
        super("NiceCoffeeAuthServer");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int taskType = intent.getIntExtra(TASK_TYPE, 0);

        switch (taskType) {
            case UPDATE_DB: {
                updateDb();
                break;
            }

            case FORCE_UPDATE_DB: {
                forceUpdateDb();
                break;
            }

            case UPDATE_PRICE: {
                updatePrice(intent.getIntExtra(PRODUCT_ID, 0));
                break;
            }

            default: {
                Log.e(LOG_TAG, "UNKNOWN AuthServer param...");
                break;
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void productListFromJSON (String productList) throws JSONException {
        JSONArray productListJson = new JSONArray(productList);
        Vector<ContentValues> cVVector = new Vector<ContentValues>(productListJson.length());

        for(int i = 0; i < productListJson.length(); i++) {

            JSONObject product = productListJson.getJSONObject(i);

            cVVector.add((new ProductModel(product)).getContentValues());
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            Uri Content_Uri = ProductContract.ProductEntry.CONTENT_URI;

            ContentResolver resolver = getContentResolver();

            resolver.bulkInsert(Content_Uri, cvArray);
        }
    }

    private void categoryListFromJSON (String categoryList) throws JSONException {
        JSONArray categoryListJson = new JSONArray(categoryList);
        Vector<ContentValues> cVVector = new Vector<ContentValues>(categoryListJson.length());

        for(int i = 0; i < categoryListJson.length(); i++) {
            JSONObject category = categoryListJson.getJSONObject(i);

            cVVector.add((new CategoryModel(category)).getContentValues());
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            Uri Content_Uri = ProductContract.CategoriesEntry.CONTENT_URI;

            ContentResolver resolver = getContentResolver();

            resolver.bulkInsert(Content_Uri, cvArray);
        }
    }

    public long getDbTime () {
        return getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).getLong(DB_TIME, 0);
    }

    private void updateDbTime () {
        long time = Utils.getUnixTime();
        getSharedPreferences(getString(R.string.content_authority),MODE_PRIVATE).
                edit().
                putLong(DB_TIME, time).
                commit();

        Log.i(LOG_TAG, "update Db time");
    }
}