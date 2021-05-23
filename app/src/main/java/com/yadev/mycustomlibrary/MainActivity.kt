package com.yadev.mycustomlibrary

import android.os.Bundle
import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.activity.BaseActivity
import com.yadev.mylibrary.pickImage


class MainActivity : BaseActivity<ActivityMainBinding>({ ActivityMainBinding.inflate(it)}, false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout.apply {
            btnPickImage.setOnClickListener {
                pickImage(this@MainActivity, imgPicture)
//            Toast.makeText(this, "${10.toDp()}", Toast.LENGTH_SHORT).show()
//            openWebview("http://paketnasi.co.id","Google")
            }
        }

    }
}