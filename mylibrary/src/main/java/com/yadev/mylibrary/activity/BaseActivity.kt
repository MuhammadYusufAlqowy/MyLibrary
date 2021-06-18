package com.yadev.mylibrary.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yadev.mylibrary.R
import com.yadev.mylibrary.hideSoftKeyboard
import com.yadev.mylibrary.makeStatusBarTransparent

abstract class BaseActivity<V: ViewBinding>(val fullScreen: Boolean) : AppCompatActivity() {
    lateinit var layout: V
    private var dispatch = false
    private var fontScale = false
    val VISIBLE = View.VISIBLE
    val GONE = View.GONE
    val VERTICAL = RecyclerView.VERTICAL
    val HORIZONTAL = RecyclerView.HORIZONTAL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fullScreen) {
            setTheme(R.style.FullScreen)
            makeStatusBarTransparent()
        }
        layout = getBindingView()
        setContentView(layout.root)
    }

    abstract fun getBindingView(): V

    fun enableDispatch(status: Boolean) {
        dispatch = status
    }

    fun disbleFontScale(status: Boolean){
        fontScale = status
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (dispatch) {
            currentFocus?.let {
                hideSoftKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        if (fontScale){
            val newOverride = Configuration(newBase?.resources?.configuration)
            newOverride.fontScale = 1.0f
            applyOverrideConfiguration(newOverride)
        }
    }
}