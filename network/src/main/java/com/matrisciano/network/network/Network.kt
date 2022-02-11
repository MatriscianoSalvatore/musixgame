package com.matrisciano.network.network

import kotlin.reflect.KClass

class Network {
    fun <T:Any>createServiceApi(apiClass: KClass<T>):T = NetworkClient().retrofit.create(apiClass.java)
}