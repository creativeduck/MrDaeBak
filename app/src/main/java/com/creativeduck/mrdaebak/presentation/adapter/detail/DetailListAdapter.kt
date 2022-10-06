package com.creativeduck.mrdaebak.presentation.adapter.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creativeduck.mrdaebak.databinding.ItemDetailHeaderBinding
import com.creativeduck.mrdaebak.databinding.ItemDetailItemBinding
import com.creativeduck.mrdaebak.domain.detail.DetailModel
import com.creativeduck.mrdaebak.presentation.adapter.ItemDiffCallback

class DetailListAdapter : ListAdapter<DetailModel.DetailItemModel, RecyclerView.ViewHolder>(ItemDiffCallback<DetailModel.DetailItemModel>(
    onItemsTheSame = { old, new -> old.price == new.price },
    onContentsTheSame = { old, new -> old == new }
)) {
    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        return when (viewType) {
            DETAIL_ITEM -> {
                val binding = ItemDetailItemBinding.inflate(inflater, parent, false)
                DetailItemViewHolder(binding)
            }
            else -> {
                val binding = ItemDetailHeaderBinding.inflate(inflater, parent, false)
                DetailHeaderViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DetailItemViewHolder -> {
                holder.bind(getItem(position) as DetailModel.DetailItemModel)
            }
            is DetailHeaderViewHolder -> {
                holder.bind(getItem(position) as DetailModel.DetailHeaderModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DetailModel.DetailItemModel -> {
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

