package xyz.mnch.noch.ui.ariang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.mnch.noch.MainActivity
import xyz.mnch.noch.R
import xyz.mnch.noch.adapters.AccessPointListAdapter
import xyz.mnch.noch.adapters.AriangListAdapter
import xyz.mnch.noch.ui.access_point.AriangListViewModel

class AriangListFragment : Fragment() {

    private lateinit var ariangListViewModel: AriangListViewModel
    var deviceAdapter: AriangListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ariangListViewModel =
            ViewModelProvider(this).get(AriangListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        deviceViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val rv = root.findViewById<RecyclerView>(R.id.rv)
        rv!!.layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        deviceAdapter = AriangListAdapter(activity as MainActivity)
        rv.adapter = deviceAdapter

        deviceAdapter?.updateDeviceList()
        return root
    }
}
