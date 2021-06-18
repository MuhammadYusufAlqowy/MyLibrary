package com.yadev.mylibrary

import android.view.ViewGroup

const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
const val GALLERY = 1
const val CAMERA = 2
const val DEFAULT = 0

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