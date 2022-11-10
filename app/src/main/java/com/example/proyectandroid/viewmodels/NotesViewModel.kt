package com.example.proyectandroid.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectandroid.models.Note
import com.example.proyectandroid.utils.NOTES
import com.example.proyectandroid.utils.showLongMessage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }

    private val _notes: MutableLiveData<List<Note?>> by lazy { MutableLiveData<List<Note?>>() }
    val notes: LiveData<List<Note?>> = _notes

    val docsIds = mutableListOf<String>()

    fun getNotes(context: Context) {
        viewModelScope.launch {
            docsIds.clear()
            db.collection(NOTES).get()
                .addOnSuccessListener {
                    val currentNotes = mutableListOf<Note?>()
                    it.documents.map { document ->
                        docsIds.add(document.id)
                        currentNotes.add(document.toObject<Note>())
                    }
                    _notes.postValue(currentNotes)
                }
                .addOnFailureListener {
                    showLongMessage("Intente de nuevo mas tarde.", context)
                }
        }
    }

    fun deleteNote(position: Int, context: Context) {
        viewModelScope.launch {
            try {
                db.collection(NOTES).document(docsIds[position]).delete()
                    .addOnSuccessListener {
                        showLongMessage("Nota eliminada con exito!", context)
                        getNotes(context)
                    }
                    .addOnCanceledListener {
                        showLongMessage("Intente de nuevo mas tarde.", context)
                    }
            } catch (exception: Exception) {
                exception.printStackTrace()
                showLongMessage("Ha ocurrido un error.", context)
            }
        }
    }
}