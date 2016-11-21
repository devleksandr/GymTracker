package com.carrickane.gymtracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by carrickane on 16.11.2016.
 */

public class ItemDivider extends RecyclerView.ItemDecoration {

    private final Drawable divider;

    public ItemDivider(Context context) {
        int[] attrs = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //initializing edges of view element
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount() - 1; ++i) {
            View item = parent.getChildAt(i);
            int top = item.getBottom() + ((RecyclerView.LayoutParams)item.getLayoutParams())
                    .bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();
            //setting bounds
            divider.setBounds(left, top, right, bottom + 2);
            //drawing
            divider.draw(c);
        }
    }
}
