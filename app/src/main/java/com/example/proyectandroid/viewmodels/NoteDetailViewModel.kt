package com.example.proyectandroid.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectandroid.interfaces.OnActionComplete
import com.example.proyectandroid.models.Note
import com.example.proyectandroid.utils.NOTES
import com.example.proyectandroid.utils.showLongMessage
import com.example.proyectandroid.utils.showShortMessage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class NoteDetailViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }

    private val _note: MutableLiveData<Note?> by lazy { MutableLiveData<Note?>() }
    val note: LiveData<Note?> = _note

    private lateinit var onActionComplete: OnActionComplete

    fun getNote(docId: String, context: Context) {
        viewModelScope.launch {
            try {
                db.collection(NOTES).document(docId).get()
                    .addOnSuccessListener {
                        _note.postValue(it.toObject<Note>())
                    }
                    .addOnCanceledListener {
                        showLongMessage("No se ha encontrado la nota.", context)
                    }
            } catch (exception: Exception) {
                exception.printStackTrace()
                showLongMessage("No se ha encontrado la nota.", context)
            }
        }
    }

    fun updateNote(docId: String, note: Note, context: Context) {
        viewModelScope.launch {
            db.collection(NOTES).document(docId).set(note)
                .addOnSuccessListener {
                    showShortMessage("Nota actualizada correctamente!", context)
                    onActionComplete.onActionComplete()
                }
                .addOnCanceledListener {
                    showShortMessage("Hubo un error inesperado!", context)
                }
        }
    }

    fun createNote(note: Note, context: Context) {
        viewModelScope.launch {
            db.collection(NOTES).add(note)
                .addOnSuccessListener {
                    showShortMessage("Nota creada correctamente!", context)
                    onActionComplete.onActionComplete()
                }
                .addOnCanceledListener {
                    showShortMessage("Hubo un error inesperado!", context)
                }
        }
    }

    fun setOnUpdateCreateSuccess(onActionComplete: OnActionComplete) {
        this.onActionComplete = onActionComplete
    }
}