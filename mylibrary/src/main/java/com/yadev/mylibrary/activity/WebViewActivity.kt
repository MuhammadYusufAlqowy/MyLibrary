package com.yadev.mylibrary.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.MailTo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.yadev.mylibrary.R
import com.yadev.mylibrary.databinding.ActivityWebViewBinding
import com.yadev.mylibrary.getColorRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebViewActivity : AppCompatActivity() {
    private lateinit var layout: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(layout.root)
        val title = intent.getStringExtra("title") ?: getString(R.string.app_name)
        val url = intent.getStringExtra("url") ?: getString(R.string.app_name)
        val icon = intent.getIntExtra("icon", R.drawable.ic_arrow_back)
        val backgroundColor = intent.getIntExtra("backgroundColor", R.color.white)
        val titleColor = intent.getIntExtra("titleColor", R.color.text_color)
//        window?.statusBarColor = getColorRes(R.color.primary)

        layout.apply {
            btnBack.setOnClickListener { onBackPressed() }
            btnBack.setImageResource(icon)
            tvTitle.text = title
            tvTitle.setTextColor(getColorRes(titleColor))
            toolbar.setBackgroundResource(backgroundColor)
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
                        /*val url = request?.url.toString()
                        Log.wtf("URLLL",Uri.parse(url).scheme)
                        if (Uri.parse(url).scheme!!.contains("market",true)||Uri.parse(url).scheme!!.contains("whatsapp",true)||Uri.parse(url).scheme!!.contains("facebook",true)||Uri.parse(url).scheme!!.contains("intent",true)){
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }catch (e:Throwable){}
                            return false
                        }else{
                            view?.loadUrl(url)
                            return true
                        }*/
                        return if (url.endsWith(".mp4")) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(Uri.parse(url), "video/*")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent) // If we return true, onPageStarted, onPageFinished won't be called.
                            true
                        } else if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith(
                                "smsto:"
                            ) || url.startsWith("mms:") || url.startsWith("mmsto:")
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            true // If we return true, onPageStarted, onPageFinished won't be called.
                        } else if (url.startsWith("mailto:")) {
                            val mt = MailTo.parse(url)
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.type = "text/html"
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mt.to))
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mt.subject)
                            emailIntent.putExtra(Intent.EXTRA_CC, mt.cc)
                            emailIntent.putExtra(Intent.EXTRA_TEXT, mt.body)
                            startActivity(emailIntent)
                            true
                        } else if (Uri.parse(url).scheme!!.contains("whatsapp", true) || Uri.parse(
                                url
                            ).scheme!!.contains("market", true)
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                            true
                        } else {
                            super.shouldOverrideUrlLoading(view, url)
                        }
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        swipe.isRefreshing = true
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        swipe.isRefreshing = false
                        super.onPageFinished(view, url)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        swipe.isRefreshing = false
                        super.onReceivedError(view, request, error)
                    }
                }

                loadUrl(url)
            }
            swipe.setOnRefreshListener {
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