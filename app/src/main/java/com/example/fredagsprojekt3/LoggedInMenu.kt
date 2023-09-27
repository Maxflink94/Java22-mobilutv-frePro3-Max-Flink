package com.example.fredagsprojekt3

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class LoggedInMenu : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in_menu)

        val spinner: Spinner = findViewById(R.id.spinner)
        val spinnerArray = resources.getStringArray(R.array.spinnerArray)

        val defaultText = "Välj ett alternativ"
        val itemList = mutableListOf(defaultText)
        itemList.addAll(spinnerArray)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            when (parent.getItemAtPosition(pos).toString()) {
                "Tenor Gif API (fungerar ej)" -> {
                    val i = Intent(this@LoggedInMenu, SecApi::class.java)
                    startActivity(i)
                }

                "Sida med randomfärg knapp" ->{
                    val i = Intent(this@LoggedInMenu, ColorChangeActivity::class.java)
                    startActivity(i)
                }

                "Logga ut" -> {
                    FirebaseAuth.getInstance().signOut()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("356914826348-4tlq3vgk68gqppftb7fkbn70je6sii46.apps.googleusercontent.com")
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(this, gso)
                    googleSignInClient.signOut().addOnCompleteListener(this) {
                        val i = Intent(this@LoggedInMenu, MainActivity::class.java)
                        startActivity(i)
                        Toast.makeText(this@LoggedInMenu, "Utloggad", Toast.LENGTH_LONG).show()

                        finish()
                    }
                }
            }
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}

