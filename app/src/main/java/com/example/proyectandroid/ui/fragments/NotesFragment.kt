package com.example.proyectandroid.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectandroid.R
import com.example.proyectandroid.databinding.FragmentNotesBinding
import com.example.proyectandroid.interfaces.OnRecyclerItemClick
import com.example.proyectandroid.interfaces.OnRecyclerItemLongClick
import com.example.proyectandroid.models.Note
import com.example.proyectandroid.ui.views.NoteItem
import com.example.proyectandroid.utils.DOC_ID
import com.example.proyectandroid.viewmodels.NotesViewModel
import com.xwray.groupie.GroupieAdapter

class NotesFragment : Fragment(), OnRecyclerItemClick, OnRecyclerItemLongClick {

    private lateinit var binding: FragmentNotesBinding

    private val recyclerAdapter by lazy { GroupieAdapter() }

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        subscribeToObserver()
        initListeners()
    }

    override fun onRecyclerItemClick(item: Any, position: Int) {
        item as Note
        findNavController().navigate(
            R.id.actionToNoteDetail,
            Bundle().apply { putString(DOC_ID, viewModel.docsIds[position]) })
    }

    override fun onRecyclerItemLong(item: Any, position: Int) {
        showDeleteDialog(position)
    }

    private fun setupRecycler() {
        val manager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.apply {
            layoutManager = manager
            adapter = recyclerAdapter
        }
    }

    private fun subscribeToObserver() {
        viewModel.notes.observe(viewLifecycleOwner) {
            updateRecycler(it)
        }

        viewModel.getNotes(requireContext())
    }

    private fun updateRecycler(notes: List<Note?>) {
        recyclerAdapter.clear()
        notes.map {
            recyclerAdapter.add(NoteItem(it, this, this))
        }
    }

    private fun initListeners() {
        binding.imageButtonNewTask.setOnClickListener {
            findNavController().navigate(
                R.id.actionToNoteDetail
            )
        }
    }

    private fun showDeleteDialog(position: Int) {
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Desea eliminar esta nota?")
            setPositiveButton("SI") { dialog, _ ->
                viewModel.deleteNote(position, requireContext())
                dialog.dismiss()
            }
            setNegativeButton("no") { dialog, _ ->
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}