package com.androidmodule.audiomoduleexample.utils

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView


object Utils {
    class MarginItemDecoration(private val spaceSize: Int, private val context: Context) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val spaceSizeDp = convertPixelsToDp(spaceSize.toFloat(), context).toInt()
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceSizeDp
                }
                left = spaceSizeDp
                right = spaceSizeDp
                bottom = spaceSizeDp
            }
        }
    }

    class SpacingItemDecoration(
        private val spanCount: Int,
        private val spacingPx: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacingPx - column * spacingPx / spanCount
                outRect.right = (column + 1) * spacingPx / spanCount
                if (position < spanCount) { // top edge
                    outRect.top = spacingPx
                }
                outRect.bottom = spacingPx // item bottom
            } else {
                outRect.left = column * spacingPx / spanCount
                outRect.right = spacingPx - (column + 1) * spacingPx / spanCount
                if (position >= spanCount) {
                    outRect.top = spacingPx // item top
                }
            }
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}