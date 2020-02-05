package com.arsoft.newsfeed.adapters

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.arsoft.newsfeed.data.models.IAttachment

class MultipleSpanGridLayoutManager(context: Context, spanCount: Int, spanList: ArrayList<IAttachment>): GridLayoutManager(context, spanCount) {
    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (spanList.count()) {
                    1 -> return 4
                    2 -> return 2
                    3 -> return when (position) {
                        0 -> 4
                        else -> 2
                    }
                    4 -> return 2
                    5 -> return when (position) {
                        0 -> 4
                        else -> 1
                    }
                    6 -> return when(position) {
                        0, 1 -> 2
                        else -> 1
                    }
                    7 -> return when (position) {
                        0 -> 4
                        1, 2 -> 2
                        else -> 1
                    }
                    8 -> return 1
                    9 -> return when(position) {
                        0 -> 4
                        else -> 1
                    }
                    10 -> return when(position) {
                        0, 1 -> 2
                        else -> 1
                    }
                    else -> return 1
                }
            }
        }
    }
}