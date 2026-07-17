package com.erazero1.tasks.ui.todoList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erazero1.tasks.R
import com.erazero1.tasks.databinding.TodoItemBinding
import com.erazero1.tasks.domain.model.Todo
import com.erazero1.tasks.domain.model.TodoLocalStatus

class TodoAdapter(
    private val onTodoClick: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, onTodoClick)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TodoViewHolder(
        private val binding: TodoItemBinding,
        private val onTodoClick: (Todo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.tvTodoTitle.text = todo.title
            binding.tvServerStatus.text =
                binding.root.context.getString(R.string.server_format, todo.isServerCompleted)

            binding.cbLocalStatus.setOnCheckedChangeListener(null)

            binding.cbLocalStatus.isChecked = todo.localStatus == TodoLocalStatus.COMPLETED

            binding.cbLocalStatus.setOnCheckedChangeListener { _, _ ->
                onTodoClick(todo)
            }

            binding.root.setOnClickListener {
                onTodoClick(todo)
            }
        }
    }

    class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
}