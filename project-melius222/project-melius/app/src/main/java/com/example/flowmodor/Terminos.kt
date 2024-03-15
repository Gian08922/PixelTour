package com.example.flowmodor

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Terminos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminos)

        //Titulos
        val textView2 = findViewById<TextView>(R.id.TerminoyCondicionesText2Titulo)
        textView2.setTypeface(null, Typeface.BOLD)

        val textView3 = findViewById<TextView>(R.id.TerminoyCondicionesText3Titulo)
        textView3.setTypeface(null, Typeface.BOLD)

        val textView4= findViewById<TextView>(R.id.TerminoyCondicionesText4itulo)
        textView4.setTypeface(null, Typeface.BOLD)

        val textView5 = findViewById<TextView>(R.id.TerminoyCondicionesText5itulo)
        textView5.setTypeface(null, Typeface.BOLD)

        val textView6 = findViewById<TextView>(R.id.TerminoyCondicionesText6itulo)
        textView6.setTypeface(null, Typeface.BOLD)

        val textView7 = findViewById<TextView>(R.id.TerminoyCondicionesText7Titulo)
        textView7.setTypeface(null, Typeface.BOLD)

        val textView8 = findViewById<TextView>(R.id.TerminoyCondicionesText8Titulo)
        textView8.setTypeface(null, Typeface.BOLD)

        val textView9 = findViewById<TextView>(R.id.TerminoyCondicionesText9Titulo)
        textView9.setTypeface(null, Typeface.BOLD)

        //Ir a los Registro
        val GoRegistro = findViewById<TextView>(R.id.boton_Terminos_Condiciones)
        GoRegistro.setOnClickListener {
            gotoRegister()
        }
    }

    private fun gotoRegister(){
        val i = Intent(this,RegistroActivity::class.java)
        startActivity(i)
    }
}