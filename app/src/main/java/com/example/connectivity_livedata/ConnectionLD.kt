package com.example.connectivity_livedata

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class ConnectionLD(context : Context):LiveData<Boolean>() {
    val TAG = "Connection mgt"
    private lateinit var networkCallback : ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks:MutableSet<Network> = HashSet()


    private fun checkValidNetwork(){
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(networkRequest, networkCallback)
    }


    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d(TAG, "onAvailible ${network}")
            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasInternetNetworkCapabilities = networkCapabilities?.hasCapability(
                NET_CAPABILITY_INTERNET
            )
            Log.d(TAG, "onAvailable ${network}, ${hasInternetNetworkCapabilities}")
            if (hasInternetNetworkCapabilities == true) {
                CoroutineScope(Dispatchers.IO).launch {
//                    val hasInternet = CheckNetworkInternet.execute(network.socketFactory
                        withContext(Dispatchers.Main)
                        {
                            Log.d(TAG, "onAvailable: adding network. ${network}")
                            validNetworks.add(network)
                            checkValidNetwork()
                        }

                }

            }
        }
        override fun onLost(network: Network) {
           Log.d(TAG, "onLost: ${network}")
            validNetworks.remove(network)
            checkValidNetwork()
        }
    }

}