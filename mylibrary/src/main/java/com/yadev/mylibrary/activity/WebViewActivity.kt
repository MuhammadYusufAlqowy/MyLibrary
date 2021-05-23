package com.yadev.mylibrary.activity

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yadev.mylibrary.R
import com.yadev.mylibrary.databinding.ActivityWebViewBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class WebViewActivity : AppCompatActivity() {
    private lateinit var layout: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(layout.root)
        val title = intent.getStringExtra("title") ?: getString(R.string.app_name)
        val url = intent.getStringExtra("url") ?: getString(R.string.app_name)
        val icon = intent.getIntExtra("icon", R.drawable.ic_baseline_arrow_back_24)
        val backgroundColor = intent.getIntExtra("backgroundColor", R.color.white)
        val titleColor = intent.getIntExtra("titleColor", R.color.primary)

        layout.apply {
            toolbar?.title = title
            toolbar.navigationIcon = ContextCompat.getDrawable(this@WebViewActivity,icon)
            toolbar.setBackgroundResource(backgroundColor)
            toolbar.setTitleTextColor(ContextCompat.getColor(this@WebViewActivity,titleColor))
            toolbar?.setNavigationOnClickListener { onBackPressed() }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                webView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    if (scrollY > oldScrollY){
                        toolbar.postOnAnimationDelayed({
                            toolbar.visibility = View.GONE
                        },500)
                    }else if (scrollY < oldScrollY){
                        toolbar.postOnAnimationDelayed({
                            toolbar.visibility = View.VISIBLE
                        },500)
                    }
                }
            }*/
            webView.apply {
                settings.loadsImagesAutomatically = true
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT

                // Tiga baris di bawah ini agar laman yang dimuat dapat
                // melakukan zoom.
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
                scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url.toString()
                        view?.loadUrl(url)
                        return super.shouldOverrideUrlLoading(view, request)

                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        swipe?.isRefreshing = true
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        swipe?.isRefreshing = false
                        super.onPageFinished(view, url)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        swipe?.isRefreshing = false
                        super.onReceivedError(view, request, error)
                    }
                }

                loadUrl(url)
            }
            swipe?.setOnRefreshListener {
                webView.loadUrl(url)
            }
            CoroutineScope(Main).launch {
                delay(5000L)
                if (swipe.isRefreshing) {
                    swipe.isRefreshing = false
                }
            }
        }

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}