package me.deepak.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.deepak.productdetails.fragments.ProductDetailFragment;
import me.deepak.productlist.fragments.ProductListFragment;

/**
 * Created by Deepak Mishra
 */

public class SliderAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 2;
    public static final int LIST_FRAGMENT_POSITION = 0;
    public static final int DETAIL_FRAGMENT_POSITION = 1;

    private ProductListFragment productListFragment;
    private ProductDetailFragment productDetailFragment;

    public SliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == LIST_FRAGMENT_POSITION) {
//            productListFragment = new ProductListFragment();
//            return productListFragment;
//                return new ProductListFragment();
                if(productListFragment == null) {
                    productListFragment = new ProductListFragment();
                }
                return productListFragment;
        }else if(position == DETAIL_FRAGMENT_POSITION) {
//                return new ProductDetailFragment();
//            productDetailFragment = new ProductDetailFragment();
//            return productDetailFragment;

                if(productDetailFragment == null) {
                    productDetailFragment = new ProductDetailFragment();
                }
                return productDetailFragment;
        }
        else return null;
    }


    public ProductListFragment getProductListFragment() {
        return (ProductListFragment) getItem(LIST_FRAGMENT_POSITION);
    }

    public ProductDetailFragment getProductDetailFragment() {
        return (ProductDetailFragment) getItem(DETAIL_FRAGMENT_POSITION);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}