package com.example.zachet

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var number: EditText
    private lateinit var factText: TextView
    private lateinit var button: Button
    private lateinit var vivodlist: Button
    private lateinit var pref: SharedPreferences
    private val APP_PREFERENCES = "app_pref"
    private val PREF_FACT = "fact"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        number = findViewById(R.id.number)
        factText = findViewById(R.id.fact)
        button = findViewById(R.id.button)
        vivodlist = findViewById(R.id.vivodinlist)
        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        button.setOnClickListener {
            val num = number.text.toString().trim()
            if (num.isNotEmpty() && num.matches(Regex("[0-9]+"))) {
                getResult(num)
                vivodlist.isEnabled = true
            } else {
                showToast("Введите только цифры")
            }
        }

        vivodlist.setOnClickListener {
            val intent = Intent(this, PrintScreen::class.java)
            startActivity(intent)
        }
    }

    private fun getResult(num: String) {
        val url = "http://numbersapi.com/$num/trivia?json"
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
                val obj = JSONObject(response)
                val numFact = obj.optString("text", "Факт не найден")
                factText.text = numFact
                saveFactToSharedPreferences(factText.text.toString())
            },
            {
                factText.text = "Ошибка при получении факта"
            })
        queue.add(stringRequest)
    }

    private fun saveFactToSharedPreferences(fact: String) {
        val prefEd = pref.edit()
        prefEd.putString(PREF_FACT, fact)
        prefEd.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
