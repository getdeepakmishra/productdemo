package me.deepak.navigation;

/**
 * Created by Deepak Mishra
 */

public interface ProductNavigator {

    void updateSelectedPosition(int productIndex);
    void scrollToSelectedPosition(int productIndex);
}
