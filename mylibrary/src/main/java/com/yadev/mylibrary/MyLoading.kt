package com.yadev.mylibrary

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RawRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yadev.mylibrary.databinding.LayoutLoadingBinding

class MyLoading {
    class create(context: Context, @RawRes lottieAnim: Int = R.raw.loading) {
        private val loading = MaterialAlertDialogBuilder(context).create()
        val layout = LayoutLoadingBinding.inflate(LayoutInflater.from(context))

        init {
            loading.setView(layout.root)
            loading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val param = loading.window?.attributes
            param?.dimAmount = 0.5f
            loading.window?.attributes = param
            loading.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            layout.apply {
                this.lottieAnim.setAnimation(lottieAnim)
            }
        }

        fun show(): create {
            loading.show()
            return this
        }

        fun dismiss(): create {
            loading.dismiss()
            return this
        }
    }
}