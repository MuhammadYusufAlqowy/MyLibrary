package com.yadev.mylibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yadev.mylibrary.R
import com.yadev.mylibrary.makeStatusBarTransparent

abstract class BaseActivity<V: ViewBinding>(val fullScreen: Boolean = false) : AppCompatActivity(){
    lateinit var layout : V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fullScreen){
            setTheme(R.style.FullScreen)
            makeStatusBarTransparent()
        }
//        layout = bindingFactory(layoutInflater)
        layout = getBindingView()
        setContentView(layout.root)

    }
    abstract fun getBindingView() : V
}