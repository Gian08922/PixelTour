package com.example.flowmodor


import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase



class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //Mantener la sesión iniciada
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings

        //Función para coger el id y que cuando se haga click se llame a la función goToRegister
        val GoRegister = findViewById<TextView>(R.id.go_to_registro)
        GoRegister.setOnClickListener {
            goToRegister()
        }

        //Boton de inicio de sesion
        val loginButton: Button = findViewById(R.id.boton_inicio_sesion)
        loginButton.setOnClickListener{
            Login()
        }

        //Función para ir al Activity de recuperar contraseña
        val GoContrasena = findViewById<TextView>(R.id.go_to_recuperar)
        GoContrasena.setOnClickListener {
            goToRecuperarContrasena()
        }
        //Boton de google
        val googleButton: Button = findViewById(R.id.boton_google)
        googleButton.setOnClickListener{
            LoginGoogle()
        }

        // Configura Firebase
        FirebaseApp.initializeApp(this)

        // Configura Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val textView = findViewById<TextView>(R.id.go_to_registro)
        val texto = "¿No tienes cuenta? ¡Regístrate!"
        val textoNegrita = SpannableString(texto)
        textoNegrita.setSpan(StyleSpan(Typeface.BOLD), 18, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = textoNegrita
    }
    //Función para ir al registro
    private fun goToRegister(){
        val i = Intent(this,RegistroActivity::class.java)
        startActivity(i)
    }
    //Función para recuperar la contraseña
    private fun goToRecuperarContrasena(){
        val x = Intent(this,RecuperarContraseya::class.java)
        startActivity(x)
    }

    //Función que comprueba si un usuario existe y permite su inicio de sesion en la app
    private fun Login(){

        val email: EditText = findViewById(R.id.correo_login)
        val contraseya: EditText = findViewById(R.id.contraseña_login)

        if(email.text.isEmpty() || contraseya.text.isEmpty()){
            Toast.makeText(this,"Rellene los campos por favor", Toast.LENGTH_SHORT).show()
            return
        }

        val inputEmail = email.text.toString()
        val inputPassword = contraseya.text.toString()

        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // Sign in success, let move to the mainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    Toast.makeText(baseContext, "Ha Iniciado Sesión con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Error al Iniciar Sesión.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error,  ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }
    }


    private fun LoginGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Autenticación con Firebase Auth usando la cuenta de Google
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Error en la autenticación con Google
                Toast.makeText(baseContext, "Error al autenticar con google", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autenticación con éxito
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    Toast.makeText(baseContext, "Ha Iniciado Sesión con éxito", Toast.LENGTH_SHORT).show()

                } else {
                    // Error en la autenticación con Firebase Auth
                    Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
    }


}