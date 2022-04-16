package com.wahyuhw.githubuserapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.wahyuhw.githubuserapp.R
import com.wahyuhw.githubuserapp.data.remote.network.ResponseCallback
import com.wahyuhw.githubuserapp.data.remote.network.ResponseResource
import com.wahyuhw.githubuserapp.data.shared.User
import com.wahyuhw.githubuserapp.databinding.ActivityDetailBinding
import com.wahyuhw.githubuserapp.ui.adapter.FollowViewPagerAdapter
import com.wahyuhw.githubuserapp.utils.Utils
import com.wahyuhw.githubuserapp.viewmodel.MainViewModel
import com.wahyuhw.githubuserapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), ResponseCallback<User> {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding as ActivityDetailBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application))[MainViewModel::class.java]

        val user = intent.getParcelableExtra<User>(EXTRA_USER)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getUserDetail(user?.username!!).observe(this@DetailActivity) {
                when (it) {
                    is ResponseResource.Error -> onFailed(it.message)
                    is ResponseResource.Loading -> onLoading()
                    is ResponseResource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                }
            }
        }

        binding.viewPager.adapter = FollowViewPagerAdapter(applicationContext, supportFragmentManager, user?.username!!)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bind(user: User?) {
        with(binding) {
            Glide.with(this@DetailActivity).load(user?.avatarUrl).into(imgProfile)
            tvName.text = user?.name
            val username = "@${user?.username}"
            tvUsername.text = username

            btnFavorite.isFavorite = user?.isFavorite == true

            btnFavorite.setOnFavoriteChangeListener { _, isFavorite ->
                if (isFavorite) {
                    btnFavorite.setAnimateFavorite(true)
                    user?.isFavorite = true
                    user?.let { it1 -> viewModel.insertFavoriteUser(it1) }
                    Utils.showShortToast(this@DetailActivity,
                        resources.getText(R.string.added_favorite) as String)
                } else {
                    user?.isFavorite = false
                    viewModel.deleteFavoriteUser(user!!)
                    Utils.showShortToast(this@DetailActivity,
                        resources.getText(R.string.deleted_favorite) as String)
                }
            }

            if (user?.bio != null) {
                tvBio.text = user.bio
            } else {
                tvBio.text = "-"
            }

            if (user?.company != null) {
                tvCompany.text = user.company
            } else {
                tvCompany.text = "-"
            }

            if (user?.location != null) {
                tvLocation.text = user.location
            } else {
                tvLocation.text = "-"
            }

            tvFollowers.text = user?.followers.toString()
            tvFollowing.text = user?.following.toString()
            tvRepository.text = user?.publicRepos.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSuccess(data: User) {
        binding.progressBar.visibility = gone
        bind(data)
    }

    override fun onLoading() {
        binding.progressBar.visibility = visible
    }

    override fun onFailed(message: String?) {
        binding.progressBar.visibility = gone
        if (message != null) {
            Utils.showLongToast(this@DetailActivity, message)
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_USERNAME = "extra_username"
    }
}