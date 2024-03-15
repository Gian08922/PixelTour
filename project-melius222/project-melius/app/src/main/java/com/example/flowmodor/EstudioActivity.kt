package com.example.flowmodor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class EstudioActivity : AppCompatActivity() {

    // Variable para seleccionar el estado para el método a mostrar
    var indiceImagenActual = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.flowmodor.R.layout.activity_estudio)

        // Obtener referencia al TextView para mostrar la fecha actual
        val txtDate = findViewById<TextView>(R.id.textView_dateEstudio)

        // Obtener la fecha actual y formatearla como "dd/MM/yyyy"
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        // Mostrar la fecha formateada en el TextView
        txtDate.text = formattedDate


        // Boton Home
        val botonHome = findViewById<ImageView>(R.id.btn_homeEstudio)

        // Obtener Botones Flechitas
        val botonDerecha = findViewById<ImageView>(R.id.flechaDer)
        val botonIzquierda = findViewById<ImageView>(R.id.flechaIzq)

        // Obtener Imagen Metodo e imagen Abajo
        val imMetodo = findViewById<ImageView>(R.id.botonSel)
        val imAbajo = findViewById<ImageView>(R.id.imgSel)

        // Obtener Titulo Metodo y texto metodo
        val tituloMetodo = findViewById<TextView>(R.id.tituloSel)
        val textoMetodo = findViewById<ImageView>(R.id.textoSeleccionado)

        imMetodo.setOnClickListener {
            // Código para abrir la pantalla de Flowmodoro
            val intent = Intent(this, FlowmodoroActivity::class.java)
            startActivity(intent)
        }

        botonHome.setOnClickListener {
            // Código para abrir la pantalla de Flowmodoro
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

        // Funcion para actualizar la imagen, se le llama al darle a las flechitas
        fun actualizarImagen() {
            when (indiceImagenActual) {
                0 -> {
                    // Cambiar la imagen del ImageView con ID imMetodo (El boton principal)
                    imMetodo.setImageResource(R.drawable.flowmodoro_image)
                    imMetodo.setOnClickListener {
                        // Código para abrir la pantalla de Flowmodoro
                        val intent = Intent(this, FlowmodoroActivity::class.java)
                        startActivity(intent)
                    }
                    // Cambiar la imagen del ImageView con ID imAbajo (Los 3 tomaticos de debajo)
                    imAbajo.setImageResource(R.drawable.metodo1sel)
                    tituloMetodo.text = "FLOWMODORO"
                    textoMetodo.setImageResource(R.drawable.ii_textoflowmodoro)
                }
                1 -> {
                    imMetodo.setImageResource(R.drawable.pomodoro_image)
                    imMetodo.setOnClickListener {
                        // Código para abrir la pantalla de Pomodoro
                        val intent = Intent(this, PomodoroActivity::class.java)
                        startActivity(intent)
                    }
                    imAbajo.setImageResource(R.drawable.metodo2sel)
                    tituloMetodo.text = "POMODORO"
                    textoMetodo.setImageResource(R.drawable.ii_textopomodoro)
                }
                2 -> {
                    imMetodo.setImageResource(R.drawable.high_tasking_image)
                    imMetodo.setOnClickListener {
                        // Código para abrir la pantalla de High Tasking
                        val intent = Intent(this, HighTaskingActivity::class.java)
                        startActivity(intent)
                    }
                    imAbajo.setImageResource(R.drawable.metodo3sel)
                    tituloMetodo.text = "HIGH TASKING"
                    textoMetodo.setImageResource(R.drawable.ii_textohightasking)
                }
            }
        }

        // Agregar listener a la flecha derecha
        botonDerecha.setOnClickListener {
            // Incrementar el índice de la imagen actual
            indiceImagenActual++
            // Si el índice se sale del rango, volver al inicio
            if (indiceImagenActual >= 3) {
                indiceImagenActual = 0
            }
            actualizarImagen()
        }

        // Agregar listener a la flecha izquierda
        botonIzquierda.setOnClickListener {
            // Decrementar el índice de la imagen actual
            indiceImagenActual--
            // Si el índice se sale del rango, volver al final
            if (indiceImagenActual < 0) {
                indiceImagenActual = 2
            }
            actualizarImagen()

        }





    }






}