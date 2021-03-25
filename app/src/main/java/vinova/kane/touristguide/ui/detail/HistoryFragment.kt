package vinova.kane.touristguide.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import vinova.kane.touristguide.databinding.FragmentHistoryBinding
import vinova.kane.touristguide.utils.AppWebViewClients

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater)

        initWebView()
        webView.loadUrl("")

        return binding.root
    }

    private fun initWebView(){
        webView = binding.webViewHistory
        progressBar = binding.progressBar

        webView.webViewClient = AppWebViewClients(progressBar)
    }

}