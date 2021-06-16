package com.yadev.mylibrary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Animatable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.drawable.toBitmap
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.yadev.mylibrary.activity.WebViewActivity
import com.yadev.mylibrary.myimagepicker.ImagePicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.savePreferences(namePreferences: String, value: Any): Boolean {
    val preferences = this.getSharedPreferences("prefs", 0)
    val editor = preferences.edit()
    when (value) {
        is Boolean -> {
            editor.putBoolean(namePreferences, value)
            editor.apply()
            return true
        }
        is String -> {
            editor.putString(namePreferences, value)
            editor.apply()
            return true
        }
        is Int -> {
            editor.putInt(namePreferences, value)
            editor.apply()
            return true
        }
        is Long -> {
            editor.putLong(namePreferences, value)
            editor.apply()
            return true
        }
        is Float -> {
            editor.putFloat(namePreferences, value)
            editor.apply()
            return true
        }
        else -> {
            return false
        }
    }
}

fun Context.getPref(): SharedPreferences {
    return this.getSharedPreferences("prefs", 0)
}

fun Double.getFormatRupiah(): String {
    return NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(this).replace(",00", ",-")
        .replace("Rp", "Rp ")
}

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        if (it is TextInputEditText || it is EditText) {
            val inputMethodManager = ContextCompat.getSystemService(
                this,
                InputMethodManager::class.java
            )!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }
}

