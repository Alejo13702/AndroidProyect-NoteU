package com.example.proyectandroid.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.proyectandroid.databinding.FragmentNoteDetailBinding
import com.example.proyectandroid.interfaces.OnActionComplete
import com.example.proyectandroid.models.Note
import com.example.proyectandroid.utils.DOC_ID
import com.example.proyectandroid.utils.showShortMessage
import com.example.proyectandroid.viewmodels.NoteDetailViewModel

class NoteDetailFragment : Fragment(), OnActionComplete {
    private lateinit var binding: FragmentNoteDetailBinding

    private val viewModel: NoteDetailViewModel by viewModels()

    private var docId: String? = null

    private var note: Note? = Note()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(layoutInflater, container, false)
        arguments?.let {
            docId = it.getString(DOC_ID)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserver()
        initListeners()
    }

    override fun onActionComplete() {
        activity?.onBackPressed()
    }

    private fun subscribeToObserver() {
        viewModel.setOnUpdateCreateSuccess(this)

        viewModel.note.observe(viewLifecycleOwner) {
            note = it
            initViews()
        }

        if (docId != null)
            viewModel.getNote(docId.toString(), requireContext())
    }

    private fun initListeners() {
        binding.editTextTitle.doAfterTextChanged {
            note?.title = it.toString()
        }

        binding.editTextContent.doAfterTextChanged {
            note?.content = it.toString()
        }

        binding.buttonSaveNote.setOnClickListener {
            if (note != null && note?.title?.isBlank() == false && note?.content?.isBlank() == false) {
                if (docId != null)
                    viewModel.updateNote(docId.toString(), note!!, requireContext())
                else
                    viewModel.createNote(note!!, requireContext())
            } else
                showShortMessage("Debe ingresar titulo y contenido para la nota.", requireContext())
        }
    }

    private fun initViews() {
        binding.editTextTitle.text = Editable.Factory().newEditable(note?.title)
        binding.editTextContent.text = Editable.Factory().newEditable(note?.content)
    }
}