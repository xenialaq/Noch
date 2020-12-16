package xyz.mnch.noch.ui.luci

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import xyz.mnch.noch.R
import xyz.mnch.noch.data.DeviceData


class LuciFragment : Fragment() {

    private lateinit var luciViewModel: LuciViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        luciViewModel =
                ViewModelProvider(this).get(LuciViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_webview, container, false)
//        val textView: TextView = root.findViewById(R.id.text_luci)
//        luciViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val routerHostname: String = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("router_address", resources.getString(R.string.router_address_default))!!
        val routerPassword: String = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("router_password", resources.getString(R.string.router_password_default))!!
        val luciWebView = root.findViewById<WebView>(R.id.simple_wv)
        luciWebView.settings.javaScriptEnabled = true
        luciWebView.loadUrl("http://$routerHostname")
        luciWebView.webViewClient = (object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.evaluateJavascript(
                    """setTimeout(function() {
                        const title = document.querySelector('h2[name="content"]');
                        if (!title || title.textContent !== "Authorization Required") {
                            return;
                        }
                        document.querySelector('input[name="luci_username"]').value = "root";
                        document.querySelector('input[name="luci_password"]').value = "$routerPassword";
                    }, 1e3)"""
                ) { paRes: String? ->
                    if (paRes != null && !paRes.isEmpty() && paRes != "null") {
                        Log.d("noch", paRes.toString())
                    }
                }
                super.onPageFinished(view, url)
            }
        })
        return root
    }
}
