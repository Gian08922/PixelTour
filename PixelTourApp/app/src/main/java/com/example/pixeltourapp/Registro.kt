package com.example.pixeltourapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class Registro : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var termsCheckbox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)



        //Inicializamos la autenticacion con firebase
        auth = Firebase.auth

        val myTextView = findViewById<TextView>(R.id.TerminosCondiciones)
        myTextView.setPaintFlags(myTextView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        //Volver al Login
        val GoLogin = findViewById<TextView>(R.id.go_to_login)
        GoLogin.setOnClickListener {
            goToLogin()
        }

        //Ir a los términos
        val GoTerms = findViewById<TextView>(R.id.TerminosCondiciones)
        GoTerms.setOnClickListener {
            gotoTerms()
        }

        //Terminos y Condiciones
        termsCheckbox = findViewById(R.id.terms_checkbox)
        termsCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val registerButton: Button = findViewById(R.id.boton_resgistro)
                registerButton.setOnClickListener {
                    Registro()
                }
            }
        }

        val textView = findViewById<TextView>(R.id.TerminosCondiciones)
        val texto = "Acepto los Términos y Condiciones"
        val textoNegrita = SpannableString(texto)
        textoNegrita.setSpan(StyleSpan(Typeface.BOLD), 11, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = textoNegrita

        val textView2 = findViewById<TextView>(R.id.go_to_login)
        val texto2 = "¿Ya tiene una cuenta? Inicie sesión"
        val textoNegrita2 = SpannableString(texto)
        textoNegrita2.setSpan(StyleSpan(Typeface.BOLD), 21, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView2.text = textoNegrita2


    }

    //Función para ir al login
    private fun goToLogin(){
        val i = Intent(this,Login::class.java)
        startActivity(i)
    }

    //Funcion para ir a los terminos
    private fun gotoTerms(){
        val i = Intent(this,Terminos::class.java)
        startActivity(i)
    }

    //Función que registra a un usuario mediante el correo y la contraseña que ha introducido
    //La función valida que los campos introducidos son los correctos y que no estan vacios
    private fun Registro(){
        val username = findViewById<EditText>(R.id.nombre_usuario)
        val email = findViewById<EditText>(R.id.correo_usuario)
        val password = findViewById<EditText>(R.id.contraseña_usuario)

        if(username.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this,"Rellene los campos por favor",Toast.LENGTH_SHORT).show()
            return
        }


        val inputUsername = username.text.toString()
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, let move to the mainActivity
                    var dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    var dbRegister = FirebaseFirestore.getInstance()
                    dbRegister.collection("users").document(inputEmail).set(hashMapOf(
                        "userNickname" to inputUsername,
                        "user" to inputEmail,
                        "dateRegister" to dateRegister,
                    ))
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    Toast.makeText(baseContext, "Se ha registrado con exito", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Error al registrarte", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error,  ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }
    }


}