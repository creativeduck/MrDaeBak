package com.creativeduck.mrdaebak.data

data class OrderDto(
    val id: Long = 0L,
    val dinner: String,
    val style: String,
    val totalPrice: Long,
    val orderState: String = "NOT_RECEIVED",
    val menuList: List<MenuModel>,
    val orderTime: String = "현재",
    val address: String
)

data class OrderListDto(
    val orderList: List<OrderDto>
)

data class MenuModel(
    val name: String,
    val price: Long,
    var amount: Int
)

sealed class OrderModel(open val id: Long) {
    data class OrderItemModel(
        override val id: Long,
        val menu: MenuModel,
        var isChecked: Boolean
    ): OrderModel(id)

    data class OrderHeaderModel(
        override val id: Long,
        val title: String
    ): OrderModel(id)
}

data class IngredientModel(
    val name: String,
    val price: Long,
    var amount: Int
)

data class IngredientListModel(
    val ingredientList: List<IngredientModel>
)

data class IngredientItemModel(
    val ingredient: IngredientModel,
    var isChecked: Boolean
)