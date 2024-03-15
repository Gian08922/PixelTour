package com.example.flowmodor

import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class PomodoroActivity : AppCompatActivity() {
    // Variables para el temporizador
    private var timer: CountDownTimer? = null
    private var timeRemaining: Long = 0
    private var tiempoRun: Long = 0
    private var restTimeRemaining: Long = 0
    private var isRunning = false
    private var isRestTime = false
    private var pausaWorkTime = false
    private var numRonda = 1
    private var estudioIniciado = 0

    // Variables para el tiempo de trabajo y descanso
    private var workTime: Long = 1500000 // 25 minutos en milisegundos
    private var restTime: Long = 300000  // 5 minutos en milisegundos

    // Variable para el sonido de alarma
    private lateinit var alarmSound: MediaPlayer

    // Vistas de la interfaz
    private lateinit var temporizador_texto: TextView
    private lateinit var BotInicioPausa: ImageView
    private lateinit var textoSelID: TextView
    private lateinit var botSalir: ImageView
    private lateinit var tituloCambio: TextView
    private lateinit var botHome: ImageView

    private var sonido: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser!!.email
        val documentReference = db.collection("ajustes").document(userEmail!!)
        documentReference.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    sonido = document.getBoolean("sonido") ?: false
                    // Aquí puedes utilizar los datos de ajustes como necesites
                } else {
                    Log.d(ContentValues.TAG, "No existe el documento de ajustes para este usuario")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error al obtener los datos de ajustes: $exception")
            }

        // Asignar vistas de la interfaz
        temporizador_texto = findViewById(R.id.temporizador_texto)
        BotInicioPausa = findViewById(R.id.BotInicioPausa)
        textoSelID = findViewById(R.id.textoSelID)
        botSalir = findViewById(R.id.botOtroMetodo)
        tituloCambio = findViewById(R.id.titulo)
        botHome = findViewById(R.id.btn_homeEstudio)

        // Inicializar el sonido de la alarma
        alarmSound = MediaPlayer.create(this, R.raw.alarm)

        // Agregar listener al botón de inicio/pausa
        BotInicioPausa.setOnClickListener {
            if (estudioIniciado == 0)
            {
                botSalir.visibility = View.INVISIBLE
            }
            estudioIniciado = 1
            if (!isRunning) {
                if (isRestTime) {
                    iniciarTemporizador(restTimeRemaining)
                } else {
                    if(!pausaWorkTime)
                    {
                        tituloCambio.text = "TRABAJO"
                        iniciarTemporizador(workTime)
                    }
                    else
                    {
                        iniciarTemporizador(timeRemaining)
                    }

                }
            } else {
                pausarTemporizador()
            }
        }

        botSalir.setOnClickListener{
            if (estudioIniciado == 0)
            {
                val intentEstudio = Intent(this, EstudioActivity::class.java)
                startActivity(intentEstudio)
            }
            else
            {
                if(numRonda > 1)
                {
                    val intentCuestionarioActivity = Intent(this, CuestionarioActivity::class.java)
                    intentCuestionarioActivity.putExtra("numRondas", numRonda)
                    intentCuestionarioActivity.putExtra("metodo", 1)
                    startActivity(intentCuestionarioActivity)
                }
                else
                {
                    val intentEstudio = Intent(this, EstudioActivity::class.java)
                    startActivity(intentEstudio)
                }

            }
        }

        botHome.setOnClickListener{
            val intentHome = Intent(this, MainActivity::class.java)
            startActivity(intentHome)
        }
    }


    private fun iniciarTemporizador(tiempo: Long) {
        timer = object : CountDownTimer(tiempo, 1000) {
            // Cada vez que acabe un timer
            override fun onFinish() {
                if(sonido != false)
                {
                    alarmSound.start()
                }
                // Si hemos acabado la ronda de descansito
                if (isRestTime) {
                    // Boton a play
                    BotInicioPausa.setImageResource(R.drawable.binicio)
                    // Ya no esta corriendo el tempo
                    isRunning = false
                    // Se reinicia el estado de rest
                    isRestTime = false
                    // Se cambia el estado de pausa work
                    pausaWorkTime = false
                    // Se actualizan las rondas
                    numRonda++
                    textoSelID.text = "Ronda $numRonda"
                    tituloCambio.text = "POMODORO"
                    botSalir.setImageResource(R.drawable.finalizar)
                    botSalir.visibility = View.VISIBLE
                } else {
                    // Si hemos acabado la ronda de estudio
                    isRestTime = true
                    tituloCambio.text = "DESCANSO"
                    iniciarTemporizador(restTime) //Iniciamos con restTime
                }
            }
            // Cada segundo que pasa
            override fun onTick(millisUntilFinished: Long) {
                // Si estamos dentro de ronda de descanso
                if (isRestTime) {
                    restTimeRemaining = millisUntilFinished
                }
                // Si estamos en ronda de estudio
                else {
                    // Guardamos el tiempo que queda
                    timeRemaining = millisUntilFinished
                }
                // Actualizamos reloj
                updateCountdownUI()
            }
        }.start()

        isRunning = true
        pausaWorkTime = true
        BotInicioPausa.setImageResource(R.drawable.bpausa)
    }

    private fun pausarTemporizador() {
        timer?.cancel()
        isRunning = false
        tiempoRun = timeRemaining
        BotInicioPausa.setImageResource(R.drawable.binicio)
    }

    private fun updateCountdownUI() {
        val tiempoRestante = if (isRestTime) restTimeRemaining else timeRemaining
        val minutos = TimeUnit.MILLISECONDS.toMinutes(tiempoRestante)
        val segundos = TimeUnit.MILLISECONDS.toSeconds(tiempoRestante) % 60
        temporizador_texto.text = String.format("%02d:%02d", minutos, segundos)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // Cancelar el temporizador al destruir la actividad
    }

    override fun onBackPressed() {
        if (isRunning) {
            AlertDialog.Builder(this)
                .setTitle("¿Seguro que quieres salir?")
                .setMessage("El temporizador se detendrá si sales de la aplicación.")
                .setPositiveButton("Salir") { _, _ -> super.onBackPressed() }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}
