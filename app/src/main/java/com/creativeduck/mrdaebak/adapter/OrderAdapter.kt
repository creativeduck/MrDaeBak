package com.creativeduck.mrdaebak.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemOrderHeaderBinding
import com.creativeduck.mrdaebak.databinding.ItemOrderItemBinding
import com.creativeduck.mrdaebak.data.OrderModel
import com.creativeduck.mrdaebak.util.ItemDiffCallback
import com.creativeduck.mrdaebak.util.OnItemClickListener

class OrderAdapter(
    private val addListener: OnItemClickListener,
    private val minusListener: OnItemClickListener,
    private val checkListener: (Int) -> Unit
) : ListAdapter<OrderModel, RecyclerView.ViewHolder>(ItemDiffCallback<OrderModel>(
    onItemsTheSame = { old, new -> old.id == new.id },
    onContentsTheSame = { old, new -> old == new }
)) {
    private lateinit var inflater: LayoutInflater

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


class OrderHeaderViewHolder(
    private val binding: ItemOrderHeaderBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderModel.OrderHeaderModel) {
        binding.tvOrderTitle.text = item.title
    }
}

class OrderItemViewHolder(
    private val binding: ItemOrderItemBinding,
    private val addListener: OnItemClickListener,
    private val minusListener: OnItemClickListener,
    private val checkListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: OrderModel.OrderItemModel) {
        with(binding) {
            tvOrderAmount.text = "${item.menu.amount}개"
            tvOrderPrice.text = "${item.menu.price}원"
            tvOrderTitle.text = item.menu.name

            btnOrderAddAmount.setOnClickListener {
                addListener.onItemClicked(it, adapterPosition)
            }

            btnOrderMinusAmount.setOnClickListener {
                minusListener.onItemClicked(it, adapterPosition)
            }

            cbOrder.setOnCheckedChangeListener(null)
            cbOrder.isChecked = item.isChecked
            cbOrder.setOnCheckedChangeListener { _, _ ->
                checkListener(adapterPosition)
            }
        }
    }
}