package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_CHAMPAGNE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_ENGLISH
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_FRENCH
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_TYPE
import com.creativeduck.mrdaebak.ApplicationClass.Companion.DINNER_VALENTINE
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityDinnerBinding
import com.creativeduck.mrdaebak.util.goActivityWithInt

class DinnerActivity : BaseActivity<ActivityDinnerBinding>(ActivityDinnerBinding::inflate) {

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbDinner, false)

        initClick()
    }

    override fun initClick() {
        with(binding) {
            btnDinnerValentine.setOnClickListener {
                goActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, DINNER_VALENTINE))
            }
            btnDinnerFrench.setOnClickListener {
                goActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, DINNER_FRENCH))
            }
            btnDinnerEnglish.setOnClickListener {
                goActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, DINNER_ENGLISH))
            }
            btnDinnerChampagne.setOnClickListener {
                goActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, DINNER_CHAMPAGNE))
            }
            fabDinnerStt.setOnClickListener {
                goActivityWithInt<SttDinnerActivity>()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_dinner, this.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_dinner_order -> {
                goActivityWithInt<OrderListActivity>()
            }
            R.id.menu_dinner_user -> {
                goActivityWithInt<SettingActivity>()
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}