fun Activity.showSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
        inputMethodManager.showSoftInput(it.rootView, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun TextView.showStrikeThrough(show: Boolean) {
    paintFlags =
        if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}


/**
 * Conversi dp dan px
 */
fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Pengecekan input TextInputLayout
 */
fun checkInput(input: Any): Boolean {
    var check = false
    if (input is TextInputLayout) {
        if (input.editText?.text.toString().trim().isEmpty()) {
            check = false
            input.error = "${input.hint ?: ""} tidak boleh kosong!"
            input.requestFocus()
//            Snackbar.make(input.rootView,"${input.hint} tidak boleh kosong!",Snackbar.LENGTH_SHORT).show()
        } else {
            check = true
            input.isErrorEnabled = false
        }
    }
    return check
}

fun checkInputEmail(input: Any): Boolean {
    var check = false

    if (input is TextInputLayout) {
        check =
            !TextUtils.isEmpty(input.editText?.text.toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(
                input.editText?.text.toString()
            ).matches()
        if (input.editText?.text.toString().isEmpty()) {
            check = false
            input.error = "Email tidak boleh kosong!"
            input.requestFocus()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input.editText?.text.toString())
                .matches()
        ) {
            check = false
            input.error = "Email tidak valid!"
            input.requestFocus()
        } else {
            check = true
        }
        input.isErrorEnabled = !check
    }
    return check
}

fun checkInputImage(input: ImageView, error: TextView): Boolean {
    var check = false
    if (input.tag != null) {
        if ((input.tag as Boolean)) {
            check = true
            error.visibility = View.GONE
        } else {
            check = false
            error.visibility = View.VISIBLE
            error.requestFocus()
        }
    } else {
        check = false
        error.visibility = View.VISIBLE
        error.requestFocus()
    }

    return check
}

fun checkInputUsername(input: Any): Boolean {
    var check = false
    if (input is TextInputLayout) {

        if (input.editText?.text.toString().trim().isEmpty()) {
            check = false
            input.error = "Username tidak boleh kosong!"
            input.requestFocus()
        } else if (input.editText?.text.toString().length < 5) {
            check = false
            input.error = "Username minimal 5 karakter!"
            input.requestFocus()
        } else {
            check = true
        }
        input.isErrorEnabled = !check
    }
    return check
}

fun checkInputPassword(input: Any): Boolean {
    var check = false
    if (input is TextInputLayout) {
        if (input.editText?.text.toString().trim().isEmpty()) {
            check = false
            input.error = "Password tidak boleh kosong!"
            input.requestFocus()
        } else if (input.editText?.text.toString().length < 8) {
            check = false
            input.error = "Password minimal 8 karakter!"
            input.requestFocus()
        } else {
            check = true
        }
        input.isErrorEnabled = !check
    }
    return check
}


/**
 * Mengambil gambar dari gallery atau kamera
 */
fun pickImage(activity: Activity, imageView: ImageView, cropSquare: Boolean = true, compress:Int = 512, pickType:Int = DEFAULT, title: String = "Pilih Foto") {
    Dexter.withContext(activity)
        .withPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                if (p0?.areAllPermissionsGranted()!!) {
                    val imagePicker = ImagePicker.with(activity)
                        .saveDir(File(activity.externalCacheDir, "ImagePicker"))
                        .maxResultSize(1080, 1080)
                        .compress(compress)
                        .setTitle(title)

                    when(pickType){
                        1 -> imagePicker.galleryOnly()
                        2 -> imagePicker.cameraOnly()
                    }
                    if (cropSquare){
                        imagePicker.cropSquare()
                    }else{
                        imagePicker.crop()
                    }
                    imagePicker.
                    start { resultCode, data ->
                        if (resultCode == Activity.RESULT_OK) {
                            Glide.with(activity).load(data?.data)
                                .into(imageView)
                            imageView.tag = true
                        } else if (resultCode == ImagePicker.RESULT_ERROR) {
                            Toast.makeText(
                                activity,
                                ImagePicker.getError(data),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()

}

interface LocationListener {
    fun getLocation(location: Location)
}

/**
 * Mendapatkan lokasi terakhir
 */
class MyLocation(val context: Context, val listener: LocationListener) {

    fun getLastLocation() {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val request = LocationRequest()
                    request.interval = 10000
                    request.fastestInterval = 5000
                    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val client =
                        LocationServices.getFusedLocationProviderClient(context as Activity)


                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gpsOn(listener)
                    } else {
                        client.requestLocationUpdates(request, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                val location: Location = locationResult.lastLocation
                                listener.getLocation(location)
                                client.removeLocationUpdates(this)
                            }
                        }, null)

                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        val dialog = MaterialAlertDialogBuilder(context)
                        dialog.setTitle("Ijin Lokasi")
                        dialog.setMessage("Aplikasi membutuhkan akses lokasi saat ini.\nSilahkan aktifkan lokasi secara manual melalui pengaturan aplikasi.")
                        dialog.setPositiveButton("Buka Pengaturan") { d, v ->
                            openSettings()
                            d.dismiss()
                        }
                        dialog.setNegativeButton("Tutup") { d, v ->
                            d.dismiss()
                        }
                        dialog.show()
                        /*val dialog = MyDialog(
                            context,
                            title = "Ijin Lokasi",
                            message = "Aplikasi membutuhkan akses lokasi saat ini untuk melihat penawaran menu di lokasi anda.\nSilahkan aktifkan lokasi secara manual melalui pengaturan aplikasi.",
                            image = R.drawable.map_color,
                            positive = "Buka pengaturan",
                            "Tolak"
                        ).show()
                        dialog.getPositiveButton().setOnClickListener {
                            openSettings()
                            dialog.dismiss()
                        }*/

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    val dialog = MaterialAlertDialogBuilder(context)
                    dialog.setTitle("Ijin Lokasi")
                    dialog.setMessage("Aplikasi membutuhkan akses lokasi saat ini untuk melihat penawaran menu di lokasi anda.\\nSilahkan aktifkan lokasi secara manual melalui pengaturan aplikasi.")
                    dialog.setPositiveButton("Buka Pengaturan") { d, v ->
                        token.continuePermissionRequest()
                        d.dismiss()
                    }
                    dialog.setNegativeButton("Tutup") { d, v ->
                        token.cancelPermissionRequest()
                        d.dismiss()
                    }
                    dialog.show()
                }
            }).check()

    }

    fun gpsOn(listener: LocationListener) {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
        builder.setAlwaysShow(true)
        val mLocationSettingsRequest = builder.build()
        val mSettingsClient = LocationServices.getSettingsClient(context)
        mSettingsClient
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(OnSuccessListener { locationSettingsResponse: LocationSettingsResponse? ->
            })
            .addOnCanceledListener {
            }
            .addOnFailureListener(OnFailureListener { e: Exception ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(context as Activity, 214)
                        //                                    startIntentSenderForResult(rae.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                    } catch (sie: Exception) {
                        Log.e("GPS", "Unable to execute request.")
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                        "GPS",
                        "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                    )
                }
            })
            .addOnCanceledListener(OnCanceledListener {
                Log.e(
                    "GPS",
                    "checkLocationSettings -> onCanceled"
                )
            })
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts(
            "package",
            context.packageName, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}

/**
 * Mendapatkan tinggi statusbar
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * Menambahkan format rupiah di TextInputLayout
 * Mengambil nilai dari format rupiah TextInputLayout
 */
fun TextInputLayout.addFormatRupiah() {
    this.editText?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0.toString().isNotEmpty()) {
                try {
                    val nominal = p0.toString().replace(".", "")
                    val newPrice: String =
                        nominal.toDouble().getFormatRupiah().removePrefix("Rp ")
                            .removeSuffix(",-")
                    this@addFormatRupiah.editText?.removeTextChangedListener(this)
                    this@addFormatRupiah.editText?.setText(newPrice)
                    this@addFormatRupiah.editText?.setSelection(newPrice.length)
                    this@addFormatRupiah.editText?.addTextChangedListener(this)
                } catch (e: Exception) {
                }
            }
        }
    })
}

