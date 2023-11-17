package com.example.zachet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PrintScreen : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var deleteButton: Button
    private lateinit var addButton: Button
    private lateinit var factList: MutableList<String>
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_screen)

        listView = findViewById(R.id.listView)
        deleteButton = findViewById(R.id.deleteButton)
        addButton = findViewById(R.id.addButton)
        factList = mutableListOf()
        sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)

        val fact: String? = sharedPreferences.getString("fact", null)

        if (!fact.isNullOrBlank()) {
            factList.add(fact)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, factList)
        listView.adapter = adapter

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(adapter)
        }

        addButton.setOnClickListener {
            showAddFactDialog(adapter)
        }
    }

    private fun showDeleteConfirmationDialog(adapter: ArrayAdapter<String>) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Вопрос")
        alertDialogBuilder.setMessage("Вы хотите удалить все факты?")
        alertDialogBuilder.setPositiveButton("Да") { _, _ ->
            factList.clear()
            sharedPreferences.edit().remove("fact").apply()
            adapter.notifyDataSetChanged()
            showToast("Факты удалены")
        }
        alertDialogBuilder.setNegativeButton("Нет") { _, _ ->
            showToast("Удаление отменено")
        }
        alertDialogBuilder.show()
    }

    private fun showAddFactDialog(adapter: ArrayAdapter<String>) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.history_activity, null)
        val etFact = dialogView.findViewById<EditText>(R.id.etFact)
        val existingFact = etFact.text.toString()
        if (!existingFact.isEmpty()) {
            etFact.setText(existingFact)
        }

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(if (existingFact.isEmpty()) "Добавить факт" else "Редактировать факт")

        dialogBuilder.setPositiveButton(if (existingFact.isEmpty()) "Добавить" else "Сохранить изменения") { _, _ ->
            val fact = etFact.text.toString()
            if (existingFact.isEmpty()) {
                addFact(fact, adapter)
            } else {
                updateFact(existingFact, fact, adapter)
            }
        }

        dialogBuilder.setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }

        val dialog = dialogBuilder.create()


        etFact.setOnClickListener {
            dialog.show()
        }

        dialog.show()
    }

    private fun addFact(newFact: String, adapter: ArrayAdapter<String>) {
        if (newFact.isNotEmpty()) {
            factList.add(newFact)
            adapter.notifyDataSetChanged()
            sharedPreferences.edit().putString("fact", newFact).apply()
        } else {
            showToast("Заполните поля")
        }
    }

    private fun updateFact(oldFact: String, newFact: String, adapter: ArrayAdapter<String>) {
        if (newFact.isNotEmpty()) {
            val position = factList.indexOf(oldFact)
            if (position != -1) {
                factList[position] = newFact
                adapter.notifyDataSetChanged()
                showToast("Факт обновлен")
                sharedPreferences.edit().putString("fact", newFact).apply()
            } else {
                showToast("Факт не найден")
            }
        } else {
            showToast("Заполните поля")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
