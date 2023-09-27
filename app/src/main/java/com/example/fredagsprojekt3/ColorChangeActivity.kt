package com.example.fredagsprojekt3

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ColorChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_change)

        val changeColorButton: Button = findViewById(R.id.colorBtn)
        val loggedInMenuButton: Button = findViewById(R.id.backBtn)


        changeColorButton.setOnClickListener {
            val randomColor = generateRandomColor()

            findViewById<View>(android.R.id.content).setBackgroundColor(randomColor)
        }

        loggedInMenuButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun generateRandomColor(): Int {
        val red = (0..255).random()
        val green = (0..255).random()
        val blue = (0..255).random()
        return Color.rgb(red, green, blue)
    }
}
