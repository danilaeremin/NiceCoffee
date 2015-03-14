package com.danilaeremin.nicecoffee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danilaeremin.nicecoffee.R;

/**
 * Created by danilaeremin on 08.03.15.
 */
public class DrawerCatalogElementListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private  String[] drawer_catalog_name = new String[]{};
    private  Integer[] drawer_catalog_pics = new Integer[]{};

    private  String[] drawer_main_name = new String[]{};
    private  Integer[] drawer_main_pics = new Integer[]{};

    private  String drawer_head = "";
    private  String drawer_catalog = "";

    public void addElements (String[] drawer_catalog_name, Integer[] drawer_catalog_pics,
                             String[] drawer_main_name, Integer[] drawer_main_pics,
                             String drawer_head, String drawer_catalog) {

        this.drawer_catalog_name=drawer_catalog_name;
        this.drawer_catalog_pics=drawer_catalog_pics;

        this.drawer_main_name=drawer_main_name;
        this.drawer_main_pics=drawer_main_pics;

        this.drawer_head=drawer_head;
        this.drawer_catalog=drawer_catalog;
    }

    public DrawerCatalogElementListAdapter(Context context) {

        super(context, R.layout.fragment_navigation_drawer, new String[]{} );


        // TODO Auto-generated constructor stub

        this.context=context;

    }

    @Override
    public boolean isEnabled(int position)
    {
        return !(position == 1 + drawer_main_name.length);
    }

    @Override
    public int getCount() {
        return 2 + drawer_main_name.length + drawer_catalog_name.length;
    }

    public View getView(int position,View view,ViewGroup parent) {

        if (position == 0) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.drawer_head, null,true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.drawer_head_text);

            txtTitle.setText(drawer_head);
            return rowView;
        } else if (position < 1 + drawer_main_name.length) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.drawer_main_elements, null,true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.drawer_main_element_text);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.drawer_main_element_pic);

            txtTitle.setText(drawer_main_name[position-1]);
            imageView.setImageResource(drawer_main_pics[position-1]);
            return rowView;
        } else if (position == 1 + drawer_main_name.length) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.drawer_catalog_head, null,true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.drawer_catalog_text);

            txtTitle.setText(drawer_catalog);
            return rowView;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View rowView = inflater.inflate(R.layout.catalog_element_navigation_drawer, null,true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.drawer_catalog_element_text);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.drawer_catalog_element_pic);

            txtTitle.setText(drawer_catalog_name[position - 2 - drawer_main_name.length]);
            imageView.setImageResource(drawer_catalog_pics[position - 2 - drawer_main_name.length]);
            return rowView;
        }
    };
}
