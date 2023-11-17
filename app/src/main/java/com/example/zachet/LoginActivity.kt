package com.example.zachet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var logEditText: EditText
    private lateinit var backButton: Button
    private lateinit var passEditText: EditText
    private lateinit var logButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logEditText = findViewById(R.id.LoginEditText)
        passEditText = findViewById(R.id.PasswordEditText)
        logButton = findViewById(R.id.logButton)
        backButton = findViewById(R.id.BackButton)

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        logButton.setOnClickListener {
            val login = logEditText.text.toString()
            val password = passEditText.text.toString()

            if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val storedEmail = sharedPreferences.getString("login", login)
                val storedHashedPassword = sharedPreferences.getString("password", password)

                if (login == storedEmail && password == storedHashedPassword) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Неправильные данные", Toast.LENGTH_SHORT).show()
                }
            }
        }

        backButton.setOnClickListener{
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }
    }
}