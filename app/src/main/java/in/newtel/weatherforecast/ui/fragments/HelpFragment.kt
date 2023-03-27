package `in`.newtel.weatherforecast.ui.fragments

import `in`.newtel.weatherforecast.databinding.FragmentHelpBinding
import `in`.newtel.weatherforecast.util.Constants
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment


class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private lateinit var pageUrl: String

    companion object {
        const val MAX_PROGRESS = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)

// get pageUrl from String
        pageUrl = newIntent(requireContext(),Constants.PAGE_URL).getStringExtra(Constants.PAGE_URL)
            ?: throw IllegalStateException("field $Constants.PAGE_URL missing in Intent")

        initWebView()
        setWebClient()
        loadUrl(Constants.PAGE_URL)

        return binding.root
    }

    private fun newIntent(context: Context, pageUrl: String): Intent {
        val intent = Intent(context, HelpFragment::class.java)
        intent.putExtra(Constants.PAGE_URL, pageUrl)
        return intent
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }

    private fun setWebClient() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                binding.progressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && binding.progressBar.visibility == ProgressBar.GONE) {
                    binding.progressBar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == MAX_PROGRESS) {
                    binding.progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    private fun loadUrl(pageUrl: String) {
        binding.webView.loadUrl(pageUrl)
    }

}