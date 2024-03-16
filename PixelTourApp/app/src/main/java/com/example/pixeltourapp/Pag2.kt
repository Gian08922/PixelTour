package com.example.pixeltourapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Pag2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pag2)
        val seekBar: SeekBar = findViewById(R.id.seekBar)
        val textView: TextView = findViewById(R.id.seekBarValue)

// Inicializa el TextView con el valor inicial del SeekBar
        textView.text = seekBar.progress.toString()
        val siguiente = findViewById<View>(R.id.BotonRuta) as Button

        siguiente.setOnClickListener {
            val intent = Intent(this, Resultado::class.java)
            startActivity(intent)
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el TextView con el valor que se está seleccionando
                textView.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Aquí puedes manejar el evento cuando el usuario comienza a mover el SeekBar si es necesario
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Aquí puedes manejar el evento cuando el usuario suelta el SeekBar si es necesario
            }
        })
    }

}