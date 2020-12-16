package xyz.mnch.noch.ui.ariang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import xyz.mnch.noch.R


class AriangFragment : Fragment() {

    private lateinit var ariangViewModel: AriangViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ariangViewModel =
                ViewModelProvider(this).get(AriangViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_webview, container, false)
//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val ariangWebView = root.findViewById<WebView>(R.id.simple_wv)
        ariangWebView.settings.javaScriptEnabled = true
        val hostname = arguments?.getString("hostname")
        if (hostname == null) {
            NavHostFragment.findNavController(this).navigate(R.id.nav_ariang_list)
            return root
        }
        ariangWebView.loadUrl("http://$hostname/ariang/#!/downloading")
        return root
    }
}