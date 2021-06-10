package com.yadev.mylibrary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.yadev.mylibrary.databinding.LayoutCustomSnackbarBinding

class MySnackbar {

    @SuppressLint("ShowToast")
    class make(
        view: View,
        title: String? = null,
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        anchorView: View? = null
    ) {
        private var snackbar: Snackbar = Snackbar.make(view, message, duration)
        var myLayout: LayoutCustomSnackbarBinding =
            LayoutCustomSnackbarBinding.inflate(LayoutInflater.from(view.context))

        init {
            val rootView = (snackbar.view as ViewGroup)
            if (anchorView != null) {
                snackbar.anchorView = anchorView
            }
            rootView.setPadding(0, 0, 0, 0)
            rootView.removeAllViews()
            rootView.setBackgroundResource(R.color.transparent)
            rootView.addView(myLayout.root)
            myLayout.apply {
                if (title.isNullOrEmpty()) {
                    tvTitle.visibility = View.GONE
                }
                tvTitle.text = title
                tvMessage.text = message
            }
        }

        fun showInfo(): make {
            myLayout.apply {
                imgIcon.setImageResource(R.drawable.ic_info)
                rootLayout.setCardBackgroundColor(root.context.getColorRes(R.color.info))
            }

            snackbar.show()
            return this
        }

        fun showSuccess(): make {
            myLayout.apply {
                imgIcon.setImageResource(R.drawable.ic_ok)
                rootLayout.setCardBackgroundColor(root.context.getColorRes(R.color.success))
            }
            snackbar.show()
            return this
        }

        fun showError(): make {
            myLayout.apply {
                imgIcon.setImageResource(R.drawable.ic_cancel)
                rootLayout.setCardBackgroundColor(root.context.getColorRes(R.color.error))
            }
            snackbar.show()
            return this
        }

        fun showWarning(): make {
            myLayout.apply {
                imgIcon.setImageResource(R.drawable.ic_warning)
                rootLayout.setCardBackgroundColor(root.context.getColorRes(R.color.warning))
            }
            snackbar.show()
            return this
        }

        fun show(): make {
            snackbar.show()
            return this
        }

        fun dismiss(): make {
            if (snackbar.isShown) {
                snackbar.dismiss()
            }
            return this
        }

        fun setCancelable(duration: Int): make {
            snackbar.duration = duration
            return this
        }

        fun setAction(action: String, listener: View.OnClickListener): make {
            myLayout.apply {
                btnAction.visibility = View.VISIBLE
                btnAction.text = action
                btnAction.isSelected = true
                btnAction.setOnClickListener {
                    listener.onClick(it)
                    snackbar.dismiss()
                }
            }
            return this
        }

    }
}