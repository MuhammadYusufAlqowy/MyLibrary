package com.embriodigital.pniuser.utils

import android.graphics.*
import android.graphics.Shader.TileMode
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.Nullable


class TileDrawable(drawable: Drawable, tileMode: TileMode?) : Drawable() {
    private val paint: Paint
    override fun draw(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.setAlpha(alpha)
    }

    override fun setColorFilter(@Nullable colorFilter: ColorFilter?) {
        paint.setColorFilter(colorFilter)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) return drawable.bitmap
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    init {
        paint = Paint()
        paint.setShader(BitmapShader(getBitmap(drawable), tileMode!!, tileMode))
    }
}