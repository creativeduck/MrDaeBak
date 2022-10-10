package com.creativeduck.mrdaebak.presentation.model

sealed class OrderModel(open val title: String) {
    data class OrderItemModel(
        override val title: String,
        val price: Int,
        var amount: Int,
        var isChecked: Boolean
    ): OrderModel(title)

    data class OrderHeaderModel(
        override val title: String
    ): OrderModel(title)
}