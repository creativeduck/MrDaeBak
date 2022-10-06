package com.creativeduck.mrdaebak.domain.detail

sealed class DetailModel(open val title: String) {
    data class DetailItemModel(
        override val title: String,
        val price: Int,
        var amount: Int,
        var isChecked: Boolean
    ): DetailModel(title)

    data class DetailHeaderModel(
        override val title: String
    ): DetailModel(title)
}