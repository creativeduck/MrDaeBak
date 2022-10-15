package com.creativeduck.mrdaebak.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkManager {

    companion object {
        fun checkNetwork(
            context: Context,
            callApi : () -> Unit,
            notConnected : () -> Unit
        ) {
            val connectivityManger = context.getSystemService(ConnectivityManager::class.java)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManger.activeNetwork
                val networkCapabilities = connectivityManger.getNetworkCapabilities(activeNetwork)
                if (networkCapabilities != null) {
                    when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            callApi()
                        }
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            callApi()
                        }
                        else -> {
                            notConnected()
                        }
                    }
                } else {
                    notConnected()
                }
            } else {
                val networkInfo = connectivityManger.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) {
                    callApi()
                } else {
                    notConnected()
                }
            }
        }
    }

}