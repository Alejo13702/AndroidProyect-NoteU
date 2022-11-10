package com.example.proyectandroid.ui.views

import android.view.View
import com.example.proyectandroid.R
import com.example.proyectandroid.databinding.NoteItemBinding
import com.example.proyectandroid.interfaces.OnRecyclerItemClick
import com.example.proyectandroid.interfaces.OnRecyclerItemLongClick
import com.example.proyectandroid.models.Note
import com.xwray.groupie.viewbinding.BindableItem

class NoteItem(
    private val note: Note?,
    private val onRecyclerItemClick: OnRecyclerItemClick,
    private val onRecyclerItemLongClick: OnRecyclerItemLongClick
) : BindableItem<NoteItemBinding>() {

    override fun bind(binding: NoteItemBinding, position: Int) {
        binding.textViewNoteItemTitle.text = note?.title

        binding.root.setOnClickListener {
            onRecyclerItemClick.onRecyclerItemClick(note!!, position)
        }

        binding.root.setOnLongClickListener {
            onRecyclerItemLongClick.onRecyclerItemLong(note!!, position)
            true
        }
    }

    override fun getLayout(): Int = R.layout.note_item

    override fun initializeViewBinding(view: View): NoteItemBinding = NoteItemBinding.bind(view)
}