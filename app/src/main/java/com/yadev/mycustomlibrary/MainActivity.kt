package com.yadev.mycustomlibrary

import android.os.Bundle
import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.MyDialog
import com.yadev.mylibrary.MyLoading
import com.yadev.mylibrary.activity.BaseActivity
import com.yadev.mylibrary.pickImage
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
                pickImage(this@MainActivity, img)
            }
            badge.setBadgeNumber(10)
            badge.setOnClickListener {
                MyDialog.Build(this@MainActivity, "Tes", "Tes")
                    .addOnNegativeButton("Oke") {
                        MyLoading.Build(this@MainActivity, message = "Tunggu...").show()
                    }.show()
            }

        }
    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}