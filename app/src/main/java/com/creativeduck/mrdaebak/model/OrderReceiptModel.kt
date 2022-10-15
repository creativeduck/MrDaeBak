package com.creativeduck.mrdaebak.model

data class OrderReceiptModel(
    val id: Long,
    val dinner: String,
    val style: String,
    val price: Long,
    val menus: List<OrderModel.OrderSimpleModel>,
    val address: String,
    val order_time: String,
    val state: Int
)