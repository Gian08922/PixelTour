package com.example.pixeltourapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarContra : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val RecuperarContrasenaButton: Button = findViewById(R.id.boton_recuperar_contrasena)
        RecuperarContrasenaButton.setOnClickListener {
            RecuperarContrasena()
        }
    }

    private fun RecuperarContrasena() {
        val email: EditText = findViewById(R.id.correo_recuperar_contrasena)
        val inputEmail = email.text.toString()
        if (!TextUtils.isEmpty(inputEmail)) {
            auth.sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Correo Enviado a $inputEmail", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Error,  ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
                }
        }
    }
}