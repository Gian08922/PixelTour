package com.example.flowmodor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EstadisticasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        // Obtener referencia al TextView para mostrar la fecha actual
        val txtDate = findViewById<TextView>(R.id.textView_date)

        // Obtener la fecha actual y formatearla como "dd/MM/yyyy"
        val currentDateT = Date()
        val dateFormatT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDateT = dateFormatT.format(currentDateT)

        // Mostrar la fecha formateada en el TextView
        txtDate.text = formattedDateT

        //Boton de concentracion
        val BotonConcentracion: Button = findViewById(R.id.boton_concentracion)
        BotonConcentracion.setOnClickListener {
            gotoConcentracion()
        }

        //Boton de concentracion
        val BotonProductividad: Button = findViewById(R.id.boton_productividad)
        BotonProductividad.setOnClickListener {
            gotoProductividad()
        }

        val botonHome = findViewById<ImageView>(R.id.btn_home)
        botonHome.setOnClickListener {
            // Código para abrir la pantalla de Flowmodoro
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }


    }

    private fun gotoConcentracion(){
        val i = Intent(this,ConcentracionActivity::class.java)
        startActivity(i)
    }
    private fun gotoProductividad(){
        val i = Intent(this,ProductividadActivity::class.java)
        startActivity(i)
    }

}