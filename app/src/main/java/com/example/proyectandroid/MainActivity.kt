package com.example.proyectandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.room.Database

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToTarea : Button = findViewById(R.id.button)
        goToTarea.setOnClickListener {
            startActivity(Intent(this, Tarea::class.java))
        }

        val goCreateNota : ImageButton = findViewById(R.id.imageButton)
        goCreateNota.setOnClickListener {
            startActivity(Intent(this, CreateNota::class.java))
        }

    }

}

