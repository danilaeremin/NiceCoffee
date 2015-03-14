package com.danilaeremin.nicecoffee.core;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.danilaeremin.nicecoffee.core.ProductContract.CategoriesEntry;
import com.danilaeremin.nicecoffee.core.ProductContract.ProductEntry;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class ProductProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private dbHelper mOpenHelper;

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_WITH_PRODUCT_ID = 101;
    private static final int PRODUCT_WITH_CATEGORY_ID = 102;
    private static final int CATEGORIES = 200;
    private static final int CATEGORY_WITH_CATEGORY_ID = 201;
    private static final int CATEGORY_WITH_PARENT_ID = 202;

    private static final SQLiteQueryBuilder sProductListQueryBuilder;
    private static final SQLiteQueryBuilder sCategoryListQueryBuilder;

    private static final SQLiteQueryBuilder sProductListWithParentCategoryIdQueryBuilder;

    static{
        sProductListQueryBuilder = new SQLiteQueryBuilder();
        sProductListQueryBuilder.setTables(
                ProductEntry.TABLE_NAME
                );

        sCategoryListQueryBuilder = new SQLiteQueryBuilder();
        sCategoryListQueryBuilder.setTables(CategoriesEntry.TABLE_NAME);

        sProductListWithParentCategoryIdQueryBuilder = new SQLiteQueryBuilder();
        sProductListWithParentCategoryIdQueryBuilder.setTables(
                ProductEntry.TABLE_NAME + " AS p" +
                    " LEFT OUTER JOIN " + CategoriesEntry.TABLE_NAME + " AS c1 ON (" +
                        "p." + ProductEntry.COLUMN_PRODUCT_CATEGORY + " = c1." + CategoriesEntry.COLUMN_CATEGORY_ID + ")" +
                    " LEFT OUTER JOIN " + CategoriesEntry.TABLE_NAME + " AS c2 ON (" +
                        "c1." + CategoriesEntry.COLUMN_PARENT_ID    + " = c2." + CategoriesEntry.COLUMN_CATEGORY_ID + ")" +
                    " LEFT OUTER JOIN " + CategoriesEntry.TABLE_NAME + " AS c3 ON (" +
                        "c2." + CategoriesEntry.COLUMN_PARENT_ID    + " = c3." + CategoriesEntry.COLUMN_CATEGORY_ID + ")"
        );
    }

    private static final String sProducts = "";
    private static final String sProductWithProductId =
            ProductEntry.TABLE_NAME + "." + ProductEntry.COLUMN_PRODUCT_ID + " = ? ";
    private static final String sProductWithCategoryId =
            "c1." + CategoriesEntry.COLUMN_CATEGORY_ID + " = ? OR "+
            "c2." + CategoriesEntry.COLUMN_CATEGORY_ID + " = ? OR "+
            "c3." + CategoriesEntry.COLUMN_CATEGORY_ID + " = ? OR "+
            "c3." + CategoriesEntry.COLUMN_PARENT_ID + " = ?";

    private static final String sCategoryList =
            CategoriesEntry.TABLE_NAME;
    private static final String sCategoryWithCategoryId =
            CategoriesEntry.TABLE_NAME + "." + CategoriesEntry.COLUMN_CATEGORY_ID + " = ? ";
    private static final String sCategoryWithParentId =
            CategoriesEntry.TABLE_NAME + "." + CategoriesEntry.COLUMN_PARENT_ID + " = ? ";

    private Cursor getProducts(Uri uri, String[] projection, String sortOrder) {
        String[] selectionArgs = new String[]{};
        String selection = sProducts;

        return sProductListQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getProductWithProductId(Uri uri, String[] projection, String sortOrder) {
        String product_id = ProductEntry.getProductIdFromUri(uri);

        String[] selectionArgs = new String[]{product_id};
        String selection = sProductWithProductId;

        return sProductListQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getProductWithCategoryId(Uri uri, String[] projection, String sortOrder) {
        String category_id = ProductEntry.getCategoryIdFromUri(uri);

        String[] selectionArgs = new String[]{category_id,category_id, category_id, category_id};
        String selection = sProductWithCategoryId;

        return sProductListWithParentCategoryIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCategoryList(Uri uri, String[] projection, String sortOrder) {
        String[] selectionArgs = new String[]{};
        String selection = sCategoryList;

        return sCategoryListQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCategoryWithCategoryId(Uri uri, String[] projection, String sortOrder) {
        String category_id = CategoriesEntry.getCategoryIdFromUri(uri);

        String[] selectionArgs = new String[]{category_id};
        String selection = sCategoryWithCategoryId;

        return sCategoryListQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCategoryWithParentId(Uri uri, String[] projection, String sortOrder) {
        String parent_id = CategoriesEntry.getParentIdFromUri(uri);

        String[] selectionArgs = new String[]{parent_id};
        String selection = sCategoryWithParentId;

        return sCategoryListQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ProductContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ProductContract.PATH_PRODUCT, PRODUCTS);
        matcher.addURI(authority, ProductContract.PATH_PRODUCT + "/"+ ProductEntry.COLUMN_PRODUCT_ID+"/#", PRODUCT_WITH_PRODUCT_ID);
        matcher.addURI(authority, ProductContract.PATH_PRODUCT + "/"+ ProductEntry.COLUMN_PRODUCT_CATEGORY+"/#", PRODUCT_WITH_CATEGORY_ID);

        matcher.addURI(authority, ProductContract.PATH_CATEGORY, CATEGORIES);
        matcher.addURI(authority, ProductContract.PATH_CATEGORY + "/"+ CategoriesEntry.COLUMN_CATEGORY_ID+"/#", CATEGORY_WITH_CATEGORY_ID);
        matcher.addURI(authority, ProductContract.PATH_CATEGORY + "/"+ CategoriesEntry.COLUMN_PARENT_ID+"/#", CATEGORY_WITH_PARENT_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new dbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case PRODUCTS:
            {
                retCursor = getProducts(uri, projection, sortOrder);
                break;
            }

            case PRODUCT_WITH_PRODUCT_ID: {
                retCursor = getProductWithProductId(uri, projection, sortOrder);
                break;
            }

            case PRODUCT_WITH_CATEGORY_ID: {
                retCursor = getProductWithCategoryId(uri, projection, sortOrder);
                break;
            }
            case CATEGORIES:
            {
                retCursor = getCategoryList(uri, projection, sortOrder);
                break;
            }

            case CATEGORY_WITH_CATEGORY_ID: {
                retCursor = getCategoryWithCategoryId(uri, projection, sortOrder);
                break;
            }

            case CATEGORY_WITH_PARENT_ID: {
                retCursor = getCategoryWithParentId(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_TYPE;
            case PRODUCT_WITH_CATEGORY_ID:
                return ProductEntry.CONTENT_TYPE;
            case PRODUCT_WITH_PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;

            case CATEGORIES:
                return CategoriesEntry.CONTENT_TYPE;
            case CATEGORY_WITH_CATEGORY_ID:
                return CategoriesEntry.CONTENT_ITEM_TYPE;
            case CATEGORY_WITH_PARENT_ID:
                return CategoriesEntry.CONTENT_TYPE;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = Uri.EMPTY;

        switch (match) {
            case PRODUCT_WITH_PRODUCT_ID: {
                long _id = db.insert(ProductEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = ProductEntry.buildProductUri((int)(_id));
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case CATEGORY_WITH_CATEGORY_ID: {
                long _id = db.insert(CategoriesEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = CategoriesEntry.buildCategoryUri((int)(_id));
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = -1;

        switch (match) {
            case PRODUCT_WITH_PRODUCT_ID:
                rowsDeleted = db.delete(
                        ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY_WITH_CATEGORY_ID:
                rowsDeleted = db.delete(
                        CategoriesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = -1;

        switch (match) {
            case PRODUCT_WITH_PRODUCT_ID:
                rowsUpdated = db.update(ProductEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CATEGORY_WITH_CATEGORY_ID:
                rowsUpdated = db.update(CategoriesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ProductEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case CATEGORIES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CategoriesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
