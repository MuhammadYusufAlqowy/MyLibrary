package com.yadev.mylibrary

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.yadev.mylibrary.databinding.LayoutBadgeImageViewBinding

class BadgeImageView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val layout =
        LayoutBadgeImageViewBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(layout.root)
        layout.apply {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.BadgeImageView)
            val badgeDrawable = attr.getDrawable(R.styleable.BadgeImageView_badgeDrawable)
            val badgeNumber = attr.getInteger(R.styleable.BadgeImageView_badgeNumber,0)
            val strokeWidth = attr.getDimensionPixelSize(R.styleable.BadgeImageView_badgeStrokeWidth,context.resources.getDimensionPixelSize(R.dimen._1adp))
            val strokeColor = attr.getColor(R.styleable.BadgeImageView_badgeStrokeColor,Color.WHITE)
            val badgeSize = attr.getDimensionPixelSize(R.styleable.BadgeImageView_badgeSize,context.resources.getDimensionPixelSize(R.dimen._24adp))
            val badgeDrawableTint = attr.getColor(R.styleable.BadgeImageView_badgeDrawableTint, Color.BLACK)
            imgIcon.setImageDrawable(badgeDrawable)
            imgIcon.imageTintList = ColorStateList.valueOf(badgeDrawableTint)
            imgIcon.updateLayoutParams<ViewGroup.LayoutParams> {
                width = badgeSize
                height = badgeSize
            }
            /*imgIcon.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = context.resources.getDimensionPixelSize(R.dimen._9_minus_adp)
            }*/
            tvBadge.text = badgeNumber.toString()
            (tvBadge.background as GradientDrawable).setStroke(strokeWidth,strokeColor)
            if (badgeNumber >= 100){
                layout.tvBadge.text = "99+"
            }
            if (badgeNumber == 0){
                layout.tvBadge.visibility = View.GONE
            }else{
                layout.tvBadge.visibility = View.VISIBLE
            }

            attr.recycle()
        }
    }

    fun setBadgeDrawable(drawable: Drawable) {
        layout.imgIcon.setImageDrawable(drawable)
    }

    fun setBadgeNumber(number:Int){
        layout.tvBadge.text = number.toString()
        if (number >= 100){
            layout.tvBadge.text = "99+"
        }
        if (number == 0){
            layout.tvBadge.visibility = View.GONE
        }else{
            layout.tvBadge.visibility = View.VISIBLE
        }
    }

}