package me.deepak.productdetails.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.deepak.api.search.viewmodels.Products;
import me.deepak.productdetails.R;

/**
 * Created by Deepak Mishra
 */

public class ProductDetailItemView extends CardView {

    private TextView productName;
    private ImageView productImage;

    private Products.Product productData;

    public ProductDetailItemView(Context context) {
        super(context);
        init(context);
    }

    public ProductDetailItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.product_detail_item_view, this);
        setRadius(getResources().getDimension(R.dimen.cardRadius));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getResources().getDimension(R.dimen.cardElevation));
        }
        productName = findViewById(R.id.productName);
        productImage = findViewById(R.id.productImage);
    }


    public void setProduct(Products.Product productData) {
        this.productData = productData;
        productName.setText(productData.name);
        Picasso
                .with(getContext())
                .load(productData.imageUrl)
                .into(productImage);
    }


}
