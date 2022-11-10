package com.example.proyectandroid.ui.views

import android.view.View
import com.example.proyectandroid.R
import com.example.proyectandroid.databinding.TaskItemBinding
import com.example.proyectandroid.interfaces.OnRecyclerItemClick
import com.example.proyectandroid.interfaces.OnRecyclerItemLongClick
import com.example.proyectandroid.models.Task
import com.xwray.groupie.viewbinding.BindableItem

class TaskItem(
    private val task: Task?,
    private val onRecyclerItemClick: OnRecyclerItemClick,
    private val onRecyclerItemLongClick: OnRecyclerItemLongClick
) : BindableItem<TaskItemBinding>() {

    override fun bind(binding: TaskItemBinding, position: Int) {
        binding.taskTitle.text = task?.task
        binding.check.isChecked = task?.checked == true

        binding.check.setOnClickListener {
            task?.checked = task?.checked == true
            if (task != null) {
                onRecyclerItemClick.onRecyclerItemClick(task, position)
            }
        }

        binding.root.setOnLongClickListener {
            if (task != null) {
                onRecyclerItemLongClick.onRecyclerItemLong(task, position)
            }
            true
        }
    }

    override fun getLayout(): Int = R.layout.task_item

    override fun initializeViewBinding(view: View): TaskItemBinding = TaskItemBinding.bind(view)
}