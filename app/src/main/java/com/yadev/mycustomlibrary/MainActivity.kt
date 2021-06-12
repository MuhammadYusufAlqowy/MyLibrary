package com.yadev.mycustomlibrary

import android.os.Bundle
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.activity.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout.apply {
            val controller = supportFragmentManager.findFragmentById(R.id.baseFragment) as NavHost
            bottomNav.setupWithNavController(controller.navController)
        }
    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}