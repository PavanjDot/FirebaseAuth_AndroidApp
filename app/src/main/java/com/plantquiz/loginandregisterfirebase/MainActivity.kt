package com.plantquiz.loginandregisterfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    val RC_SIGN_IN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        register_button_register.setOnClickListener{
            performRegister()
        }

        already_have_account_textview.setOnClickListener {

            Log.d("MainActivity", "Try to show login Activity")

            //lanch login Activity

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



    }
    private fun signInGoogle(){

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){

        try{
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            updateUI (account)

        }catch(e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }



    }

    private fun updateUI(account: GoogleSignInAccount){

        val dispTxt = findViewById<View>(R.id.dispTxt) as TextView
        dispTxt.text = account.displayName

    }


    private fun performRegister(){

        val email =  email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()
        //val username = username_edittext_registration.text.toString()

        if(email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this, "Please enter text in Email & Password", Toast.LENGTH_SHORT).show()
           // return@addOnCompleteListner

        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password is:  $password")

        //Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if(it.isSuccessful)
                {Log.d("Main", "Successfully created user with uid: " +
                        "${it.result?.user?.uid}")
                    Log.d("Main", "createUserWithEmail:success")

                }

                else{
                    Log.w("Main", "createUserWithEmail:failure", it.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }

            }


    }



}
