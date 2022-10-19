package com.creativeduck.mrdaebak.model

data class OrderListModel(
    val id: Long,
    val dinner: String,
    val style: String,
    val price: Long,
    val orderState: OrderState
)