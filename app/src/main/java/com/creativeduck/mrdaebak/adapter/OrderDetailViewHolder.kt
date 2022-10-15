package com.creativeduck.mrdaebak.adapter

import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderDetailBinding
import com.creativeduck.mrdaebak.model.OrderDetailModel
import com.creativeduck.mrdaebak.util.moneyFormat

class OrderDetailViewHolder(
    private val binding: ItemOrderDetailBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderDetailModel) {
        with(binding) {
            tvOrderDetailName.text = item.name
            tvOrderDetailPrice.text = item.price.moneyFormat()
            tvOrderDetailAmount.text = "${item.amount}개"
        }
    }
}