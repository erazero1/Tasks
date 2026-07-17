package com.erazero1.tasks.ui.userList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.erazero1.tasks.R
import com.erazero1.tasks.databinding.FragmentUserListBinding
import com.erazero1.tasks.domain.model.AppNetworkException
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserListViewModel by viewModel()

    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupListeners()
        setupEdgeToEdgeInsets()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter { clickedUser ->
            val action = UserListFragmentDirections.actionUserListFragmentToUserDetailsFragment(
                user = clickedUser
            )
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = userAdapter
    }

    private fun setupSearch() {
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text?.toString().orEmpty())
        }
    }

    private fun setupListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadUserList()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { uiState ->
                    renderState(uiState)
                }
            }
        }
    }

    private fun renderState(state: UserListUiState) {
        binding.progressBar.isVisible = state is UserListUiState.Loading
        binding.recyclerView.isVisible = state is UserListUiState.Success
        binding.errorLayout.isVisible = state is UserListUiState.Error
        binding.tvEmpty.isVisible = state is UserListUiState.Empty

        when (state) {
            is UserListUiState.Success -> {
                userAdapter.submitList(state.users)
            }

            is UserListUiState.Error -> {
                binding.tvErrorMessage.text = mapThrowableToStringRes(state.throwable)
            }

            else -> {}
        }
    }

    private fun mapThrowableToStringRes(throwable: Throwable): String {
        return when (throwable) {
            is AppNetworkException.NoInternetException -> {
                getString(R.string.error_no_internet)
            }

            is AppNetworkException.ServerException -> {
                getString(R.string.error_server, throwable.code)
            }

            is AppNetworkException.UnknownNetworkException -> {
                getString(R.string.unknown_error)
            }

            else -> throwable.localizedMessage ?: getString(R.string.error_occurred)
        }
    }

    private fun setupEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, windowInsets ->
            val navBars = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, navBars.bottom)
            windowInsets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}