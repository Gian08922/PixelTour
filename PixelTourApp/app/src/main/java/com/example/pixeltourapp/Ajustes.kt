package com.example.pixeltourapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class Ajustes : AppCompatActivity() {

    private var sonidoBBDD: Boolean = false
    private var duracionDescansosBBDD: Double = 0.0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)



        // Obtener referencia al botón de ajustes
        val btnSettings = findViewById<ImageView>(R.id.CerrarAjustes)
        // Manejar los clics en el botón de ajustes
        btnSettings.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Obtener referencia al TextView para mostrar la fecha actual
        val txtDate = findViewById<TextView>(R.id.textView_dateAjustes)

        // Obtener la fecha actual y formatearla como "dd/MM/yyyy"
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        // Mostrar la fecha formateada en el TextView
        txtDate.text = formattedDate

        //Cerrar Sesion
        //Boton de inicio de sesion
        val cerrarsesionButton: ImageButton = findViewById(R.id.cerrarSesionBoton)
        cerrarsesionButton.setOnClickListener{
            CerrarSesion()
        }
    }

    private fun CerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        Toast.makeText(baseContext, "Ha cerrado sesión", Toast.LENGTH_SHORT).show()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}