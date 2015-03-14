package com.danilaeremin.nicecoffee.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danilaeremin.nicecoffee.MainActivity;
import com.danilaeremin.nicecoffee.R;
import com.danilaeremin.nicecoffee.core.ProductContract;
import com.danilaeremin.nicecoffee.core.Utils;
import com.danilaeremin.nicecoffee.data.models.ProductModel;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class ProductFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_PRODUCT_ID = "product_id";

    private static final String SAVED_PRODUCT_ID = "Saved_product_id";

    private static final int PRODUCT_DATA_LOADER = 0;

    private int product_id;

    private TextView mProductName = null;
    private TextView mProductDesc = null;
    private ImageView mProductPic = null;

    public static ProductFragment newInstance(Integer sectionNumber) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        this.mProductDesc = (TextView)rootView.findViewById(R.id.product_desc);
        this.mProductName = (TextView)rootView.findViewById(R.id.product_name);
        this.mProductPic = (ImageView)rootView.findViewById(R.id.product_pic);

        this.product_id = getArguments().getInt(ARG_PRODUCT_ID);
        if (savedInstanceState != null) {
            product_id = savedInstanceState.getInt(SAVED_PRODUCT_ID);
        }

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PRODUCT_ID, product_id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PRODUCT_DATA_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case PRODUCT_DATA_LOADER: {
                Uri productWithId =
                        ProductContract.ProductEntry.buildProductUri(product_id);

                return new CursorLoader(getActivity(),
                        productWithId,
                        null,
                        null,
                        null,
                        null);
            }
            default: {

                return null;
            }
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Utils.setActivityName(
                (MainActivity)getActivity(),
                getString(R.string.drawer_head)
        );

        if (data.getCount() > 0) {
            data.moveToFirst();
            ProductModel current = new ProductModel(data);

            Utils.setActivityName((MainActivity) getActivity(), current.getName());

            this.mProductDesc.setText(Html.fromHtml(current.getDescription()));
            this.mProductName.setText(current.getName());

            this.mProductPic.setImageResource(R.drawable.coffe_big);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        this.mProductDesc.setText("");
        this.mProductName.setText("");

        this.mProductPic.setImageResource(R.drawable.coffe_big);
    }
}
