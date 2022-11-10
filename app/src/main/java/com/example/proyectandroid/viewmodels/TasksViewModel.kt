package com.example.proyectandroid.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectandroid.interfaces.OnActionComplete
import com.example.proyectandroid.models.Task
import com.example.proyectandroid.utils.TASKS
import com.example.proyectandroid.utils.showShortMessage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {
    private val db by lazy { Firebase.firestore }

    private val _tasks: MutableLiveData<List<Task?>> by lazy { MutableLiveData<List<Task?>>() }
    val task: LiveData<List<Task?>> = _tasks

    private val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading = _loading

    private val docsIds = mutableListOf<String>()

    fun getTasks(context: Context) {
        _loading.postValue(true)
        viewModelScope.launch {
            db.collection(TASKS).get()
                .addOnSuccessListener {
                    val currentTasks = mutableListOf<Task?>()
                    it.documents.map { document ->
                        currentTasks.add(document.toObject<Task>())
                        docsIds.add(document.id)
                    }
                    _tasks.postValue(currentTasks)
                    _loading.postValue(false)
                }
                .addOnCanceledListener {
                    showShortMessage("Hubo un error inesperado!", context)
                }
        }
    }

    fun addTask(
        taskToAdd: Task,
        context: Context
    ) {
        viewModelScope.launch {
            db.collection(TASKS).add(taskToAdd)
                .addOnSuccessListener {
                    showShortMessage("Tarea creada", context)
                    docsIds.add(it.id)
                }
                .addOnCanceledListener {
                    showShortMessage("Hubo un error inesperado!", context)
                }
        }
    }

    fun updateTask(
        taskToUpdate: Task,
        context: Context,
        position: Int
    ) {
        viewModelScope.launch {
            db.collection(TASKS).document(docsIds[position]).set(taskToUpdate)
                .addOnSuccessListener {
                    showShortMessage("Tarea actualizada", context)
                }
                .addOnCanceledListener {
                    showShortMessage("Hubo un error inesperado!", context)
                }
        }
    }

    fun deleteTask(position: Int, context: Context, onActionComplete: OnActionComplete) {
        viewModelScope.launch {
            db.collection(TASKS).document(docsIds[position]).delete()
                .addOnSuccessListener {
                    docsIds.clear()
                    onActionComplete.onActionComplete()
                    showShortMessage("Tarea eliminada!", context)
                }
                .addOnCanceledListener {
                    showShortMessage("Ha ocurrido un error, intente nuevamente", context)
                }
        }
    }

}
