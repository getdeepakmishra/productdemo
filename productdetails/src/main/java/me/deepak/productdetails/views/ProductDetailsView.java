package me.deepak.productdetails.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import me.deepak.androidutils.DeviceInfo;
import me.deepak.api.exceptions.InProgressException;
import me.deepak.api.exceptions.NoMoreDataException;
import me.deepak.api.search.callbacks.SearchApiInteractor;
import me.deepak.api.search.viewmodels.Products;
import me.deepak.navigation.ProductNavigator;
import me.deepak.productdetails.R;

/**
 * Created by Deepak Mishra
 */

public class ProductDetailsView extends RecyclerView {

    private static final String TAG = "ProductDetailsView";

    private ProductListViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    private SearchApiInteractor searchApiInteractor;
    @Nullable private ProductNavigator productNavigator;

    private SnapHelper snapHelper;

    private static final int PREFETCH_INDEX = 5;

    public ProductDetailsView(Context context) {
        super(context);
        init();
    }

    public ProductDetailsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        if(DeviceInfo.isInPortrait()){
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        setLayoutManager(layoutManager);
        adapter = (new ProductListViewAdapter());
        setAdapter(adapter);
        ScrollListener scrollListener = new ScrollListener();
        addOnScrollListener(scrollListener);

        if(DeviceInfo.isInPortrait()){
            snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(this);
        }

    }

    public void setProductError(Exception exception) {
        Log.d(TAG, "setProductError() called with: exception = [" + exception + "]");
        if(exception instanceof InProgressException){
            showDebugInfo("DebugInfo: Loading in progress!");
        } else if (exception instanceof NoMoreDataException){
            // TODO: 10/7/17 replace with another view type in list
            showDebugInfo("DebugInfo: No more data!");
        } else {
            showDebugInfo("DebugInfo: Error - " + exception.getMessage());
        }
    }

    public void setSelectedItem(int index) {
        Log.d(TAG, "setSelectedItem() called with: index = [" + index + "]");
        if(index < 0) return;
        if(index < ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition() ||
                (index > ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition())) {
            if(DeviceInfo.isInLandscape()) smoothScrollToPosition(index);
            else scrollToPosition(index);
        }
    }

    private void showDebugInfo(String message){
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show();
    }

    private class ScrollListener extends OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG, "onScrolled() called with dx = " + dx);

            int itemsFetched = layoutManager.getItemCount();
            int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
            if(!searchApiInteractor.isLoading() &&
                    (lastVisiblePosition + PREFETCH_INDEX >= itemsFetched)){
                Log.d(TAG, "onScrolled: loadMoreSearchResults");
                searchApiInteractor.loadMoreSearchResults();
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, "onScrollStateChanged() called with newState = [" + newState + "]");
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                Log.d(TAG, "onScrollStateChanged: idle " + layoutManager.findFirstVisibleItemPosition());
                if (DeviceInfo.isInPortrait() && productNavigator != null) {
                    productNavigator.updateSelectedPosition(layoutManager.findFirstVisibleItemPosition());
                }
            }
        }
    }


    public class ProductListViewAdapter extends Adapter<ProductListViewAdapter.BaseViewHolder> {


        private static final int PRODUCT_VIEW_TYPE = 1 ;
        private static final int PROGRESS_VIEW_TYPE = 2 ;

        private List<Products.Product> productList;

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case PRODUCT_VIEW_TYPE:
                    ProductDetailItemView productDetailItemView = new ProductDetailItemView(getContext());
                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    int margin = (int) getResources().getDimension(R.dimen.cardMargin);
                    layoutParams.setMargins(margin, margin, margin, margin);
                    productDetailItemView.setLayoutParams(layoutParams);
                    return new ProductViewHolder(productDetailItemView);
                case PROGRESS_VIEW_TYPE:
                    return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).
                            inflate(R.layout.progress_item, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            switch (getItemViewType(position)){
                case PRODUCT_VIEW_TYPE:
                    ((ProductDetailItemView)((ProductViewHolder)holder).itemView).setProduct(
                            productList.get(position));
                    return;
                case PROGRESS_VIEW_TYPE:
                    ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
                    return;
            }

        }

        @Override
        public int getItemCount() {
            return productList == null ? 0 : productList.size();
        }

//      NOTE SURE IF PRODUCT IDs SENT FROM THE SERVER ARE UNIQUE - THIS CAN HELP OPTIMIZE!
//        @Override
//        public void setHasStableIds(boolean hasStableIds) {
//            super.setHasStableIds(true);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return productList.get(position) == null ? -2 : Integer.parseInt(productList.get(position).id);
//        }

        @Override
        public int getItemViewType(int position) {
            Log.d(TAG, "getItemViewType() called with: position = [" + position + "]");
            return (productList.get(position) == null) ? PROGRESS_VIEW_TYPE : PRODUCT_VIEW_TYPE;
        }

        abstract class BaseViewHolder extends ViewHolder{
            BaseViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class ProductViewHolder extends BaseViewHolder{

            ProductViewHolder(ProductDetailItemView productView) {
                super(productView);
            }
        }

        private class ProgressViewHolder extends BaseViewHolder{

            ProgressBar progressBar;
            ProgressViewHolder(View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.progressBar);
            }
        }

        public void resetProducts(final List<Products.Product> updatedProductList){

            Log.e(TAG, "updatedProductList size: " + updatedProductList.size());
            if(productList == null){
                productList = updatedProductList;
                notifyItemRangeInserted(0, productList.size());
            }

            else {
                int oldSize = productList.size();
                productList = updatedProductList;
                notifyItemChanged(oldSize-1);
                notifyItemRangeInserted(oldSize, productList.size()-oldSize-1);
            }

            Log.e(TAG, "productList size: " + productList.size());
            Log.e(TAG, "-------------------------------------");
        }

    }

    public void setSearchApiInteractor(SearchApiInteractor searchApiInteractor) {
        this.searchApiInteractor = searchApiInteractor;

    }

    public void setProductNavigator(@Nullable ProductNavigator productNavigator) {
        this.productNavigator = productNavigator;
    }

    public void setProductData(final List<Products.Product> newProductList) {
        adapter.resetProducts(newProductList);
    }

}


