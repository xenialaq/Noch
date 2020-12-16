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
import okhttp3.Request
import org.apache.commons.net.util.SubnetUtils
import xyz.mnch.noch.MainActivity
import xyz.mnch.noch.R
import xyz.mnch.noch.data.DeviceData
import java.net.ConnectException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit

class AccessPointListAdapter(activity: MainActivity) :
    RecyclerView.Adapter<AccessPointListAdapter.ViewHolder>() {
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
        holder.tvDeviceBrand.text = deviceData.name
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
        val accessPointAddressRange: String = PreferenceManager.getDefaultSharedPreferences(
            activity
        ).getString("access_point_address_range", "")!!
        var subnet = SubnetUtils(accessPointAddressRange)
        val min = subnet.info.lowAddress
        val addressCount = subnet.info.addressCountLong
        subnet = SubnetUtils("$min/31")
        Thread {
            for (index in 1..addressCount) {
                val address = subnet.info.address
                Log.d("noch", address)
                val request = Request.Builder()
                    .url("http://$address/web-static/language/cn/str.js")
                    .build()
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful && response.body != null) {
                            val body = response.body!!.string()
                            if (body.indexOf("TP-LINK ID") >= 0) {
                                deviceDataList.add(DeviceData(dict[address] ?: "TP-LINK", null, address))
                            } else if (body.indexOf("MERCURY ID") >= 0) {
                                deviceDataList.add(DeviceData(dict[address] ?: "MERCURY", null, address))
                            }
                        }
                    }
                } catch (ex: SocketTimeoutException) {

                } catch (ex: ConnectException) {

                } catch (ex: Exception) {
                    Log.d("noch", ex.toString())
                }
                subnet = subnet.next
            }
            activity.runOnUiThread(Thread {
                notifyDataSetChanged()
            })
        }.start()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var deviceData: DeviceData? = null
        var tvDeviceBrand: TextView = view.findViewById(R.id.tvDeviceName)
        var tvDeviceHostname: TextView = view.findViewById(R.id.tvDeviceHostname)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (deviceData != null) {
                val bundle = bundleOf("hostname" to deviceData!!.hostname)
                v?.findNavController()?.navigate(R.id.nav_access_point, bundle)
            }
        }
    }

    init {
        deviceDataList = ArrayList<DeviceData>()
        this.activity = activity
    }
}