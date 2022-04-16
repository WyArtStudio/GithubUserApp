package com.wahyuhw.githubuserapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wahyuhw.githubuserapp.R
import com.wahyuhw.githubuserapp.data.remote.network.ResponseCallback
import com.wahyuhw.githubuserapp.data.remote.network.ResponseResource
import com.wahyuhw.githubuserapp.data.shared.User
import com.wahyuhw.githubuserapp.databinding.ActivityFavoriteBinding
import com.wahyuhw.githubuserapp.ui.adapter.ListUserAdapter
import com.wahyuhw.githubuserapp.utils.Utils
import com.wahyuhw.githubuserapp.viewmodel.MainViewModel
import com.wahyuhw.githubuserapp.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), ResponseCallback<List<User>> {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding as ActivityFavoriteBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(application))[MainViewModel::class.java]

        loadData()

        binding.btnClear.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.dialog_clear).setCancelable(true).setPositiveButton(
                resources.getText(R.string.yes)) { _, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    binding.progressBar.visibility = visible
                    viewModel.clearFavoriteUser()
                    binding.progressBar.visibility = gone
                    listUserAdapter.clearData()
                }
            }.setNegativeButton(resources.getText(R.string.no)) { dialog, _ -> dialog.cancel() }
            builder.create().show()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getListFavoriteUser().observe(this@FavoriteActivity) { users ->
                when (users) {
                    is ResponseResource.Loading -> onLoading()
                    is ResponseResource.Success -> users.data?.let { onSuccess(it) }
                    is ResponseResource.Error -> onFailed(users.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRestart() {
        listUserAdapter.clearData()
        super.onRestart()
        loadData()
    }

    override fun onSuccess(data: List<User>) {
        binding.progressBar.visibility = gone
        listUserAdapter = ListUserAdapter()
        binding.rvUsers.adapter = listUserAdapter
        listUserAdapter.setUsersData(data)
    }

    override fun onLoading() {
        binding.progressBar.visibility = visible
    }

    override fun onFailed(message: String?) {
        binding.progressBar.visibility = gone
        if (message != null) {
            Utils.showLongToast(this@FavoriteActivity, message)
        }
    }
}