package com.creativeduck.mrdaebak.model

data class OrderDetailModel(
    val id: Long,
    val name: String,
    val price: Long,
    val amount: Int
)