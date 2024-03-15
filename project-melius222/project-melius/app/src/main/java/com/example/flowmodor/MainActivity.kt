package com.example.flowmodor

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencia al botón de ajustes
        val btnSettings = findViewById<ImageView>(R.id.btn_settings)
        val btnEstudio = findViewById<ImageView>(R.id.imageView_study_methods)
        val btnEstadisticas = findViewById<ImageView>(R.id.imageView_statistics)

        val puntosTexto = findViewById<TextView>(R.id.textView4)
        val rangoTexto = findViewById<TextView>(R.id.textView6)

        // Manejar los clics en el botón de ajustes
        btnSettings.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val intent = Intent(this, AjustesActivity::class.java)
            startActivity(intent)
        }
        // Manejar los clics en el botón de estudio
        btnEstudio.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val intent = Intent(this, EstudioActivity::class.java)
            startActivity(intent)
        }
        // Manejar los clics en el botón de estadisticas
        btnEstadisticas.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val intent = Intent(this, EstadisticasActivity::class.java)
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
        val documentReference = db.collection("estudio").document(userEmail!!).collection("dias")
        val query = documentReference
        val tasks = mutableListOf<Task<QuerySnapshot>>()
        tasks.add(query.get())

        var sumatorioFlowmodoro = 0
        var sumatorioPomodoro = 0
        var sumatorioHighTasking = 0

        Tasks.whenAllSuccess<QuerySnapshot>(tasks)
            .addOnSuccessListener { querySnapshots ->
                for (querySnapshot in querySnapshots) {
                    var g = querySnapshot.documents
                    for (document in querySnapshot.documents) {
                        val fechaDocStr = document.getString("formattedDate")
                        val fechaDoc = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaDocStr)
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DATE, -30)
                        val fechaLimite = calendar.time
                        if (fechaDoc.after(fechaLimite)) {
                            val metodo = document.getLong("metodo")?.toInt() ?: -1 // -1 si no hay valor
                            val numRondas = document.getLong("numRondas")?.toInt() ?: -1 // -1 si no hay valor
                            when (metodo) {
                                0 -> sumatorioFlowmodoro += numRondas * 10
                                1 -> sumatorioPomodoro += numRondas * 10
                                2 -> sumatorioHighTasking += numRondas * 30
                            }
                        }


                    }
                }
                var sumTotal = sumatorioFlowmodoro + sumatorioHighTasking + sumatorioPomodoro

                puntosTexto.text = sumTotal.toString()

                if(sumTotal < 450){
                    rangoTexto.text = "Semilla"
                }
                else if(sumTotal < 600){
                    rangoTexto.text = "Brote"
                }
                else if(sumTotal < 750){
                    rangoTexto.text = "Cherry"
                }
                else if(sumTotal < 900){
                    rangoTexto.text = "Tomate"
                }
                else if(sumTotal < 1050){
                    rangoTexto.text = "Super Tomate"
                }
                else if(sumTotal < 1300){
                    rangoTexto.text = "Tomate Maestro"
                }
                else{
                    rangoTexto.text = "Flowmate"
                }
            }
            .addOnFailureListener { exception ->
                // Aquí manejas la excepción si algo falla al leer los datos

            }


    }
    // Flowmate el que lo lea

}
