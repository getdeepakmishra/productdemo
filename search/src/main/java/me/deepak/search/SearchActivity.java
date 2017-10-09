package me.deepak.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import me.deepak.androidutils.DeviceInfo;
import me.deepak.navigation.FragmentNavigator;
import me.deepak.productdetails.fragments.ProductDetailFragment;
import me.deepak.productlist.fragments.ProductListFragment;

/**
 * Created by Deepak Mishra
 */

public class SearchActivity extends AppCompatActivity implements FragmentNavigator{

    private static final String TAG = "SearchActivity";

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if(DeviceInfo.isInPortrait()){
            setupViewPager();
        } else {
            setupFragments();
        }
    }

    private void setupFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.listFragmentHolder, new ProductListFragment());
        fragmentTransaction.replace(R.id.detailFragmentHolder, new ProductDetailFragment());
        fragmentTransaction.commit();
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new SliderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void switchFragment(FragmentType toFragment, int productIndex) {
        if(!DeviceInfo.isInPortrait()) return;

        if(toFragment.ordinal() == viewPager.getCurrentItem()) return;
        if(viewPager.getCurrentItem() == SliderAdapter.LIST_FRAGMENT_POSITION) {
            viewPager.setCurrentItem(SliderAdapter.DETAIL_FRAGMENT_POSITION, true);
            ProductDetailFragment productDetailFragment = ((SliderAdapter)viewPager.getAdapter()).getProductDetailFragment();
            if(productDetailFragment.isAdded()){
                productDetailFragment.scrollToSelectedPosition(productIndex);
            }
        }
        else if(viewPager.getCurrentItem() == SliderAdapter.DETAIL_FRAGMENT_POSITION) {
            viewPager.setCurrentItem(SliderAdapter.LIST_FRAGMENT_POSITION, true);
            ProductListFragment productListFragment = ((SliderAdapter)viewPager.getAdapter()).getProductListFragment();
            if(productListFragment.isAdded()){
                productListFragment.scrollToSelectedPosition(productIndex);
            }
        }

    }



    @Override
    public void onBackPressed() {
        if(DeviceInfo.isInPortrait()){
            if(viewPager.getCurrentItem() == SliderAdapter.LIST_FRAGMENT_POSITION) super.onBackPressed();
            else viewPager.setCurrentItem(SliderAdapter.LIST_FRAGMENT_POSITION);
        } else {
            super.onBackPressed();
        }

    }
}
