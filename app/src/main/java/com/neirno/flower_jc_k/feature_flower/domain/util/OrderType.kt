package com.neirno.flower_jc_k.feature_flower.domain.util

sealed class OrderType {
    object Ascending: OrderType() // по возрастанию
    object Descending: OrderType()  // по убыванию
}