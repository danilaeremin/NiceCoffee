package com.danilaeremin.nicecoffee.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.danilaeremin.nicecoffee.core.ProductContract.ProductEntry;
import com.danilaeremin.nicecoffee.core.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class ProductModel {

    private static final String LOG_TAG = ProductModel.class.getSimpleName();

    final String PRODUCT_ID = "productID";
    final String CATEGORY_ID = "categoryID";
    final String PRODUCT_NAME = "name_ru";
    final String PRODUCT_PRICE = "Price";
    final String PRODUCT_BRIEF = "brief_description_ru";
    final String PRODUCT_DESCRIPTION = "description_ru";
    final String PRODUCT_PIC = "picture";

    private Integer productId = 0;
    private String name = "no_name";
    private String brief = "no_brief";
    private String desc = "no_desc";
    private String pic = "no_pic";
    private Integer price = 0;
    private Integer categoryId = 0;

    public ProductModel () {
    }

    public ProductModel(Cursor cursor) {
        int productId_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_ID);
        int name_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int brief_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_BRIEF);
        int desc_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_DESCRIPTION);
        int price_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int pic_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PIC);
        int categoryId_idx = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);

        this.productId = cursor.getInt(productId_idx);
        this.name = cursor.getString(name_idx);
        this.brief = cursor.getString(brief_idx);
        this.desc = cursor.getString(desc_idx);
        this.price = cursor.getInt(price_idx);
        this.pic = cursor.getString(pic_idx);
        this.categoryId = cursor.getInt(categoryId_idx);
    }

    public ProductModel (JSONObject product) throws JSONException {
        this.productId = product.getInt(PRODUCT_ID);
        this.name = product.getString(PRODUCT_NAME);
        this.brief = product.getString(PRODUCT_BRIEF);
        this.desc = product.getString(PRODUCT_DESCRIPTION);
        this.categoryId = product.getInt(CATEGORY_ID);
        this.price = product.getInt(PRODUCT_PRICE);

        this.pic = product.getString(PRODUCT_NAME);//
    }

    public String getName () {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getDescription () {
        return Utils.br2nl(desc);
    }

    public String getBrief () {
        return Utils.br2nl(brief);
    }

    public String getPriceStr () {
        return price.toString();
    }
    public Integer getPrice () {
        return price;
    }
    public void setPrice (Integer price) {
        this.price = price;
    }

    public ContentValues getContentValues () {
        ContentValues productValues = new ContentValues();

        productValues.put(ProductEntry.COLUMN_PRODUCT_ID, productId);
        productValues.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        productValues.put(ProductEntry.COLUMN_PRODUCT_BRIEF, brief);
        productValues.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, desc);
        productValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        productValues.put(ProductEntry.COLUMN_PRODUCT_PIC, pic);
        productValues.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, categoryId);

        return productValues;
    }
}
