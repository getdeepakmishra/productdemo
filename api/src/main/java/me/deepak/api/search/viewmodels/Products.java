package me.deepak.api.search.viewmodels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Mishra
 */

public class Products {

    public List<Product> productList;
    public int totalProducts;

    public Products() {
        this.productList = new ArrayList<>();
        this.totalProducts = -1;
    }

    public static class Product {

        public String id;
        public String name;
        public String shortDesc;
        public String longDesc;
        public String price;
        public String imageUrl;
        public double rating;
        public int reviewCount;
        public boolean inStock;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Product product = (Product) o;

            if (Double.compare(product.rating, rating) != 0) return false;
            if (reviewCount != product.reviewCount) return false;
            if (inStock != product.inStock) return false;
            if (!id.equals(product.id)) return false;
            if (!name.equals(product.name)) return false;
            if (shortDesc != null ? !shortDesc.equals(product.shortDesc) : product.shortDesc != null)
                return false;
            if (longDesc != null ? !longDesc.equals(product.longDesc) : product.longDesc != null)
                return false;
            if (price != null ? !price.equals(product.price) : product.price != null) return false;
            return imageUrl.equals(product.imageUrl);

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = id.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + (shortDesc != null ? shortDesc.hashCode() : 0);
            result = 31 * result + (longDesc != null ? longDesc.hashCode() : 0);
            result = 31 * result + (price != null ? price.hashCode() : 0);
            result = 31 * result + imageUrl.hashCode();
            temp = Double.doubleToLongBits(rating);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + reviewCount;
            result = 31 * result + (inStock ? 1 : 0);
            return result;
        }
    }


}
