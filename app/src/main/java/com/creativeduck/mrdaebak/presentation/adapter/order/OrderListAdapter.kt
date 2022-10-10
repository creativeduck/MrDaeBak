package com.creativeduck.mrdaebak.presentation.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderHeaderBinding
import com.creativeduck.mrdaebak.databinding.ItemOrderItemBinding
import com.creativeduck.mrdaebak.presentation.model.OrderModel
import com.creativeduck.mrdaebak.presentation.adapter.ItemDiffCallback
import com.creativeduck.mrdaebak.util.OnItemClickListener

class OrderListAdapter(
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
                DetailItemViewHolder(binding, addListener, minusListener, checkListener)
            }
            else -> {
                val binding = ItemOrderHeaderBinding.inflate(inflater, parent, false)
                DetailHeaderViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DetailItemViewHolder -> {
                holder.bind(getItem(position) as OrderModel.OrderItemModel)
            }
            is DetailHeaderViewHolder -> {
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

