package com.creativeduck.mrdaebak.adapter

import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderHeaderBinding
import com.creativeduck.mrdaebak.model.OrderModel


class OrderHeaderViewHolder(
    private val binding: ItemOrderHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderModel.OrderHeaderModel) {
        binding.tvOrderTitle.text = item.title
    }
}