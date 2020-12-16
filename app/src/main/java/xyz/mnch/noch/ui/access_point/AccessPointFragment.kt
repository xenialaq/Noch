package xyz.mnch.noch.ui.access_point

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.preference.PreferenceManager
import xyz.mnch.noch.R


class AccessPointFragment : Fragment() {

    private lateinit var accessPointViewModel: AccessPointViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accessPointViewModel =
                ViewModelProvider(this).get(AccessPointViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_webview, container, false)
//        val textView: TextView = root.findViewById(R.id.text_accessPoint)
//        accessPointViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val accessPointPassword: String = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("access_point_password", "")!!
        val accessPointWebView = root.findViewById<WebView>(R.id.simple_wv)
        accessPointWebView.settings.javaScriptEnabled = true
        accessPointWebView.settings.userAgentString = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:83.0) Gecko/20100101 Firefox/83.0";
        val hostname = arguments?.getString("hostname")
        if (hostname == null) {
            findNavController(this).navigate(R.id.nav_access_point)
            return root
        }
        accessPointWebView.loadUrl("http://$hostname")
//        accessPointWebView.webViewClient = WebViewClient()
        accessPointWebView.webViewClient = (object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.evaluateJavascript(
                    """setTimeout(function() {
                        const forgotPassword = document.querySelector('span.loginHelp')
                        if (!forgotPassword || forgotPassword.textContent.indexOf("忘记密码") < 0) {
                            return
                        }
                        const pwdBox = document.querySelector('li.lgInput,li.lgBom')
                        pwdBox.hidden = true
                        const pwd = document.querySelector('input[id="lgPwd"][type="password"]')
                        pwd.value = "$accessPointPassword"
                    }, 2e3)"""
                ) { paRes: String? ->
                    if (paRes != null && !paRes.isEmpty() && paRes != "null") {
                        Log.d("noch", paRes.toString())
                    }
                }
                Log.d("noch", "Likely DoNe")
                super.onPageFinished(view, url)
            }
        })
        return root
    }
}
