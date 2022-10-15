package com.creativeduck.mrdaebak.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AllSpaceDecoration(
    private val start: Int,
    private val top: Int,
    private val bottom: Int,
    private val left: Int,
    private val right: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position > start) {
            if (position == 1) {
                outRect.top = top
                outRect.bottom = bottom
                outRect.left = left
                outRect.right = right
            }
            else {
                outRect.top = top
                outRect.left = left
                outRect.right = right
            }
            if (parent.getChildAdapterPosition(view) > start) {
                outRect.left = left
                outRect.right = right
                outRect.top = top
                outRect.bottom = bottom
            }
        }
    }
}