fun TextInputLayout.getValueFormatRupiah() = this.editText?.text.toString().replace(".", "")

fun Context.openWebview(
    url: String,
    title: String? = getString(R.string.app_name),
    @DrawableRes icon: Int? = R.drawable.ic_baseline_arrow_back_24,
    @ColorRes backGroundColor: Int? = R.color.white,
    @ColorRes titleColor: Int? = R.color.primary
) {
    val intent = Intent(this, WebViewActivity::class.java)
    intent.putExtra("title", title)
    intent.putExtra("url", url)
    intent.putExtra("icon", icon)
    intent.putExtra("backgroundColor", backGroundColor)
    intent.putExtra("titleColor", titleColor)
    startActivity(intent)
}

fun readableFileSize(size: Long): String? {
    if (size <= 0) return "0 Bytes"
    val units = arrayOf("Bytes", "kB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble()))
        .toString() + " " + units[digitGroups]
}

fun Context.getCacheSize(): Long {
    var size: Long = 0
    size += getDirSize(cacheDir!!)
    size += getDirSize(externalCacheDir!!)
    return size
}

fun getDirSize(dir: File): Long {
    var size: Long = 0
    for (file in dir.listFiles()) {
        if (file != null && file.isDirectory) {
            size += getDirSize(file)
        } else if (file != null && file.isFile) {
            size += file.length()
        }
    }
    return size
}

fun ImageViewToByteArray(imageView: ImageView): ByteArray {
    val bitmap = imageView.drawable.toBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

@SuppressLint("UseCompatLoadingForColorStateLists")
fun TextInputLayout.setLoadingTil(show: Boolean) {
    if (show) {
        this.context.applicationContext.apply {
            val value = TypedValue()
            theme.resolveAttribute(android.R.attr.progressBarStyleSmall, value, false)
            val progressBarStyle = value.data
            val attributes = intArrayOf(android.R.attr.indeterminateDrawable)
            val array = obtainStyledAttributes(progressBarStyle, attributes)
            val drawable = array.getDrawableOrThrow(0)
            array.recycle()

            isStartIconVisible = true
            startIconDrawable = drawable
            setStartIconTintList(ContextCompat.getColorStateList(this, R.color.primary))
            drawable.setTintList(ContextCompat.getColorStateList(this, R.color.primary))

            (startIconDrawable as? Animatable)?.start()
        }
    } else {
        this.context.applicationContext.apply {
            (startIconDrawable as? Animatable)?.stop()
            isStartIconVisible = false
        }
    }
}

fun getSimpleDate(format: String): SimpleDateFormat {
    return SimpleDateFormat(format, Locale("id", "ID"))
}

fun ImageView.setTextDrawable(
    nama: String,
    @ColorRes colorText: Int,
    @ColorRes colorBackground: Int? = null
) {
    val generator = ColorGenerator.MATERIAL
    val text = TextDrawable.builder()
        .beginConfig()
        .textColor(this.context.getColorRes(colorText))
        .height(this.measuredHeight)
        .width(this.measuredWidth)
        .bold()
        .toUpperCase()
        .endConfig()
        .buildRound(
            nama[0].toString(),
            if (colorBackground != null) this.context.getColorRes(colorBackground) else generator?.getColor(
                nama
            )!!
        )
    this.setImageDrawable(text)
}

fun Context.getColorRes(@ColorRes res: Int) = ContextCompat.getColor(this, res)

fun Context.getDrawableRes(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)

fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun View.snackSucces(msg: String, anchorView: View? = null): MySnackbar.make {
    val snack = MySnackbar.make(this, title = "Success", message = msg, anchorView = anchorView)
    snack.showSuccess()
    return snack
}

fun View.snackError(msg: String, anchorView: View? = null): MySnackbar.make {
    val snack = MySnackbar.make(this, title = "Error", message = msg, anchorView = anchorView)
    snack.showError()
    return snack
}

fun View.snackWarning(msg: String, anchorView: View? = null): MySnackbar.make {
    val snack = MySnackbar.make(this, title = "Warning", message = msg, anchorView = anchorView)
    snack.showWarning()
    return snack
}

fun View.snackInfo(msg: String, anchorView: View? = null): MySnackbar.make {
    val snack = MySnackbar.make(this, title = "Info", message = msg, anchorView = anchorView)
    snack.showInfo()
    return snack
}

fun View.setPaddingHorizontal(padding: Int) {
    setPadding(padding, paddingTop, padding, paddingBottom)
}

fun View.setPaddingVertical(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, padding)
}

fun ImageView.setImageUrl(
    url: String,
    key: String? = "",
    type: MyBitmapTransformation = MyBitmapTransformation.FIT_CENTER,
    roundedCorner: Int = 1,
    cacheStrategy: MyDiskCacheStrategy = MyDiskCacheStrategy.ALL,
//    @DrawableRes errorDrawable: Int = R.drawable.ic_error_cloud
) {
    var bitmapTransformation: BitmapTransformation = when (type) {
        is MyBitmapTransformation.FIT_CENTER -> FitCenter()
        is MyBitmapTransformation.CENTER_CROP -> CenterCrop()
        is MyBitmapTransformation.CENTER_INSIDE -> CenterInside()
        is MyBitmapTransformation.CIRCLE_CROP -> CircleCrop()
    }
    var diskCacheStrategy = when (cacheStrategy) {
        is MyDiskCacheStrategy.ALL -> DiskCacheStrategy.ALL
        is MyDiskCacheStrategy.NONE -> DiskCacheStrategy.NONE
        is MyDiskCacheStrategy.AUTO -> DiskCacheStrategy.AUTOMATIC
    }
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 4f
    circularProgressDrawable.centerRadius = 25f
    circularProgressDrawable.start()
    Glide.with(this.context.applicationContext).load(url)
        .apply(
            RequestOptions().transform(bitmapTransformation, RoundedCorners(roundedCorner))
                .diskCacheStrategy(diskCacheStrategy)
                .signature(ObjectKey(key!!))
        ).placeholder(circularProgressDrawable)
//        .error(errorDrawable)
        .into(this)
}

fun ImageView.setImageBase64(
    base64: String,
    key: String = "",
    type: BitmapTransformation,
    roundedCorner: Int = 0,
    cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.ALL,
//    @DrawableRes errorDrawable: Int
) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 4f
    circularProgressDrawable.centerRadius = 25f
    circularProgressDrawable.start()
    Glide.with(this.context.applicationContext).load(Base64.decode(base64, Base64.DEFAULT))
        .apply(
            RequestOptions().transform(type, RoundedCorners(roundedCorner))
                .diskCacheStrategy(cacheStrategy)
                .signature(ObjectKey(key))
        )
        .placeholder(circularProgressDrawable)
//        .error(errorDrawable)
        .into(this)
}

fun View.getMeasuredViewHeight(): Int {
    val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY)
    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    this.measure(wMeasureSpec, hMeasureSpec)
    return this.measuredHeight
}
