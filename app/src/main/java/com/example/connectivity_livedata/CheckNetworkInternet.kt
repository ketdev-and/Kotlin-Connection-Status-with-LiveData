package com.example.connectivity_livedata

import android.content.ContentValues.TAG
import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object CheckNetworkInternet {
    fun execute(socketFactory: SocketFactory):Boolean{
        return try {
            Log.d(TAG, "pinging google")
            val socket = socketFactory.createSocket()?:throw IOException("Socket is null")
            socket.connect(InetSocketAddress("8.8.4.4", 853), 1500)
            socket.close()
            Log.d(TAG, "ping success")
            true
        }catch (e: IOException){
            Log.e(TAG, "No internet connection. ${e}")
            false
        }
    }
}