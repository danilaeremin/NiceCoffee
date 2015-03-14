package com.danilaeremin.nicecoffee.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danilaeremin.nicecoffee.core.ProductContract.CategoriesEntry;
import com.danilaeremin.nicecoffee.core.ProductContract.ProductEntry;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class dbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "products.db";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ProductEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL,"+
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"+
                ProductEntry.COLUMN_PRODUCT_BRIEF + " TEXT NOT NULL,"+
                ProductEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT NOT NULL,"+
                ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL,"+
                ProductEntry.COLUMN_PRODUCT_PIC + " TEXT NOT NULL,"+
                ProductEntry.COLUMN_PRODUCT_CATEGORY + " INTEGER NOT NULL,"+

                " FOREIGN KEY (" + ProductEntry.COLUMN_PRODUCT_CATEGORY + ") REFERENCES " +
                CategoriesEntry.TABLE_NAME + " (" + CategoriesEntry.COLUMN_CATEGORY_ID + "), " +

                " UNIQUE (" + ProductEntry.COLUMN_PRODUCT_ID +
                ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CategoriesEntry.TABLE_NAME + " (" +
                CategoriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                CategoriesEntry.COLUMN_CATEGORY_ID + " INTEGER NOT NULL,"+
                CategoriesEntry.COLUMN_PARENT_ID + " INTEGER NOT NULL,"+
                CategoriesEntry.COLUMN_CATEGORY_NAME + " TEXT NOT NULL,"+
                CategoriesEntry.COLUMN_CATEGORY_PIC + " TEXT NOT NULL,"+

                " FOREIGN KEY (" + CategoriesEntry.COLUMN_PARENT_ID + ") REFERENCES " +
                CategoriesEntry.TABLE_NAME + " (" + CategoriesEntry.COLUMN_CATEGORY_ID + "), " +

                " UNIQUE (" + CategoriesEntry.COLUMN_CATEGORY_ID +
                ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoriesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
