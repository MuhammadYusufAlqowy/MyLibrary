package com.yadev.mylibrary

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yadev.mylibrary.databinding.LayoutCustomDialogBinding

class MyDialog {

    class BuildCustomLayout(val context: Context, val viewBinding: ViewBinding) {
        private var dialog = MaterialAlertDialogBuilder(context).create()
        private var layout = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))

        init {
            dialog.setView(layout.root)
            layout.baseLayout.visibility = GONE
            layout.customLayout.visibility = VISIBLE
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val param = dialog.window?.attributes
            param?.dimAmount = 0.5f
            dialog.window?.attributes = param
            dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            layout.customLayout.addView(
                viewBinding.root,
                ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            )
        }

        fun setCancelable(boolean: Boolean): BuildCustomLayout {
            dialog.setCancelable(boolean)
            return this
        }

        fun show(): BuildCustomLayout {
            dialog.show()
            return this
        }

        fun dismiss(): BuildCustomLayout {
            dialog.dismiss()
            return this
        }
    }

    class Build(
        val context: Context,
        title: String? = null,
        message: String? = null,
        @DrawableRes image: Int? = null
    ) {
        private var dialog = MaterialAlertDialogBuilder(context).create()
        var layout = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))

        init {
            dialog.setView(layout.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val param = dialog.window?.attributes
            param?.dimAmount = 0.5f
            dialog.window?.attributes = param
            dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            layout.apply {
                if (title.isNullOrEmpty()) {
                    tvTitle.visibility = GONE
                }
                if (message.isNullOrEmpty()) {
                    tvMessage.visibility = GONE
                }
                if (image == null) {
                    imgIcon.visibility = GONE
                }
                tvTitle.text = title
                tvMessage.text = message
                imgIcon.setImageResource(image ?: 0)
                btnNegative.visibility = GONE
                btnPositive.visibility = GONE
            }
        }

        fun setCancelable(boolean: Boolean): Build {
            dialog.setCancelable(boolean)
            return this
        }

        fun addOnNegativeButton(button: String, listener: View.OnClickListener): Build {
            layout.apply {
                btnNegative.visibility = VISIBLE
                btnNegative.text = button
                btnNegative.setOnClickListener {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    listener.onClick(it)
                }
            }
            return this
        }

        fun addOnPositiveButton(button: String, listener: View.OnClickListener): Build {
            layout.apply {
                btnPositive.visibility = VISIBLE
                btnPositive.text = button
                btnPositive.setOnClickListener {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    listener.onClick(it)
                }
            }
            return this
        }

        fun show(): Build {
            dialog.show()
            return this
        }

        fun dismiss(): Build {
            dialog.dismiss()
            return this
        }


    }
}