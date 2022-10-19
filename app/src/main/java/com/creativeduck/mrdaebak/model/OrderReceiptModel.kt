package com.creativeduck.mrdaebak.model

data class OrderReceiptModel(
    val id: Long,
    val dinner: String,
    val style: String,
    val price: Long,
    val menus: List<OrderDetailModel>,
    val address: String,
    val orderTime: String,
    val state: Int
)