package com.example.flowmodor

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class AjustesActivity : AppCompatActivity() {

    private var sonidoBBDD: Boolean = false
    private var duracionDescansosBBDD: Double = 0.0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        val rgDescansos: RadioGroup = findViewById(R.id.rgDescansos)

        val rbCortos = findViewById<RadioButton>(R.id.rbCortos)
        val rbMedios = findViewById<RadioButton>(R.id.rbMedios)
        val rbLargos = findViewById<RadioButton>(R.id.rbLargos)

        // Ajustes del sonido, versión imágenes

        val rgVolumen: RadioGroup = findViewById(R.id.rgVolumen)

        val rbSilencio = findViewById<RadioButton>(R.id.rbSilencio)
        val rbSonido = findViewById<RadioButton>(R.id.rbSonido)


        val db = FirebaseFirestore.getInstance()


        val userEmail = FirebaseAuth.getInstance().currentUser!!.email
        val documentReference = db.collection("ajustes").document(userEmail!!)
        documentReference.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    duracionDescansosBBDD = document.getDouble("duracionDescansos") ?: 0.5
                    sonidoBBDD = document.getBoolean("sonido") ?: false
                    if(duracionDescansosBBDD == 0.2)
                    {
                        rgDescansos.clearCheck()
                        rbCortos.setButtonDrawable(R.drawable.bb_cortos_checked_small)
                        rbMedios.setButtonDrawable(R.drawable.bb_medios_unchecked_small)
                        rbLargos.setButtonDrawable(R.drawable.bb_largos_unchecked_small)
                        rgDescansos.check(rbCortos.id)
                    }
                    else if(duracionDescansosBBDD == 0.33)
                    {
                        rgDescansos.clearCheck()
                        rbCortos.setButtonDrawable(R.drawable.bb_cortos_unchecked_small)
                        rbMedios.setButtonDrawable(R.drawable.bb_medios_checked_small)
                        rbLargos.setButtonDrawable(R.drawable.bb_largos_unchecked_small)
                        rgDescansos.check(rbMedios.id)
                    }
                    else if(duracionDescansosBBDD == 0.5)
                    {
                        rgDescansos.clearCheck()
                        rbCortos.setButtonDrawable(R.drawable.bb_cortos_unchecked_small)
                        rbMedios.setButtonDrawable(R.drawable.bb_medios_unchecked_small)
                        rbLargos.setButtonDrawable(R.drawable.bb_largos_checked_small)
                        rgDescansos.check(rbLargos.id)
                    }

                    if(!sonidoBBDD)
                    {
                        rgVolumen.clearCheck()
                        rbSilencio.setButtonDrawable(R.drawable.bb_silencio_checked)
                        rbSonido.setButtonDrawable(R.drawable.bb_sonido_unchecked)
                        rgVolumen.check(rbSilencio.id)

                    }
                    else
                    {
                        rgVolumen.clearCheck()
                        rbSilencio.setButtonDrawable(R.drawable.bb_silencio_unchecked)
                        rbSonido.setButtonDrawable(R.drawable.bb_sonido_checked)
                        rgVolumen.check(rbSonido.id)
                    }
                } else {
                    Log.d(ContentValues.TAG, "No existe el documento de ajustes para este usuario")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error al obtener los datos de ajustes: $exception")
            }


        // Obtener referencia al botón de ajustes
        val btnSettings = findViewById<ImageView>(R.id.CerrarAjustes)
        var duracionDescansos = duracionDescansosBBDD //valores 0.2, 0.33, 0.5
        var volumen = sonidoBBDD
        // Manejar los clics en el botón de ajustes
        btnSettings.setOnClickListener {
            // Código para abrir la pantalla de ajustes
            val ajustes: MutableMap<String, Any> = HashMap()
            ajustes["duracionDescansos"] = duracionDescansos
            ajustes["sonido"] = volumen
            val userEmail = FirebaseAuth.getInstance().currentUser!!.email
            val documentReference = db.collection("ajustes").document(
                userEmail!!
            )
            documentReference.set(ajustes)

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

        // Ajustes de los descansos, versión imágenes



        rbCortos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbCortos.setButtonDrawable(R.drawable.bb_cortos_checked_small)
            } else {
                rbCortos.setButtonDrawable(R.drawable.bb_cortos_unchecked_small)
            }
        }

        rbMedios.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbMedios.setButtonDrawable(R.drawable.bb_medios_checked_small)
            } else {
                rbMedios.setButtonDrawable(R.drawable.bb_medios_unchecked_small)
            }
        }

        rbLargos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbLargos.setButtonDrawable(R.drawable.bb_largos_checked_small)
            } else {
                rbLargos.setButtonDrawable(R.drawable.bb_largos_unchecked_small)
            }
        }

        // Modificar descansos


        rgDescansos.setOnCheckedChangeListener {group, checkedId ->
            // Verifica qué botón de radio está seleccionado y establece el valor de "volumen" en consecuencia
            when (checkedId) {
                R.id.rbCortos -> duracionDescansos = 0.2
                R.id.rbMedios -> duracionDescansos = 0.33
                R.id.rbLargos -> duracionDescansos = 0.5
            }
        }



        rbSilencio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbSilencio.setButtonDrawable(R.drawable.bb_silencio_checked)
            } else {
                rbSilencio.setButtonDrawable(R.drawable.bb_silencio_unchecked)
            }
        }

        rbSonido.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rbSonido.setButtonDrawable(R.drawable.bb_sonido_checked)
            } else {
                rbSonido.setButtonDrawable(R.drawable.bb_sonido_unchecked)
            }
        }

        // Modificar volumen


        rgVolumen.setOnCheckedChangeListener {group, checkedId ->
        // Verifica qué botón de radio está seleccionado y establece el valor de "volumen" en consecuencia
        when (checkedId) {
            R.id.rbSonido -> volumen = true
            R.id.rbSilencio -> volumen = false
        }
        }


        //Cerrar Sesion
        //Boton de inicio de sesion
        val cerrarsesionButton: ImageButton = findViewById(R.id.cerrarSesionBoton)
        cerrarsesionButton.setOnClickListener{
            CerrarSesion()
        }
    }

    private fun CerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        Toast.makeText(baseContext, "Ha cerrado sesión", Toast.LENGTH_SHORT).show()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}