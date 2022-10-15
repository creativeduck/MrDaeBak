package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.creativeduck.mrdaebak.databinding.ItemOrderListBinding
import com.creativeduck.mrdaebak.model.OrderListModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback

class OrderListAdapter(
    private val itemClick: (OrderListModel) -> Unit
) : ListAdapter<OrderListModel, OrderListViewHolder>(ItemDiffCallback(
    onItemsTheSame = { old, new -> old.id == new.id },
    onContentsTheSame = { old, new -> old == new }
)) {
    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        val binding = ItemOrderListBinding.inflate(inflater, parent, false)
        return OrderListViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}