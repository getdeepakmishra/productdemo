package me.deepak.search;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Deepak Mishra
 */

public class ProductViewPager extends ViewPager {
    private static final String TAG = "ProductViewPager";

    float initialXValue;

    public ProductViewPager(Context context) {
        super(context);
    }

    public ProductViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            initialXValue = event.getX();
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE){
            Log.d(TAG, "onTouchEvent: " + String.valueOf(event.getX() - initialXValue));
            if(event.getX() - initialXValue < 0){
                return false;
            }
        }
        return super.onInterceptTouchEvent(event);
    }
}
