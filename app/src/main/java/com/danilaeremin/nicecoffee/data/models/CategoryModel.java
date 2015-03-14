package com.danilaeremin.nicecoffee.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.danilaeremin.nicecoffee.core.ProductContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by danilaeremin on 10.03.15.
 */
public class CategoryModel {

    private static final String LOG_TAG = CategoryModel.class.getSimpleName();

    final String PARENT_ID = "parent";
    final String CATEGORY_ID = "categoryID";
    final String CATEGORY_NAME = "name_ru";
    final String CATEGORY_PIC = "picture";

    private Integer categoryId = 0;
    private String name = "no_name";
    private Integer parentId = 0;
    private String pic = "no_pic";

    public CategoryModel () {
    }

    public CategoryModel(Cursor cursor) {
        int categoryId_idx = cursor.getColumnIndex(ProductContract.CategoriesEntry.COLUMN_CATEGORY_ID);
        int name_idx = cursor.getColumnIndex(ProductContract.CategoriesEntry.COLUMN_CATEGORY_NAME);
        int parentId_idx = cursor.getColumnIndex(ProductContract.CategoriesEntry.COLUMN_PARENT_ID);
        int pic_idx = cursor.getColumnIndex(ProductContract.CategoriesEntry.COLUMN_CATEGORY_PIC);

        this.categoryId = cursor.getInt(categoryId_idx);
        this.parentId = cursor.getInt(name_idx);
        this.name = cursor.getString(name_idx);
        this.pic = cursor.getString(pic_idx);
    }

    public CategoryModel (JSONObject category) throws JSONException {
        this.categoryId = category.getInt(CATEGORY_ID);
        if (!category.isNull( PARENT_ID ))
            this.parentId = category.getInt(PARENT_ID);
        this.name = category.getString(CATEGORY_NAME);
        this.pic = category.getString(CATEGORY_PIC);
    }

    public String getName () {
        return name;
    }

    public String getPic () {
        return pic;
    }

    public Integer getCategoryId () {
        return categoryId;
    }

    public Integer getParentId () {
        return parentId;
    }

    public ContentValues getContentValues () {
        ContentValues productValues = new ContentValues();

        productValues.put(ProductContract.CategoriesEntry.COLUMN_CATEGORY_ID, categoryId);
        productValues.put(ProductContract.CategoriesEntry.COLUMN_CATEGORY_NAME, name);
        productValues.put(ProductContract.CategoriesEntry.COLUMN_CATEGORY_PIC, pic);
        productValues.put(ProductContract.CategoriesEntry.COLUMN_PARENT_ID, parentId);

        return productValues;
    }
}
