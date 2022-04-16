package com.wahyuhw.githubuserapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.wahyuhw.githubuserapp.R
import com.wahyuhw.githubuserapp.databinding.ActivityMainBinding
import com.wahyuhw.githubuserapp.data.shared.User
import com.wahyuhw.githubuserapp.data.remote.network.ResponseCallback
import com.wahyuhw.githubuserapp.data.remote.network.ResponseResource
import com.wahyuhw.githubuserapp.ui.adapter.ListUserAdapter
import com.wahyuhw.githubuserapp.utils.Utils
import com.wahyuhw.githubuserapp.viewmodel.MainViewModel
import com.wahyuhw.githubuserapp.viewmodel.MainViewModelFactory
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), ResponseCallback<List<User>> {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var listUserAdapter: ListUserAdapter
    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application))[MainViewModel::class.java]

        listUserAdapter = ListUserAdapter()
        binding.rvUsers.adapter = listUserAdapter

        binding.btnFavorite.setOnClickListener {
            startActivity(Intent(applicationContext, FavoriteActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                viewModel.searchUser(query.toString()).observe(this@MainActivity) { users ->
                    when (users) {
                        is ResponseResource.Loading -> onLoading()
                        is ResponseResource.Success -> users.data?.let { onSuccess(it) }
                        is ResponseResource.Error -> onFailed(users.message)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { return false }
        })
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            finish()
            exitProcess(0)
        }

        this.doubleBackToExit = true
        Toast.makeText(this, resources.getText(R.string.click_exit), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExit = false }, delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSuccess(data: List<User>) {
        binding.progressBar.visibility = gone
        listUserAdapter.setUsersData(data)
    }

    override fun onLoading() {
        binding.progressBar.visibility = visible
    }

    override fun onFailed(message: String?) {
        binding.progressBar.visibility = gone
        if (message != null) {
            Utils.showLongToast(this@MainActivity, message)
        }
    }

    companion object {
        const val delay = 2000L
    }
}