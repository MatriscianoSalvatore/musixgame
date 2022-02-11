package com.matrisciano.network.network

class Network {
    fun <T:Any>createServiceApi(apiClass: Class<T>):T = NetworkClient().retrofit.create(apiClass)
}