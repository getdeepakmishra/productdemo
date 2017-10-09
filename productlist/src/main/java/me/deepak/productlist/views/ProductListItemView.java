package me.deepak.productlist.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.deepak.api.search.viewmodels.Products;
import me.deepak.productlist.R;

/**
 * Created by Deepak Mishra
 */

public class ProductListItemView extends CardView {

    private TextView productName;
    private ImageView productImage;

    private Products.Product productData;

    public ProductListItemView(Context context) {
        super(context);
        init(context);
    }

    public ProductListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.product_list_item_view, this);
        setRadius(getResources().getDimension(R.dimen.cardRadius));
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
