package com.creativeduck.mrdaebak.model

sealed class OrderModel(open val title: String) {
    data class OrderSimpleModel(
        val id: Long,
        override val title: String,
        val amount: Int
    ): OrderModel(title)

    data class OrderItemModel(
        val id: Long,
        override val title: String,
        val price: Long,
        var amount: Int,
        var isChecked: Boolean
    ): OrderModel(title)

    data class OrderHeaderModel(
        override val title: String
    ): OrderModel(title)
}