package com.wahyuhw.githubuserapp.ui.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wahyuhw.githubuserapp.R
import com.wahyuhw.githubuserapp.ui.activity.DetailActivity
import com.wahyuhw.githubuserapp.ui.fragment.ListFollowerFragment
import com.wahyuhw.githubuserapp.ui.fragment.ListFollowingFragment

class FollowViewPagerAdapter(private val context: Context, fm: FragmentManager, private val username: String) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        val bundle = Bundle()
        bundle.putString(DetailActivity.EXTRA_USERNAME, username)

        if (position == 0) {
            fragment = ListFollowerFragment()
        } else if (position == 1) {
            fragment = ListFollowingFragment()
        }

        fragment?.arguments = bundle
        return fragment!!
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        var title = if (position == 0) {
            context.resources.getText(R.string.followers)
        } else {
            context.resources.getText(R.string.following)
        }

        return title
    }
}