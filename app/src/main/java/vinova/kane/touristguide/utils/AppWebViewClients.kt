package vinova.kane.touristguide.utils

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import vinova.kane.touristguide.ext.View.hide
import vinova.kane.touristguide.ext.View.show


class AppWebViewClients(private val progressBar: ProgressBar) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        progressBar.show()
        progressBar.bringToFront()
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView, url: String) {
        progressBar.hide()
        super.onPageFinished(view, url)
    }

}