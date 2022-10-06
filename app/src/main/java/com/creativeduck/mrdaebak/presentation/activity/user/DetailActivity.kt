package com.creativeduck.mrdaebak.presentation.activity.user

import android.os.Bundle
import com.creativeduck.mrdaebak.databinding.ActivityDetailBinding
import com.creativeduck.mrdaebak.domain.detail.DetailModel
import com.creativeduck.mrdaebak.presentation.activity.BaseActivity
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.ENGLISH
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.FRENCH
import com.creativeduck.mrdaebak.presentation.activity.user.DinnerActivity.Companion.VALENTINE
import com.creativeduck.mrdaebak.presentation.activity.user.StyleActivity.Companion.GRAND
import com.creativeduck.mrdaebak.presentation.activity.user.StyleActivity.Companion.SIMPLE
import com.creativeduck.mrdaebak.presentation.activity.user.StyleActivity.Companion.STYLE_TYPE
import com.creativeduck.mrdaebak.presentation.adapter.detail.DetailListAdapter

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {

    private lateinit var detailListAdapter: DetailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dinnerType = intent.getIntExtra(DINNER_TYPE, VALENTINE)
        val styleType = intent.getIntExtra(STYLE_TYPE, SIMPLE)

        initText(dinnerType, styleType)
        initClick()
        initAdapter()
    }

    private fun initText(dinnerType: Int, styleType: Int) {
        with(binding) {
            tvDetailDinner.text =
                when (dinnerType) {
                    VALENTINE -> { "발렌타인 디너" }
                    FRENCH -> { "프렌치 디너" }
                    ENGLISH -> { "잉글리시 디너" }
                    else -> { "샴페인 디너" }
            }
            tvDetailStyle.text =
                when (styleType) {
                    SIMPLE -> { "심플 디너" }
                    GRAND -> { "그랜드 디너" }
                    else -> { "디럭스 디너" }
                }
        }
    }

    private fun initClick() {
        binding.btnDetailToOrder.setOnClickListener {
                // TODO 주문 데이터 POST 하기

                // TODO 주문 상세 내역 화면으로 이동

                // TODO 주문 상세 화면에서 뒤로오면 메인 액티비티가 좋을 거 같은데, 처리 어떻게 할까.

        }
    }
    private fun initAdapter() {
        detailListAdapter = DetailListAdapter()

        binding.recyclerDetail.apply {
            adapter = detailListAdapter
        }


        val detailList = ArrayList<DetailModel.DetailItemModel>()
        for(i in 0 until 10) {
            detailList.add(DetailModel.DetailItemModel(
                "크리스피", 7500 + i * 100, 0, false
            ))
        }
        detailListAdapter.submitList(detailList.toList())
    }
}