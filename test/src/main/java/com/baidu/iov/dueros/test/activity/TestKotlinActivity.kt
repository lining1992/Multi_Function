package com.baidu.iov.dueros.test

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.baidu.iov.dueros.test.view.CustomTextView
import com.google.android.material.tabs.TabLayout

/**
 *
 * @author v_lining05
 * @date   2020-08-22
 *
 */
class TestKotlinActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var tableLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var string  = arrayOf<String>("layout1", "layout2", "layout3")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        tableLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        val view1: View = layoutInflater.inflate(R.layout.layout1, null)
        val view2: View = layoutInflater.inflate(R.layout.layout2, null)
        val view3: View = layoutInflater.inflate(R.layout.layout3, null)
        val listOf = listOf<View>(view1, view2, view3)
        val kotlinViewPager = KotlinViewPager(listOf)
        viewPager.adapter = kotlinViewPager
        tableLayout.run {
            this.setupWithViewPager(viewPager)
            setupWithViewPager(viewPager)
            viewPager.adapter = kotlinViewPager
            getTabAt(0)?.setCustomView(CustomTextView(applicationContext)
                    .setTextChangeColor(Color.RED).setText(string[0]))?.text = string[0]
            getTabAt(1)?.setCustomView(CustomTextView(applicationContext)
                    .setTextChangeColor(Color.RED).setText(string[1]))?.text = string[1]
            getTabAt(2)?.setCustomView(CustomTextView(applicationContext)
                    .setTextChangeColor(Color.RED).setText(string[2]))?.text = string[2]
        }
        tableLayout.addOnTabSelectedListener(this)
        viewPager.addOnPageChangeListener(pagerListener)
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        // !!不可能为空
        viewPager.currentItem = p0!!.position
    }

    private var pagerListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            Log.d("debugli", "position=" + position
                    + "=positionOffset=" + positionOffset
                    + "=positionOffsetPixels=" + positionOffsetPixels)
            val left = tableLayout.getTabAt(position)?.customView as CustomTextView;
            left.setDirection(CustomTextView.Direction.RIGHT_TO_LEFT)
            left.setCurrentTextProgress(1 - positionOffset)

            if (position < string.size - 1) {
                val right = tableLayout.getTabAt(position + 1)?.customView as CustomTextView;
                right.setDirection(CustomTextView.Direction.LEFT_TO_RIGHT)
                right.setCurrentTextProgress(positionOffset)
            }
        }

        override fun onPageSelected(position: Int) {
        }

    }

}

class KotlinViewPager(list: List<View>) : PagerAdapter() {

    var viewList = list;

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun getCount(): Int {
        return viewList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(viewList.get(position))
        return viewList.get(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList.get(position))
    }
}

class AdapterFragment : FragmentPagerAdapter {

    private var mFragments: List<Fragment>

    constructor(fm: FragmentManager, fragments: List<Fragment>) : super(fm) {
        this.mFragments = fragments;
    }

    override fun getItem(position: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return mFragments.get(position)
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragments.get(position).javaClass.simpleName
    }
}