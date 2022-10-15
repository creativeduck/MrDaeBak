package com.creativeduck.mrdaebak.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.creativeduck.mrdaebak.R
import com.creativeduck.mrdaebak.databinding.ActivityDinnerBinding

class DinnerActivity : BaseActivity<ActivityDinnerBinding>(ActivityDinnerBinding::inflate) {

    companion object {
        const val VALENTINE = 0
        const val FRENCH = 1
        const val ENGLISH = 2
        const val CHAMPAGNE = 3
        const val DINNER_TYPE = "dinner type"
    }

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(binding.tbDinner, false)

        initClick()
    }

    private fun initClick() {
        with(binding) {
            btnDinnerValentine.setOnClickListener {
                startNewActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, VALENTINE))
            }
            btnDinnerFrench.setOnClickListener {
                startNewActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, FRENCH))
            }
            btnDinnerEnglish.setOnClickListener {
                startNewActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, ENGLISH))
            }
            btnDinnerChampagne.setOnClickListener {
                startNewActivityWithInt<StyleActivity>(Pair(DINNER_TYPE, CHAMPAGNE))
            }
            fabDinnerStt.setOnClickListener {
                startNewActivityWithInt<SttDinnerActivity>()
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
                startNewActivityWithInt<OrderListActivity>()
            }
            R.id.menu_dinner_user -> {
                startNewActivityWithInt<SettingActivity>()
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}