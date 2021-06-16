package com.yadev.mycustomlibrary

import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.activity.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(false) {

    override fun onSetup() {
        layout.apply {
            val controller = supportFragmentManager.findFragmentById(R.id.baseFragment) as NavHost
            bottomNav.setupWithNavController(controller.navController)
        }
    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}