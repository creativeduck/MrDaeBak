package com.creativeduck.mrdaebak.adapter

import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderHeaderBinding
import com.creativeduck.mrdaebak.databinding.ItemOrderItemBinding
import com.creativeduck.mrdaebak.model.OrderModel
import com.creativeduck.mrdaebak.util.OnItemClickListener

class OrderItemViewHolder(
    private val binding: ItemOrderItemBinding,
    private val addListener: OnItemClickListener,
    private val minusListener: OnItemClickListener,
    private val checkListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderModel.OrderItemModel) {
        with(binding) {
            tvOrderAmount.text = "${item.amount}개"
            tvOrderPrice.text = "${item.price}원"
            tvOrderTitle.text = item.title
            cbOrder.isChecked = item.isChecked

            btnOrderAddAmount.setOnClickListener {
                addListener.onItemClicked(it, adapterPosition)
            }

            btnOrderMinusAmount.setOnClickListener {
                minusListener.onItemClicked(it, adapterPosition)
            }

            cbOrder.setOnCheckedChangeListener { view, isChecked ->
                checkListener(adapterPosition)
            }
        }
    }
}

class OrderHeaderViewHolder(
    private val binding: ItemOrderHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderModel.OrderHeaderModel) {
        binding.tvOrderTitle.text = item.title
    }
}