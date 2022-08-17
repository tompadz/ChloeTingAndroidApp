package com.xlt.chloeting.ui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class SpacesItemDecoration(private val topSpace : Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect : Rect,
        view : View,
        parent : RecyclerView,
        state : RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 1) outRect.top = topSpace
    }
}