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
public class DataAdapter extends ArrayAdapter<String> {

    Context mContext;

    private String[] drawerMainElements;
    private Integer[] drawerMainElementsPics;

    // Конструктор
    public DataAdapter(Context context, String[] drawerMainElements, Integer[] drawerMainElementsPics) {
        super(context, R.layout.main_fragment_grid_main_buttons, drawerMainElements);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.drawerMainElements = drawerMainElements;
        this.drawerMainElementsPics = drawerMainElementsPics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rowView = inflater.inflate(R.layout.main_fragment_grid_main_buttons, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.main_fragment_grid_main_element_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.main_fragment_grid_main_element_pic);

        txtTitle.setText(drawerMainElements[position]);
        imageView.setImageResource(drawerMainElementsPics[position]);

        return rowView;
    }

    // возвращает содержимое выделенного элемента списка
    public String GetItem(int position) {
        return drawerMainElements[position];
    }


    public void addElements (String[] drawerMainElements, Integer[] drawerMainElementsPics) {
        this.drawerMainElements = drawerMainElements;
        this.drawerMainElementsPics = drawerMainElementsPics;
    }
    public int getCount() {
        return this.drawerMainElementsPics.length;
    }

}
