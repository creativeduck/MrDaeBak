package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderDetailBinding
import com.creativeduck.mrdaebak.data.MenuModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback
import com.creativeduck.mrdaebak.util.moneyFormat

class OrderDetailAdapter : ListAdapter<MenuModel, OrderDetailViewHolder>(ItemDiffCallback(
    onItemsTheSame = { old, new -> old.name == new.name },
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

class OrderDetailViewHolder(
    private val binding: ItemOrderDetailBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MenuModel) {
        with(binding) {
            tvOrderDetailName.text = item.name
            tvOrderDetailPrice.text = item.price.moneyFormat()
            tvOrderDetailAmount.text = "${item.amount}개"
        }
    }
}