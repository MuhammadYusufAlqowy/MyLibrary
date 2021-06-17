package com.yadev.mycustomlibrary

import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.activity.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(false) {

    override fun onSetup() {

    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}