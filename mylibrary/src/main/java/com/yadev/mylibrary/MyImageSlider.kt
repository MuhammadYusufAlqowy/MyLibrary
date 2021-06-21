package com.yadev.mylibrary

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.yadev.mylibrary.adapter.BaseMyImageSliderAdapter
import com.yadev.mylibrary.databinding.LayoutMyImageSliderBinding
import java.util.*
import kotlin.concurrent.timerTask


class MyImageSlider(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var startX: Int = 0
    private var startY: Int = 0
    private val layout =
        LayoutMyImageSliderBinding.inflate(LayoutInflater.from(context), this, false)
    var scrollDuration: Long
    var isInfinte = true
    var isAutoScroll = true
    var pageMargin: Int
    var pageOffset: Int
    var timer: Timer = Timer()
    val update: Runnable? = null
    var totalItemCount: Int = 0
    var sliderMinHeight: Int = 0
    var indicatorPaddingTop: Int = 0
    var indicatorPaddingBottom: Int = 0
    var allowIntercept = false

    private lateinit var rclv: RecyclerView

    init {
        addView(layout.root)
        layout.apply {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.MyImageSlider)
            isAutoScroll = attr.getBoolean(R.styleable.MyImageSlider_sliderAutoScroll, true)
            scrollDuration =
                attr.getInt(R.styleable.MyImageSlider_sliderScrollDuration, 3000).toLong()
            val enableIndicator =
                attr.getBoolean(R.styleable.MyImageSlider_sliderEnableIndicator, true)
            allowIntercept =
                attr.getBoolean(R.styleable.MyImageSlider_sliderDisallowIntercept, false)
            val indicatorStyle = attr.getInt(R.styleable.MyImageSlider_sliderIndicatorType, 0)
            val indicatorPaddingStart = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderIndicatorPaddingStart,
                resources.getDimensionPixelSize(R.dimen._4adp)
            )
            val indicatorPaddingEnd = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderIndicatorPaddingEnd,
                resources.getDimensionPixelSize(R.dimen._4adp)
            )
            indicatorPaddingBottom = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderIndicatorPaddingBottom,
                resources.getDimensionPixelSize(R.dimen._4adp)
            )
            var paddingTop = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderIndicatorPaddingTop,
                resources.getDimensionPixelSize(R.dimen._4adp)
            )

            val indicatorPosition =
                attr.getInt(R.styleable.MyImageSlider_sliderIndicatorPosition, 0)
            val indicatorSize = attr.getDimension(
                R.styleable.MyImageSlider_sliderIndicatorSize,
                resources.getDimension(R.dimen._7adp)
            )
            sliderMinHeight = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderMinHeight,
                0
            )
            val indicatorSpacing = attr.getDimension(
                R.styleable.MyImageSlider_sliderIndicatorSpacing,
                resources.getDimension(R.dimen._2adp)
            )
            val indicatorActiveColor = attr.getColor(
                R.styleable.MyImageSlider_sliderIndicatorActiveColor,
                context.getColorRes(R.color.info)
            )
            val indicatorDisableColor = attr.getColor(
                R.styleable.MyImageSlider_sliderIndicatorDisableColor,
                context.getColorRes(R.color.gray)
            )
            pageMargin = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderMargin,
                0
            )
            pageOffset = attr.getDimensionPixelSize(
                R.styleable.MyImageSlider_sliderOffset,
                0
            )
            wormIndicator.setDotIndicatorColor(indicatorActiveColor)
            wormIndicator.setStrokeDotsIndicatorColor(indicatorDisableColor)
            dotIndicator.selectedDotColor = indicatorActiveColor
            dotIndicator.dotsColor = indicatorDisableColor
            springIndicator.setDotIndicatorColor(indicatorActiveColor)
            springIndicator.setStrokeDotsIndicatorColor(indicatorDisableColor)
            dotIndicator.setDotSize(indicatorSize)
            springIndicator.setDotSize(indicatorSize)
            wormIndicator.setDotSize(indicatorSize)
            dotIndicator.setDotSpacing(indicatorSpacing)
            springIndicator.setDotSpacing(indicatorSpacing)
            wormIndicator.setDotSpacing(indicatorSpacing)

            rclv = viewPager.getChildAt(0) as RecyclerView
            rclv.overScrollMode = OVER_SCROLL_NEVER

            if (!enableIndicator) {
                lIndicator.visibility = GONE
            }
            indicatorPaddingTop = 0
            when (indicatorPosition) {
                0 -> {
                    indicatorPaddingTop = paddingTop
                    lIndicator.updateLayoutParams<RelativeLayout.LayoutParams> {
                        addRule(RelativeLayout.BELOW, viewPager.id)
                    }
                }
                1 -> lIndicator.gravity = Gravity.START
                2 -> lIndicator.gravity = Gravity.CENTER
                3 -> lIndicator.gravity = Gravity.END
            }

            when (indicatorStyle) {
                0 -> {
                    wormIndicator.visibility = View.GONE
                    springIndicator.visibility = View.GONE
                }
                1 -> {
                    wormIndicator.visibility = View.GONE
                    dotIndicator.visibility = View.GONE
                }
                2 -> {
                    springIndicator.visibility = View.GONE
                    dotIndicator.visibility = View.GONE
                }
            }
            lIndicator.setPadding(
                indicatorPaddingStart,
                indicatorPaddingTop,
                indicatorPaddingEnd,
                indicatorPaddingBottom
            )
            if (sliderMinHeight > 0) {
                viewPager.updateLayoutParams {
                    height = sliderMinHeight
                }
                setMeasuredDimension(
                    measuredWidth,
                    sliderMinHeight + indicatorPaddingBottom + indicatorPaddingTop
                )
            }
            attr.recycle()
        }
    }

    fun <T, V : ViewBinding> setAdapter(adapter: BaseMyImageSliderAdapter<T, V>): BaseMyImageSliderAdapter<T, V> {
        layout.apply {
            viewPager.apply {
                setAdapter(adapter)
                totalItemCount = adapter.itemCount
                orientation = ORIENTATION_HORIZONTAL
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
                updatePagerHeightForChild(viewPager.get(0), viewPager, sliderMinHeight)
                if (adapter.itemCount > 1) {
                    adapter.pageOffset = pageOffset
                    adapter.pageMargin = pageMargin
                    setCurrentItem(1, false)
                    setCurrentItem(0, true)

                    val pageMarginPx = pageMargin
                    val offsetPx = pageOffset
                    setPageTransformer { page, position ->
                        updatePagerHeightForChild(page, this, sliderMinHeight)
                        if (pageOffset > 0 || pageMargin > 0) {
                            val viewPager = page.parent.parent as ViewPager2
                            var offset = position * -(2 * offsetPx + pageMarginPx)
                            if (currentItem == 0 && position == 0f) offset = -offsetPx.toFloat()
                            if (currentItem == 0 && position == 1f) offset =
                                position * -(3 * offsetPx + pageMarginPx)
                            if (currentItem == totalItemCount - 1 && position == -1f) offset =
                                position * -(3 * offsetPx + pageMarginPx)
                            if (currentItem == totalItemCount - 1 && position == 0f) offset =
                                offsetPx.toFloat()

                            if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                                    page.translationX = -offset
                                } else {
                                    page.translationX = offset
                                }
                            } else {
                                page.translationY = offset
                            }
                        }
                    }

                    if (isAutoScroll) {
                        var currentPage = currentItem
                        val update = Runnable {
                            viewPager.setCurrentItem(currentPage % adapter.itemCount, true)
                            currentPage++
                        }
                        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                Handler(Looper.getMainLooper()).removeCallbacks(update)
                                currentPage = position
                            }
                        })

                        val timerTask = timerTask {
                            Handler(Looper.getMainLooper()).post(update)
                        }
                        timer.schedule(timerTask, scrollDuration, scrollDuration)
                    }
                    wormIndicator.setViewPager2(viewPager)
                    dotIndicator.setViewPager2(viewPager)
                    springIndicator.setViewPager2(viewPager)
                }
            }
        }
        return adapter
    }

    inner class InfiniteScrollBehaviour(
        private val itemCount: Int,
        private val layoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {
        override fun onScrolled(
            recyclerView: RecyclerView, dx: Int, dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            val lastItemVisible = layoutManager.findLastVisibleItemPosition()
            if (firstItemVisible == (itemCount - 1) && dx > 0) {
                recyclerView.scrollToPosition(1)
            } else if (lastItemVisible == 0 && dx < 0) {
                recyclerView.scrollToPosition(itemCount - 2)
            }
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        requestDisallowInterceptTouchEvent(allowIntercept)
        return super.onInterceptTouchEvent(e)
    }

    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (disY > disX) {
            if (layout.viewPager.currentItem == 0 && endY - startY > 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(
                    layout.viewPager.currentItem != 0 || endY - startY >= 0
                )
            }

        } else if (disX > disY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (disX > disY) {
            if (layout.viewPager.currentItem == 0 && endX - startX > 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(
                    (layout.viewPager.currentItem != 0 || endX - startX >= 0)
                )
            }
        } else {
            parent.requestDisallowInterceptTouchEvent(true)
        }

    }

    private fun updatePagerHeightForChild(view: View, pager: ViewPager2, sliderMinHeight: Int) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            if (sliderMinHeight <= 0) {
                pager.layoutParams =
                    (pager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                pager.invalidate()
                setMeasuredDimension(
                    measuredWidth,
                    view.measuredHeight + indicatorPaddingBottom + indicatorPaddingTop
                )
            } else {
                if (pager.height > view.measuredHeight) {
                    pager.layoutParams =
                        (pager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                    pager.invalidate()
                    setMeasuredDimension(
                        measuredWidth,
                        view.measuredHeight + indicatorPaddingBottom + indicatorPaddingTop
                    )
                } else if ((view.measuredHeight.toFloat() / pager.height) <= 1.5f) {
                    pager.layoutParams =
                        (pager.layoutParams).also { lp -> lp.height = view.measuredHeight }
                    pager.invalidate()
                    setMeasuredDimension(
                        measuredWidth,
                        view.measuredHeight + indicatorPaddingBottom + indicatorPaddingTop
                    )
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            sliderMinHeight + indicatorPaddingBottom + indicatorPaddingTop
        )
    }
}