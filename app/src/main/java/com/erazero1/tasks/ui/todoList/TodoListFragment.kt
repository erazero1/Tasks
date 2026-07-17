package com.erazero1.tasks.ui.todoList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.erazero1.tasks.R
import com.erazero1.tasks.databinding.FragmentTodoListBinding
import com.erazero1.tasks.domain.model.AppNetworkException
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TodoListFragment : Fragment() {
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    private val args: TodoListFragmentArgs by navArgs()
    private val viewModel: TodoListViewModel by viewModel { parametersOf(args.userId) }

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupFilterListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter { clickedTodo ->
            viewModel.toggleTodoStatus(clickedTodo.id)
        }
        binding.rvTodos.adapter = todoAdapter
    }

    private fun setupListeners() {
        binding.errorLayout.findViewById<View>(R.id.btnRetry)?.setOnClickListener {
            viewModel.loadTodos()
        }
    }

    private fun setupFilterListeners() {
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val filter = when (checkedIds.firstOrNull()) {
                R.id.chipInProgress -> TodoFilter.IN_PROGRESS
                R.id.chipCompleted -> TodoFilter.COMPLETED
                else -> TodoFilter.ALL
            }
            viewModel.onFilterChanged(filter)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: TodoUiState) {
        binding.progressBar.isVisible = state is TodoUiState.Loading
        binding.rvTodos.isVisible = state is TodoUiState.Success
        binding.errorLayout.isVisible = state is TodoUiState.Error
        binding.tvEmpty.isVisible = state is TodoUiState.Empty

        binding.chipGroupFilter.isVisible =
            state is TodoUiState.Success || state is TodoUiState.Empty

        when (state) {
            is TodoUiState.Success -> {
                todoAdapter.submitList(state.todos)
            }

            is TodoUiState.Error -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}