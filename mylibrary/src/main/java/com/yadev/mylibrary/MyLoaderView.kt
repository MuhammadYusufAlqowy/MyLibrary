package com.yadev.mylibrary

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.text.TextUtils
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

interface LoaderView {
    fun setRectColor(rectPaint: Paint?)
    fun invalidate()
    fun valueSet(): Boolean
}


object LoaderConstant {
    val COLOR_DEFAULT_GRADIENT = Color.rgb(245, 245, 245)
    const val MIN_WEIGHT = 0.0f
    const val MAX_WEIGHT = 1.0f
    const val CORNER_DEFAULT = 25
    const val USE_GRADIENT_DEFAULT = false
}

class LoaderController(private val loaderView: LoaderView) :
    ValueAnimator.AnimatorUpdateListener {
    private var rectPaint: Paint? = null
    private var linearGradient: LinearGradient? = null
    private var progress = 0f
    private var valueAnimator: ValueAnimator? = null
    private var widthWeight = LoaderConstant.MAX_WEIGHT
    private var heightWeight = LoaderConstant.MAX_WEIGHT
    private var useGradient = LoaderConstant.USE_GRADIENT_DEFAULT
    private var corners = LoaderConstant.CORNER_DEFAULT
    private fun init() {
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        loaderView.setRectColor(rectPaint)
        setValueAnimator(0.5f, 1f, ObjectAnimator.INFINITE)
    }

    @SuppressLint("DrawAllocation")
    @JvmOverloads
    fun onDraw(
        canvas: Canvas,
        left_pad: Float = 0f,
        top_pad: Float = 0f,
        right_pad: Float = 0f,
        bottom_pad: Float = 0f
    ) {
        val margin_height = canvas.height * (1 - heightWeight) / 2
        rectPaint!!.alpha = (progress * MAX_COLOR_CONSTANT_VALUE).toInt()
        if (useGradient) {
            prepareGradient(canvas.width * widthWeight)
        }
        canvas.drawRoundRect(
            RectF(
                0 + left_pad,
                margin_height + top_pad,
                canvas.width * widthWeight - right_pad,
                canvas.height - margin_height - bottom_pad
            ),
            corners.toFloat(), corners.toFloat(),
            rectPaint!!
        )
    }

    fun onSizeChanged() {
        linearGradient = null
        startLoading()
    }

    private fun prepareGradient(width: Float) {
        if (linearGradient == null) {
            linearGradient = LinearGradient(
                0f, 0f, width, 0f, rectPaint!!.color,
                LoaderConstant.COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR
            )
        }
        rectPaint!!.shader = linearGradient
    }

    fun startLoading() {
        if (valueAnimator != null && !loaderView.valueSet()) {
            valueAnimator!!.cancel()
            init()
            valueAnimator!!.start()
        }
    }

    fun setHeightWeight(heightWeight: Float) {
        this.heightWeight = validateWeight(heightWeight)
    }

    fun setWidthWeight(widthWeight: Float) {
        this.widthWeight = validateWeight(widthWeight)
    }

    fun setUseGradient(useGradient: Boolean) {
        this.useGradient = useGradient
    }

    fun setCorners(corners: Int) {
        this.corners = corners
    }

    private fun validateWeight(weight: Float): Float {
        if (weight > LoaderConstant.MAX_WEIGHT) return LoaderConstant.MAX_WEIGHT
        return if (weight < LoaderConstant.MIN_WEIGHT) LoaderConstant.MIN_WEIGHT else weight
    }

    fun stopLoading() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
            setValueAnimator(progress, 0f, 0)
            valueAnimator!!.start()
        }
    }

    private fun setValueAnimator(begin: Float, end: Float, repeatCount: Int) {
        valueAnimator = ValueAnimator.ofFloat(begin, end)
        valueAnimator?.repeatCount = repeatCount
        valueAnimator?.duration = ANIMATION_CYCLE_DURATION.toLong()
        valueAnimator?.repeatMode = ValueAnimator.REVERSE
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.addUpdateListener(this)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        progress = valueAnimator.animatedValue as Float
        loaderView.invalidate()
    }

    fun removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator!!.removeUpdateListener(this)
            valueAnimator!!.cancel()
        }
        progress = 0f
    }

    companion object {
        private const val MAX_COLOR_CONSTANT_VALUE = 255
        private const val ANIMATION_CYCLE_DURATION = 750 //milis
    }

    init {
        init()
    }
}

