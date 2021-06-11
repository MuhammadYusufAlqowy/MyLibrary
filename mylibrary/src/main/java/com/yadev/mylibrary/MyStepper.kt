package com.yadev.mylibrary

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.*
import com.yadev.mylibrary.databinding.LayoutStepperBinding

class MyStepper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleOwner {
    private val layout = LayoutStepperBinding.inflate(LayoutInflater.from(context), this, false)
    var max = 0
    var min = 0
    var increment = 1
    var value = 0
    val valueLiveData = MutableLiveData<Int>()
    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        addView(layout.root)
        layout.apply {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.MyStepper)
            max = attr.getInteger(R.styleable.MyStepper_stepMaxValue, 0)
            min = attr.getInteger(R.styleable.MyStepper_stepMinValue, 0)
            val drawablePlus =
                attr.getResourceId(R.styleable.MyStepper_stepDrawablePlus, R.drawable.ic_plus)
            val drawableMin =
                attr.getResourceId(R.styleable.MyStepper_stepDrawableMinus, R.drawable.ic_min)
            val drawableSize = attr.getDimensionPixelSize(
                R.styleable.MyStepper_stepDrawableSize,
                context.resources.getDimensionPixelSize(R.dimen._24adp)
            )
            val drawableColor = attr.getColorStateList(R.styleable.MyStepper_stepDrawableColor)
                ?: ContextCompat.getColorStateList(context, R.color.color_button)
            value = attr.getInteger(R.styleable.MyStepper_stepValue, min)
            increment = attr.getInteger(R.styleable.MyStepper_stepValueIncrement, 1)
            val valueColor = attr.getColor(
                R.styleable.MyStepper_stepValueColor,
                context.getColorRes(R.color.text_color)
            )
            val valueSize = attr.getDimension(
                R.styleable.MyStepper_stepValueSize,
                resources.getDimension(R.dimen.t11)
            )
            btnMin.setImageResource(drawableMin)
            btnPlus.setImageResource(drawablePlus)
            btnMin.updateLayoutParams<ViewGroup.LayoutParams> {
                height = drawableSize
                width = drawableSize
            }

            btnPlus.updateLayoutParams<ViewGroup.LayoutParams> {
                height = drawableSize
                width = drawableSize
            }
            btnPlus.imageTintList = drawableColor
            btnMin.imageTintList = drawableColor
            tvValue.text = value.toString()
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueSize)
            tvValue.setTextColor(valueColor)
            btnMin.isEnabled = value > min
            btnPlus.isEnabled = !(max != 0 && value >= max)
            if (value <= min) value = min
            if (max != 0 && value >= max) value = max

            valueLiveData.observe(this@MyStepper) {
                btnMin.isEnabled = it > min
                btnPlus.isEnabled = !(max != 0 && it >= max)
                if (it <= min) value = min
                if (max != 0 && it >= max) value = max
                tvValue.text = value.toString()
            }

            layout.apply {
                btnPlus.setOnClickListener {
                    value += increment
                    if (max != 0 && value >= max) value = max
                    valueLiveData.postValue(value)
                }
            }

            layout.apply {
                btnMin.setOnClickListener {
                    value -= increment
                    if (value<=min) value = min
                    valueLiveData.postValue(value)
                }
            }

            attr.recycle()
        }
    }

    fun setButtonPlusOnClickListener(listener: OnClickListener) {
        layout.apply {
            btnPlus.setOnClickListener {
                value += increment
                valueLiveData.postValue(value)
                listener.onClick(it)
            }
        }
    }

    fun setButtonMinusOnClickListener(listener: OnClickListener) {
        layout.apply {
            btnMin.setOnClickListener {
                value -= increment
                valueLiveData.postValue(value)
                listener.onClick(it)
            }
        }
    }

    override fun getLifecycle() = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }


}