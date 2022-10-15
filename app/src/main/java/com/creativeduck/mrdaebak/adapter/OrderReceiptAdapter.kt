package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.creativeduck.mrdaebak.databinding.ItemOrderReceiptBinding
import com.creativeduck.mrdaebak.model.OrderReceiptModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback

class OrderReceiptAdapter : ListAdapter<OrderReceiptModel, OrderReceiptViewHolder>(ItemDiffCallback(
    onItemsTheSame = { old, new -> old.id == new.id },
    onContentsTheSame = { old, new -> old == new }
)) {
    private lateinit var inflater : LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderReceiptViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val binding = ItemOrderReceiptBinding.inflate(inflater, parent, false)
        return OrderReceiptViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderReceiptViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}