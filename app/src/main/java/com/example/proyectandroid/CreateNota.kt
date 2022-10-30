package com.example.proyectandroid

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CreateNota : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_nota)

        val backMain : ImageButton = findViewById(R.id.backbuttom)
        backMain.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
}