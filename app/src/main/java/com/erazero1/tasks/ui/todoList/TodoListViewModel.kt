package com.erazero1.tasks.ui.todoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erazero1.tasks.domain.model.Todo
import com.erazero1.tasks.domain.model.TodoLocalStatus
import com.erazero1.tasks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val todoRepository: TodoRepository,
    private val userId: Int
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    private val _activeFilter = MutableStateFlow(TodoFilter.ALL)
    private val _networkState = MutableStateFlow<TodoUiState>(TodoUiState.Initial)

    val uiState: StateFlow<TodoUiState> = combine(
        _networkState,
        _todos,
        _activeFilter
    ) { networkState, todos, filter ->

        if (networkState is TodoUiState.Loading || networkState is TodoUiState.Error) {
            return@combine networkState
        }

        if (todos.isEmpty()) {
            TodoUiState.Empty
        } else {
            val filteredList = when (filter) {
                TodoFilter.ALL -> todos
                TodoFilter.IN_PROGRESS -> todos.filter { it.localStatus == TodoLocalStatus.IN_PROGRESS }
                TodoFilter.COMPLETED -> todos.filter { it.localStatus == TodoLocalStatus.COMPLETED }
            }
            TodoUiState.Success(filteredList, filter)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodoUiState.Initial
    )

    init {
        loadTodos()
    }

    fun loadTodos() {
        _networkState.update { TodoUiState.Loading }
        viewModelScope.launch {
            todoRepository.getTodoListByUserId(userId)
                .onSuccess { todoList ->
                    _todos.update { todoList }
                    _networkState.update { TodoUiState.Initial }
                }
                .onFailure { throwable ->
                    _networkState.update { TodoUiState.Error(throwable) }
                }
        }
    }

    fun toggleTodoStatus(todoId: Int) {
        _todos.update { currentList ->
            currentList.map { todo ->
                if (todo.id == todoId) {
                    val newStatus = if (todo.localStatus == TodoLocalStatus.COMPLETED) {
                        TodoLocalStatus.IN_PROGRESS
                    } else {
                        TodoLocalStatus.COMPLETED
                    }
                    todo.copy(localStatus = newStatus)
                } else {
                    todo
                }
            }
        }
    }

    fun onFilterChanged(filter: TodoFilter) {
        _activeFilter.value = filter
    }
}