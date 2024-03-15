package com.example.flowmodor

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class CuestionarioActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuestionario)
        var notaProdu = 1
        var notaConc = 1
        var notaEstado = 1

        val extras = intent.extras
        val numRondas = extras?.getInt("numRondas", 0) ?: 0
        val metodo = extras?.getInt("metodo", 0) ?: 0


        val rgProdu: RadioGroup = findViewById(R.id.rgProdu)

        rgProdu.setOnCheckedChangeListener {group, checkedId ->
            when (checkedId) {
                R.id.prod1 ->
                    notaProdu = 1
                R.id.prod2 ->
                    notaProdu = 2
                R.id.prod3 ->
                    notaProdu = 3
                R.id.prod4 ->
                    notaProdu = 4
                R.id.prod5 ->
                    notaProdu = 5
            }
        }

        val rgConc: RadioGroup = findViewById(R.id.rgConc)

        rgConc.setOnCheckedChangeListener {group, checkedId ->
            when (checkedId) {
                R.id.conc1 -> notaConc = 1
                R.id.conc2 -> notaConc = 2
                R.id.conc3 -> notaConc = 3
                R.id.conc4 -> notaConc = 4
                R.id.conc5 -> notaConc = 5
            }
        }

        val rgEstado: RadioGroup = findViewById(R.id.rgEstado)

        rgEstado.setOnCheckedChangeListener {group, checkedId ->
            when (checkedId) {
                R.id.est1 -> notaEstado = 1
                R.id.est2 -> notaEstado = 2
                R.id.est3 -> notaEstado = 3
                R.id.est4 -> notaEstado = 4
            }
        }


        val botSalir = findViewById<ImageView>(R.id.finalizarCuestionarioBoton)
        botSalir.setOnClickListener {
            //Obtenemos la fecha:
            // Obtener la fecha actual y formatearla como "dd/MM/yyyy"
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)

            val currentDate2 = Date()
            val dateFormat2 = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val formattedDate2 = dateFormat2.format(currentDate2)
            // Código para salir al main
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)

            val db = FirebaseFirestore.getInstance()
            val userEmail = FirebaseAuth.getInstance().currentUser!!.email
            val documentReference = db.collection("estudio").document(userEmail!!).collection("dias")
            val fechaActual = formattedDate2.toString() // obtener la fecha actual en formato YYYY-MM-DD
            val datos = hashMapOf(
                "notaProdu" to notaProdu,
                "notaConc" to notaConc,
                "notaEstado" to notaEstado,
                "numRondas" to numRondas,
                "metodo" to metodo,
                "formattedDate" to formattedDate
            )
            documentReference.document(fechaActual).set(datos)
        }



    }
}