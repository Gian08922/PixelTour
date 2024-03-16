package com.example.pixeltourapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Buscador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buscador)
        val siguiente = findViewById<View>(R.id.Buscar) as Button

        siguiente.setOnClickListener {
            val intent = Intent(this, Pag2::class.java)
            startActivity(intent)
        }

    }
}