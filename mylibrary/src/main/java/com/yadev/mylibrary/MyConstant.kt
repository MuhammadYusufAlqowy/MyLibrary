package com.yadev.mylibrary

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
const val GALLERY = 1
const val CAMERA = 2
const val DEFAULT = 0
const val GONE = View.GONE
const val VISIBLE: Int = View.VISIBLE
const val INVISIBLE = View.INVISIBLE
const val VERTICAL = RecyclerView.VERTICAL
const val HORIZONTAL = RecyclerView.HORIZONTAL

sealed class MyBitmapTransformation {
    object FIT_CENTER : MyBitmapTransformation()
    object CENTER_CROP : MyBitmapTransformation()
    object CENTER_INSIDE : MyBitmapTransformation()
    object CIRCLE_CROP : MyBitmapTransformation()
}

sealed class MyDiskCacheStrategy {
    object ALL : MyDiskCacheStrategy()
    object NONE : MyDiskCacheStrategy()
    object AUTO : MyDiskCacheStrategy()
}