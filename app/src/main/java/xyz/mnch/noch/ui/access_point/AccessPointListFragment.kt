package xyz.mnch.noch.ui.access_point

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

class AccessPointListFragment : Fragment() {

    private lateinit var accessPointListViewModel: AccessPointListViewModel
    var deviceAdapter: AccessPointListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accessPointListViewModel =
            ViewModelProvider(this).get(AccessPointListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        deviceViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val rv = root.findViewById<RecyclerView>(R.id.rv)
        rv!!.layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        deviceAdapter = AccessPointListAdapter(activity as MainActivity)
        rv.adapter = deviceAdapter

        deviceAdapter?.updateDeviceList()
        return root
    }
}