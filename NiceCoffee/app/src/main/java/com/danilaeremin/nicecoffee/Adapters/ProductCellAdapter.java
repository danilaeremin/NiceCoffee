package com.danilaeremin.nicecoffee.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danilaeremin.nicecoffee.R;
import com.danilaeremin.nicecoffee.data.models.CategoryModel;
import com.danilaeremin.nicecoffee.data.models.ProductModel;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class ProductCellAdapter extends CursorAdapter {
    public ProductCellAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private ProductModel convertCursorRowToUXFormat(Cursor cursor) {
        return new ProductModel (cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_cell, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView product_name = (TextView) view.findViewById(R.id.product_name);
        product_name.setText(convertCursorRowToUXFormat(cursor).getName());

        TextView product_brief = (TextView) view.findViewById(R.id.product_brief);
        product_brief.setText(convertCursorRowToUXFormat(cursor).getBrief());

        TextView product_price = (TextView) view.findViewById(R.id.product_price);
        product_price.setText(convertCursorRowToUXFormat(cursor).getPriceStr());
    }

    public ProductModel getProduct(int position) {
        Cursor cursor = getCursor();

        if(cursor.moveToPosition(position)) {
            return new ProductModel (cursor);
        }

        return null;
    }
}
