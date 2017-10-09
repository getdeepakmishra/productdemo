package me.deepak.api.search.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Deepak Mishra
 */

public class SearchApiResponse {

    @SerializedName("products")
    public List<Product> products;
    @SerializedName("totalProducts")
    public int totalProducts;
    @SerializedName("pageNumber")
    public int pageNumber;
    @SerializedName("pageSize")
    public int pageSize;
    @SerializedName("status")
    public int status;
    @SerializedName("kind")
    public String kind;
    @SerializedName("etag")
    public String etag;

    public static class Product {
        @SerializedName("productId")
        public String productId;
        @SerializedName("productName")
        public String productName;
        @SerializedName("shortDescription")
        public String shortDescription;
        @SerializedName("longDescription")
        public String longDescription;
        @SerializedName("price")
        public String price;
        @SerializedName("productImage")
        public String productImage;
        @SerializedName("reviewRating")
        public double reviewRating;
        @SerializedName("reviewCount")
        public int reviewCount;
        @SerializedName("inStock")
        public boolean inStock;
    }
}
