package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.creativeduck.mrdaebak.databinding.ItemOrderDetailBinding
import com.creativeduck.mrdaebak.model.OrderDetailModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback

class OrderDetailAdapter : ListAdapter<OrderDetailModel, OrderDetailViewHolder>(ItemDiffCallback(
    onItemsTheSame = { old, new -> old.id == new.id },
    onContentsTheSame = { old, new -> old == new }
)) {
    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemOrderDetailBinding.inflate(inflater, parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}