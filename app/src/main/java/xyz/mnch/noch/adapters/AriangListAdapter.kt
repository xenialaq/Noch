package xyz.mnch.noch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import xyz.mnch.noch.MainActivity
import xyz.mnch.noch.R
import xyz.mnch.noch.data.DeviceData
import java.util.*
import java.util.concurrent.TimeUnit


class AriangListAdapter(activity: MainActivity) :
    RecyclerView.Adapter<AriangListAdapter.ViewHolder>() {
    private val deviceDataList: MutableList<DeviceData>
    private val activity: MainActivity
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MILLISECONDS)
        .build();

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.device_view, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deviceData: DeviceData = deviceDataList[position]
        holder.tvDeviceName.text = deviceData.name
        holder.tvDeviceHostname.text = deviceData.hostname
        holder.deviceData = deviceData
    }

    override fun getItemCount(): Int {
        return deviceDataList.size
    }

    fun updateDeviceList() {
        deviceDataList.clear()
        val dict = PreferenceManager.getDefaultSharedPreferences(
            activity.baseContext
        ).getString("device_aliases", "")!!.split(Regex("\\n")).filter { it.indexOf(',') > 0 }.map {
            val arr = it.split(',')
            arr[0] to arr[1]
        }.toMap()
        val ariangAddresses: List<String> = PreferenceManager.getDefaultSharedPreferences(
            activity.baseContext
        ).getString("ariang_addresses", "")!!.split(Regex("\\n"))
        for (address in ariangAddresses) {
            deviceDataList.add(DeviceData(dict[address] ?: "AriaNG", null, address))
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var deviceData: DeviceData? = null
        var tvDeviceName: TextView = view.findViewById(R.id.tvDeviceName)
        var tvDeviceHostname: TextView = view.findViewById(R.id.tvDeviceHostname)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (deviceData != null) {
                val bundle = bundleOf("hostname" to deviceData!!.hostname)
                v?.findNavController()?.navigate(R.id.nav_ariang, bundle)
            }
        }
    }

    init {
        deviceDataList = ArrayList<DeviceData>()
        this.activity = activity
    }
}