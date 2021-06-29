package com.yadev.mycustomlibrary

import android.os.Bundle
import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.activity.BaseActivity
import com.yadev.mylibrary.openWebview
import com.yadev.mylibrary.snackError


class MainActivity : BaseActivity<ActivityMainBinding>(false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout.apply {
            stepper.setValue(10)
            stepper.getValueChangeListener {
                root.snackError("Hello")
            }
            slider.setAdapter(adaptertes())
            btnAction.setOnClickListener {
                openWebview("https://google.com")
            }
            badge.setBadgeNumber(10)

        }
    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}