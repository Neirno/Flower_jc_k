package com.neirno.flower_jc_k.feature_flower.domain.util

sealed class FlowerOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): FlowerOrder(orderType)
    class Water(orderType: OrderType): FlowerOrder(orderType)
    class Fertilize(orderType: OrderType): FlowerOrder(orderType)
    class Spraying(orderType: OrderType): FlowerOrder(orderType)

    fun copy(orderType: OrderType): FlowerOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Water -> Water(orderType)
            is Fertilize -> Fertilize(orderType)
            is Spraying -> Spraying(orderType)
        }
    }
}