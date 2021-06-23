package com.yadev.mylibrary

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RawRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yadev.mylibrary.databinding.LayoutLoadingBinding

class MyLoading {
    class Build(
        context: Context,
        @RawRes lottieAnim: Int = R.raw.loading,
        message: String? = null
    ) {
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
                if (!message.isNullOrEmpty()) {
                    tvMessage.visibility = View.VISIBLE
                    tvMessage.text = message
                }
            }
        }

        fun show(): Build {
            loading.show()
            return this
        }

        fun setCancelable(boolean: Boolean): Build {
            loading.setCancelable(boolean)
            return this
        }

        fun dismiss(delay: Long = 0L): Build {
            Handler(Looper.getMainLooper()).postDelayed({
                loading.dismiss()
            }, delay)
            return this
        }
    }
}