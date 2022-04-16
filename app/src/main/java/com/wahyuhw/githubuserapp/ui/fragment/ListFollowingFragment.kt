package com.wahyuhw.githubuserapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.wahyuhw.githubuserapp.databinding.FragmentListFollowingBinding
import com.wahyuhw.githubuserapp.data.shared.User
import com.wahyuhw.githubuserapp.data.remote.network.ResponseCallback
import com.wahyuhw.githubuserapp.data.remote.network.ResponseResource
import com.wahyuhw.githubuserapp.ui.activity.DetailActivity
import com.wahyuhw.githubuserapp.ui.adapter.ListUserAdapter
import com.wahyuhw.githubuserapp.utils.Utils
import com.wahyuhw.githubuserapp.viewmodel.MainViewModel
import com.wahyuhw.githubuserapp.viewmodel.MainViewModelFactory

class ListFollowingFragment() : Fragment(), ResponseCallback<List<User>> {
    private var _binding: FragmentListFollowingBinding? = null
    private val binding get() = _binding as FragmentListFollowingBinding

    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = FragmentListFollowingBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this, MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME)

        listUserAdapter = ListUserAdapter()
        binding.rvUsers.adapter = listUserAdapter

        viewModel.getListFollowing(username!!).observe(viewLifecycleOwner) {
            when (it) {
                is ResponseResource.Error -> onFailed(it.message)
                is ResponseResource.Loading -> onLoading()
                is ResponseResource.Success -> it.data?.let { it1 -> onSuccess(it1) }
            }
        }
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
            Utils.showLongToast(requireContext(), message)
        }
    }
}