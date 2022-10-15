package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderHeaderBinding
import com.creativeduck.mrdaebak.databinding.ItemOrderItemBinding
import com.creativeduck.mrdaebak.model.OrderModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback
import com.creativeduck.mrdaebak.util.OnItemClickListener

class OrderAdapter(
    private val addListener: OnItemClickListener,
    private val minusListener: OnItemClickListener,
    private val checkListener: (Int) -> Unit
) : ListAdapter<OrderModel, RecyclerView.ViewHolder>(ItemDiffCallback<OrderModel>(
    onItemsTheSame = { old, new -> old.title == new.title },
    onContentsTheSame = { old, new -> old == new }
)) {
    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        return when (viewType) {
            DETAIL_ITEM -> {
                val binding = ItemOrderItemBinding.inflate(inflater, parent, false)
                OrderItemViewHolder(binding, addListener, minusListener, checkListener)
            }
            else -> {
                val binding = ItemOrderHeaderBinding.inflate(inflater, parent, false)
                OrderHeaderViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderItemViewHolder -> {
                holder.bind(getItem(position) as OrderModel.OrderItemModel)
            }
            is OrderHeaderViewHolder -> {
                holder.bind(getItem(position) as OrderModel.OrderHeaderModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is OrderModel.OrderItemModel -> {
                DETAIL_ITEM
            }
            else -> {
                DETAIL_HEADER
            }
        }
    }

    companion object {
        const val DETAIL_ITEM = 0
        const val DETAIL_HEADER = 1
    }
}

