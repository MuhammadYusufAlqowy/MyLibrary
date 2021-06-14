package com.yadev.mycustomlibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yadev.mycustomlibrary.databinding.FragmentTesBinding
import com.yadev.mylibrary.*
import com.yadev.mylibrary.fragment.BaseFragment

class TesFragment : BaseFragment<FragmentTesBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTesBinding {
        return FragmentTesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout.apply {
            layout.apply {
                btn1.setOnClickListener {
                    root.snackSucces("Login berhasil!").setAction("Oke") {
                        MySnackbar.make(root, message = "Berhasil di tutup!").showSuccess()
                    }
                }
                btn2.setOnClickListener {
                    root.snackError("Login berhasil!").setAction("Oke") {

                    }
                }
                btn3.setOnClickListener {
                    root.snackWarning("Selemat!").setAction("Lanjutkanssssssssssssss") {

                    }
                }
                btn4.setOnClickListener {
                    root.snackInfo("Selemat!").setAction("Lanjutkan") {

                    }
                }
                btn5.setOnClickListener {
                    val dialog = MyDialog.Build(
                        requireContext(),
                        "Ini Adalah Judul",
                        message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        image = R.drawable.ic_gallery
                    )

                    dialog.addOnNegativeButton("Tutup") {
                        root.snackInfo("Negative!")
                    }
                    dialog.addOnPositiveButton("Buka") {
                        root.snackInfo("Positive!")
                    }
                    dialog.show()
                }
                btn6.setOnClickListener {
                    val loading = MyLoading.create(requireContext()).show()
                }
                btnQ.setOnClickListener {
                    val loading = MyLoading.create(requireContext()).show()
                }
                slider.apply {
                    setAdapter(adaptertes())
                }

                fab.setOnClickListener {
                    pickImage(requireActivity(), image)
                }

                stepper.getValueChangeListener {
                    root.snackInfo(it.toString())
                }
                image.setImageUrl(
                    "https://paketnasi.co.id/uploads/user/slider/19.png",
                    type = MyBitmapTransformation.CENTER_INSIDE,
                    cacheStrategy = MyDiskCacheStrategy.NONE
                )
            }
        }
    }

}