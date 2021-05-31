package com.yadev.mycustomlibrary

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.yadev.mycustomlibrary.databinding.ActivityMainBinding
import com.yadev.mylibrary.*
import com.yadev.mylibrary.activity.BaseActivity
import kotlin.coroutines.CoroutineContext


class MainActivity : BaseActivity<ActivityMainBinding>(false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout.apply {
            btn1.setOnClickListener {
                root.snackSucces("Login berhasil!").setAction("Oke"){
                    MySnackbar.make(root,message = "Berhasil di tutup!").showSuccess()
                }
            }
            btn2.setOnClickListener {
                root.snackError("Login berhasil!").setAction("Oke"){

                }
            }
            btn3.setOnClickListener {
                root.snackWarning("Selemat!").setAction("Lanjutkan"){

                }
            }
            btn4.setOnClickListener {
                root.snackInfo("Selemat!").setAction("Lanjutkan"){

                }
            }
            btn5.setOnClickListener {
                val dialog = MyDialog.Build(this@MainActivity,"Ini Adalah Judul", message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", image = R.drawable.ic_gallery)

                dialog.addOnNegativeButton("Tutup"){
                    root.snackInfo("Negative!")
                }
                dialog.addOnPositiveButton("Buka"){
                    root.snackInfo("Positive!")
                }
                dialog.show()
            }
            btn6.setOnClickListener{
                val loading = MyLoading.create(this@MainActivity).show()
            }
            btnQ.setOnClickListener{
                val loading = MyLoading.create(this@MainActivity).show()
            }
        }

    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}