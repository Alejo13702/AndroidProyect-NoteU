package com.example.proyectandroid.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectandroid.databinding.FragmentTasksBinding
import com.example.proyectandroid.interfaces.OnActionComplete
import com.example.proyectandroid.interfaces.OnRecyclerItemClick
import com.example.proyectandroid.interfaces.OnRecyclerItemLongClick
import com.example.proyectandroid.models.Task
import com.example.proyectandroid.ui.views.TaskItem
import com.example.proyectandroid.viewmodels.TasksViewModel
import com.xwray.groupie.GroupieAdapter

class TasksFragment : Fragment(), OnRecyclerItemClick, OnRecyclerItemLongClick, OnActionComplete {
    private lateinit var binding: FragmentTasksBinding

    private var textToAdd = ""

    private val recyclerAdapter by lazy { GroupieAdapter() }

    private val viewModel: TasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupRecycler()
        subscribeToObserver()
    }

    override fun onRecyclerItemClick(item: Any, position: Int) {
        viewModel.updateTask(item as Task, requireContext(), position)
    }

    override fun onRecyclerItemLong(item: Any, position: Int) {
        viewModel.deleteTask(position, requireContext(), this)
        recyclerAdapter.removeGroupAtAdapterPosition(position)
    }

    override fun onActionComplete() {
        viewModel.getTasks(requireContext())
    }

    private fun initViews() {
        binding.editTextAddWord.doAfterTextChanged {
            if (it.toString().length > 1)
                textToAdd = it.toString()
        }

        binding.buttonAddTask.setOnClickListener {
            if (binding.editTextAddWord.toString().length > 1) {
                recyclerAdapter.add(TaskItem(Task(textToAdd), this, this))
                viewModel.addTask(Task(textToAdd), requireContext())
                binding.apply {
                    root.clearFocus()
                    editTextAddWord.text = Editable.Factory().newEditable("")
                }
                textToAdd = ""
                hideSoftKeyboard()
            }
        }
    }

    private fun setupRecycler() {
        val manager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.apply {
            layoutManager = manager
            adapter = recyclerAdapter
        }
    }

    private fun subscribeToObserver() {
        viewModel.task.observe(viewLifecycleOwner) {
            updateRecycler(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            showLoader(it)
        }

        viewModel.getTasks(requireContext())
    }

    private fun updateRecycler(tasks: List<Task?>) {
        recyclerAdapter.clear()
        tasks.map {
            recyclerAdapter.add(TaskItem(it, this, this))
        }
    }

    private fun showLoader(loading: Boolean) {
        binding.laoder.visibility = if (loading) View.VISIBLE else View.GONE
        binding.recyclerViewTasks.visibility = if (!loading) View.VISIBLE else View.GONE
    }

    private fun hideSoftKeyboard() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}