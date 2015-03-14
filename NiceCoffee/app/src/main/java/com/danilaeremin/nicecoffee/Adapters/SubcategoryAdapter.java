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

/**
 * Created by danilaeremin on 09.03.15.
 */
public class SubcategoryAdapter extends CursorAdapter {
    public SubcategoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private CategoryModel convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        return new CategoryModel (cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.subcategory_cell, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView category_name = (TextView) view.findViewById(R.id.category_name);
        category_name.setText(convertCursorRowToUXFormat(cursor).getName());
    }

    public CategoryModel getCategory(int position) {
        Cursor cursor = getCursor();

        if(cursor.moveToPosition(position)) {
            return new CategoryModel (cursor);
        }

        return null;
    }
}
