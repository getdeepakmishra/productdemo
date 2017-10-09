package me.deepak.navigation;

/**
 * Created by Deepak Mishra
 */

public interface FragmentNavigator {

    enum FragmentType{
        LIST, DETAIL
    }
    void switchFragment(FragmentType toFragment, int productIndex);
}
