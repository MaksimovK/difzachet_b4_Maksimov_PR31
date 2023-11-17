package com.example.zachet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegisterActivity : AppCompatActivity() {
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var backButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginEditText = findViewById(R.id.registerLoginEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        backButton = findViewById(R.id.BackButton)

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        registerButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
            } else if (login.length < 3 || password.length < 3) {
                Toast.makeText(this, "Логин и пароль должны содержать как минимум 4 и 4 символов соответственно.", Toast.LENGTH_SHORT).show()
            } else {
                val existingUser = getUserFromSharedPreferences(login)
                if (existingUser == null) {
                    saveUserToSharedPreferences(User(login, password))
                    Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()
                }
            }
        }
        backButton.setOnClickListener{
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserToSharedPreferences(user: User) {
        val userList = getUsersListFromSharedPreferences()
        userList.add(user)

        val gson = Gson()
        val jsonUsers = gson.toJson(userList)

        val editor = sharedPreferences.edit()
        editor.putString("users", jsonUsers)
        editor.apply()
    }

    private fun getUserFromSharedPreferences(login: String): User? {
        val userList = getUsersListFromSharedPreferences()
        return userList.find { it.login == login }
    }

    private fun getUsersListFromSharedPreferences(): MutableList<User> {
        val jsonUsers = sharedPreferences.getString("users", "")
        val gson = Gson()
        val type = object : TypeToken<MutableList<User>>() {}.type
        return gson.fromJson(jsonUsers, type) ?: mutableListOf()
    }
}