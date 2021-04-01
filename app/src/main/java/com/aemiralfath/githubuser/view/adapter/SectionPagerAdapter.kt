package com.aemiralfath.githubuser.view.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.view.fragment.FollowFragment

class SectionPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT), OnDataPass {

    private lateinit var username: String

    companion object {
        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onDataPass(username: String) {
        this.username = username
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLE[position])
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return FollowFragment.newInstance(username, position)
    }
}