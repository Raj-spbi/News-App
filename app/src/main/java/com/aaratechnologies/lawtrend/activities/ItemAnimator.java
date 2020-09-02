package com.aaratechnologies.lawtrend.activities;

import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAnimator extends DefaultItemAnimator
{
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        if(oldHolder != null)
        {
            oldHolder.itemView.setVisibility(View.INVISIBLE);
            dispatchChangeFinished(oldHolder, true);
        }

        if(newHolder != null)
        {
            dispatchChangeFinished(newHolder, false);
        }

        return false;
    }
}