class MyImageView : AppCompatImageView, LoaderView {
    private var loaderController: LoaderController? = null
    private var defaultColorResource = 0

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        loaderController = LoaderController(this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyImageView, 0, 0)
        loaderController!!.setUseGradient(
            typedArray.getBoolean(
                R.styleable.MyImageView_miv_use_gradient,
                LoaderConstant.USE_GRADIENT_DEFAULT
            )
        )
        loaderController!!.setCorners(
            typedArray.getInt(
                R.styleable.MyImageView_miv_cornerRadius,
                LoaderConstant.CORNER_DEFAULT
            )
        )
        defaultColorResource = typedArray.getColor(
            R.styleable.MyImageView_miv_backgroundColor, ContextCompat.getColor(
                context, R.color.default_color
            )
        )
        typedArray.recycle()
        resetLoader()
    }

    fun resetLoader() {
        if (drawable != null) {
            super.setImageDrawable(null)
            loaderController!!.startLoading()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController!!.onSizeChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController!!.onDraw(canvas)
    }

    override fun setRectColor(rectPaint: Paint?) {
        rectPaint?.color = defaultColorResource
    }

    override fun valueSet(): Boolean {
        return drawable != null
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        loaderController!!.stopLoading()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        loaderController!!.stopLoading()
    }

    override fun setImageIcon(icon: Icon?) {
        super.setImageIcon(icon)
        loaderController!!.stopLoading()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        loaderController!!.stopLoading()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController!!.removeAnimatorUpdateListener()
    }
}

class MyTextView : AppCompatTextView, LoaderView {
    private var loaderController: LoaderController? = null
    private var defaultColorResource = 0
    private var darkerColorResource = 0

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        loaderController = LoaderController(this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView, 0, 0)
        loaderController!!.setWidthWeight(
            typedArray.getFloat(
                R.styleable.MyTextView_mtv_width_weight,
                LoaderConstant.MAX_WEIGHT
            )
        )
        loaderController!!.setHeightWeight(
            typedArray.getFloat(
                R.styleable.MyTextView_mtv_height_weight,
                LoaderConstant.MAX_WEIGHT
            )
        )
        loaderController!!.setUseGradient(
            typedArray.getBoolean(
                R.styleable.MyTextView_mtv_use_gradient,
                LoaderConstant.USE_GRADIENT_DEFAULT
            )
        )
        loaderController!!.setCorners(
            typedArray.getInt(
                R.styleable.MyTextView_mtv_cornerRadius,
                LoaderConstant.CORNER_DEFAULT
            )
        )
        defaultColorResource = typedArray.getColor(
            R.styleable.MyTextView_mtv_backgroundColor, ContextCompat.getColor(
                context, R.color.default_color
            )
        )
        darkerColorResource = typedArray.getColor(
            R.styleable.MyTextView_mtv_backgroundColor, ContextCompat.getColor(
                context, R.color.default_color
            )
        )
        typedArray.recycle()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        loaderController!!.onSizeChanged()
    }

    fun resetLoader() {
        if (!text.isNullOrEmpty()) {
            super.setText("")
            loaderController!!.startLoading()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        loaderController!!.onDraw(
            canvas, compoundPaddingLeft.toFloat(),
            compoundPaddingTop.toFloat(),
            compoundPaddingRight.toFloat(),
            compoundPaddingBottom.toFloat()
        )
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        if (loaderController != null) {
            loaderController!!.stopLoading()
        }
    }

    override fun setRectColor(rectPaint: Paint?) {
        val typeface = typeface
        if (typeface != null && typeface.style == Typeface.BOLD) {
            rectPaint?.color = darkerColorResource
        } else {
            rectPaint?.color = defaultColorResource
        }
    }

    override fun valueSet(): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loaderController!!.removeAnimatorUpdateListener()
    }
}