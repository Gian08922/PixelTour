package com.example.pixeltourapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import java.text.SimpleDateFormat
import java.util.Calendar
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Date
import java.util.Locale
import com.google.android.gms.maps.GoogleMap;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencia al botón de ajustes
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        val btnMapa = findViewById<ImageView>(R.id.botonMapa)
        val btnBuscador = findViewById<ImageView>(R.id.botonBuscador)


        // Manejar los clics en el botón de ajustes
        btnSettings.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val intent = Intent(this, Ajustes::class.java)
            startActivity(intent)
        }

        // Manejar los clics en el botón de estadisticas
        btnMapa.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val intent = Intent(this, Mapa::class.java)
            startActivity(intent)
        }
        btnBuscador.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val intent = Intent(this, Buscador::class.java)
            startActivity(intent)
        }

        // Obtener referencia al TextView para mostrar la fecha actual
        val txtDate = findViewById<TextView>(R.id.textView_date)

        // Obtener la fecha actual y formatearla como "dd/MM/yyyy"
        val currentDateT = Date()
        val dateFormatT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDateT = dateFormatT.format(currentDateT)

        // Mostrar la fecha formateada en el TextView
        txtDate.text = formattedDateT

        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val fechaFin = dateFormat.format(currentDate) // Fecha actual en formato dd.MM.yyyy
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -30) // Restar 30 días
        val fechaInicio = dateFormat.format(calendar.time) // Fecha de inicio en formato dd.MM.yyyy

        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser!!.email
        //val documentReference = db.collection("estudio").document(userEmail!!).collection("dias")
        //val query = documentReference
        //val tasks = mutableListOf<Task<QuerySnapshot>>()
        //tasks.add(query.get())



    }
}