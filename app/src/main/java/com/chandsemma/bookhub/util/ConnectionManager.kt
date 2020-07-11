package com.chandsemma.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context): Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo
        //The class ConnectivityManager has the method activeNetworkInfo which is used to fetch data about active networks

        if(activeNetwork?.isConnected!=null){   //isConnected method of NetworkInfo class and is used to know
            // if the network is connected to the internet, or no
            //This method can return 3 values:
            //1) True: if the Network Has Internet
            //2) False: if the Network does NOT Have Internet
            //3) Null: if the Network is Broken/Inactive
            return activeNetwork.isConnected
        }
        else {
            return false
        }
    }
}