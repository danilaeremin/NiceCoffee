package com.danilaeremin.nicecoffee.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.danilaeremin.nicecoffee.Adapters.ProductCellAdapter;
import com.danilaeremin.nicecoffee.Adapters.SubcategoryAdapter;
import com.danilaeremin.nicecoffee.MainActivity;
import com.danilaeremin.nicecoffee.R;
import com.danilaeremin.nicecoffee.core.ProductContract;
import com.danilaeremin.nicecoffee.core.Utils;
import com.danilaeremin.nicecoffee.data.models.CategoryModel;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class CategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    final String LOG_TAG = CategoryFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_ID = "category_id";

    private static final String SAVED_CATEGORY_ID = "Saved_category_id";

    private static final int PRODUCT_LIST_LOADER = 0;
    private static final int SUBCATEGORY_LIST_LIST_LOADER = 1;
    private static final int HEAD_LOADER = 2;

    private Integer category_id;
    private int subcategory_id;
    private int product_id = -1;


    private ProductCellAdapter mProductCellAdapter;
    private SubcategoryAdapter mSubcategoryAdapter;

    public static CategoryFragment newInstance(Integer sectionNumber) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.category_id = getArguments().getInt(ARG_CATEGORY_ID);
        if (savedInstanceState != null) {
            category_id = savedInstanceState.getInt(SAVED_CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        mProductCellAdapter = new ProductCellAdapter(getActivity(), null, 0);

        final GridView products = (GridView) rootView.findViewById(R.id.product_list_grid);
        products.setAdapter(mProductCellAdapter);
        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                product_id = mProductCellAdapter.getProduct(position).getProductId();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProductFragment.newInstance(
                                        product_id)
                        )
                        .addToBackStack(getString(R.string.back_stack_tag))
                        .commit();

            }
        });

        mSubcategoryAdapter = new SubcategoryAdapter(getActivity(), null, 0);

        final GridView subcategories = (GridView) rootView.findViewById(R.id.subcategory_grid);
        subcategories.setAdapter(mSubcategoryAdapter);
        subcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                subcategory_id = mSubcategoryAdapter.getCategory(position).getCategoryId();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CategoryFragment.newInstance(subcategory_id))
                        .addToBackStack(getString(R.string.back_stack_tag))
                        .commit();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CATEGORY_ID, category_id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(HEAD_LOADER, null, this);
        getLoaderManager().initLoader(PRODUCT_LIST_LOADER, null, this);
        getLoaderManager().initLoader(SUBCATEGORY_LIST_LIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
        }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case PRODUCT_LIST_LOADER: {
                Uri productsForCategory =
                        ProductContract.ProductEntry.buildProductUriWithCategory(category_id);

                return new CursorLoader(getActivity(),
                        productsForCategory,
                        null,
                        null,
                        null,
                        null);
            }
            case SUBCATEGORY_LIST_LIST_LOADER: {
                Uri subcategoryListForCategory =
                        ProductContract.CategoriesEntry.buildCategoryUriWithParent(category_id);

                return new CursorLoader(getActivity(),
                        subcategoryListForCategory,
                        null,
                        null,
                        null,
                        null);
            }
            case HEAD_LOADER: {
                Uri categoryWithId =
                        ProductContract.CategoriesEntry.buildCategoryUri(category_id);

                return new CursorLoader(getActivity(),
                        categoryWithId,
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
        switch (loader.getId()) {
            case PRODUCT_LIST_LOADER: {
                mProductCellAdapter.swapCursor(data);
                break;
            }
            case SUBCATEGORY_LIST_LIST_LOADER: {
                mSubcategoryAdapter.swapCursor(data);

                break;
            }
            case HEAD_LOADER: {

                if (data.getCount() > 0) {
                    data.moveToFirst();
                    CategoryModel current = new CategoryModel(data);

                    Utils.setActivityName((MainActivity)getActivity(),current.getName());
                } else {
                    Utils.setActivityName(
                            (MainActivity)getActivity(),
                            getString(R.string.drawer_head)
                    );
                }
            }
            default: {

                break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case PRODUCT_LIST_LOADER: {
                mProductCellAdapter.swapCursor(null);
                break;
            }
            case SUBCATEGORY_LIST_LIST_LOADER: {
                mSubcategoryAdapter.swapCursor(null);
                break;
            }
            case HEAD_LOADER: {

            }
            default: {

                break;
            }
        }
    }

//    public static int convertDpToPixels(float dp, Context context){
//        Resources resources = context.getResources();
//        return (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                dp,
//                resources.getDisplayMetrics()
//        );
//    }
}
