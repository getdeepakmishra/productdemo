package me.deepak.productdetails.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import me.deepak.api.search.callbacks.SearchApiInteractor;
import me.deepak.api.search.viewmodels.Products;
import me.deepak.navigation.ProductNavigator;
import me.deepak.productdetails.R;
import me.deepak.productdetails.views.ProductDetailsView;
import me.deepak.viewmodels.ProductViewModel;

/**
 * Created by Deepak Mishra
 */

public class ProductDetailFragment extends Fragment implements SearchApiInteractor, ProductNavigator {

    private static final String TAG = "ProductDetailFragment";

    private ProductDetailsView productListView;
    private ProgressBar progressBar;

    private ProductViewModel productViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        productListView = view.findViewById(R.id.productListView);
        progressBar = view.findViewById(R.id.searchProgress);
        productListView.setSearchApiInteractor(this);
        productListView.setProductNavigator(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeProductUpdates();
        if(hasProductData()){
            setProductData(productViewModel.getSearchResults().productList);
        } else {
            loadMoreSearchResults();
        }

    }

    private void subscribeProductUpdates() {
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        productViewModel.getSearchExceptionAsync().observe(getActivity(), new Observer<Exception>() {
            @Override
            public void onChanged(@Nullable Exception e) {
                Log.d(TAG, "observer.setProductError() called with: e = [" + e + "]");
                setProductError(e);
            }
        });

        productViewModel.getSearchResultsAsync().observe(getActivity(), new Observer<Products>() {
            @Override
            public void onChanged(@Nullable Products products) {
                Log.d(TAG, "observer.setProductData() called with: searchResultsViewModel = [" + products + "]");
                setProductData(products.productList);
            }
        });
        productViewModel.getSelectedIndexAsync().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer index) {
                Log.d(TAG, "observer.setSelectedItem() called with: index = [" + index + "]");
                productListView.setSelectedItem(index);
            }
        });
    }

    @Override
    public void loadMoreSearchResults() {
        productViewModel.loadMoreSearchResults();
    }

    @Nullable
    @Override
    public List<Products.Product> getFetchedSearchResults() {
        if(productViewModel.getSearchResults() == null) return null;
        else return productViewModel.getSearchResults().productList;
    }

    @Override
    public void cancelSearch(){
        productViewModel.cancelPendingCalls();
    }

    @Override
    public boolean isLoading(){
        return productViewModel.isLoading();
    }

    @Override
    public boolean hasMoreData() {
        return productViewModel.hasMoreData();
    }
    public boolean hasProductData() {
        return productViewModel.hasProductData();
    }

    public void setProductData(final List<Products.Product> newProductList) {
        productListView.setProductData(newProductList);
        progressBar.setVisibility(View.GONE);
        productListView.setVisibility(View.VISIBLE);
    }

    public void setProductError(Exception exception) {
        productListView.setProductError(exception);
        progressBar.setVisibility(View.GONE);
        productListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateSelectedPosition(int productIndex) {
        productViewModel.setSelectedIndexAsync(productIndex);
    }

    @Override
    public void scrollToSelectedPosition(int productIndex) {
        productListView.setSelectedItem(productIndex);
    }

}


