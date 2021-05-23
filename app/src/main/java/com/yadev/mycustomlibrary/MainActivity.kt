package com.yadev.mycustomlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yadev.mycustomlibrary.R.*
import com.yadev.mylibrary.pickImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        pickImage(this,)
    }
}