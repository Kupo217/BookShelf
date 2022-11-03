package com.example.bookshelf

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<Button>(R.id.btnAdd)

        setupListOfDataRecyclerView()

        btnAdd.setOnClickListener {
            addBook()
        }
    }

    private fun addBook() {
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etAuthor = findViewById<EditText>(R.id.etAuthor)

        val title = etTitle.text.toString()
        val author = etAuthor.text.toString()

        val dbHelper = DBHelper(this)
        if (title.isNotEmpty() && author.isNotEmpty()) {
            val status = dbHelper.addBook(Book(0, title, author))
            if (status > -1) {
                Toast.makeText(this, "Book saved", Toast.LENGTH_LONG).show()
                etTitle.text.clear()
                etAuthor.text.clear()
                setupListOfDataRecyclerView()
            }
        } else {
            Toast.makeText(this, "Title or author cannot be blank !", Toast.LENGTH_LONG).show()
        }
    }

    private fun getBooksList(): ArrayList<Book> {
        val dbHelper = DBHelper(this)

        return dbHelper.viewBooks()
    }


    private fun setupListOfDataRecyclerView() {
        if (getBooksList().size > 0) {
            val rvBookList = findViewById<RecyclerView>(R.id.rvBookList)
            rvBookList.visibility = View.VISIBLE

            rvBookList.layoutManager = LinearLayoutManager(this)
            val bookAdapter = BookAdapter(getBooksList(), this)
            rvBookList.adapter = bookAdapter
        } else {
            val rvBookList = findViewById<RecyclerView>(R.id.rvBookList)
            rvBookList.visibility = View.GONE
        }
    }


    fun updateRecordDialog(book: Book) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        updateDialog.setContentView(R.layout.dialog_update)
        updateDialog.findViewById<EditText>(R.id.etUpdateTitle).setText(book.title)
        updateDialog.findViewById<EditText>(R.id.etUpdateAuthor).setText(book.author)

        updateDialog.findViewById<TextView>(R.id.tvUpdate).setOnClickListener {
            val title = updateDialog.findViewById<EditText>(R.id.etUpdateTitle).text.toString()
            val author = updateDialog.findViewById<EditText>(R.id.etUpdateAuthor).text.toString()

            val dbHelper = DBHelper(this)

            if (title.isNotEmpty() && author.isNotEmpty())
            {
                val status =
                    dbHelper.updateBook(Book(book.id, title, author))
                if (status > -1)
                {
                    Toast.makeText(this, "Record Updated", Toast.LENGTH_LONG).show()

                    setupListOfDataRecyclerView()

                    updateDialog.dismiss()
                }
            }else
            {
                Toast.makeText(this, "Title or author cannot be blank !", Toast.LENGTH_LONG).show()
            }
        }

        updateDialog.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            updateDialog.dismiss()
        }
    }

    fun deleteRecordAlertDialog(book: Book)
    {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Record")

        builder.setMessage("Are you sure you want delete ${book.id}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, i ->
            val dbHelper = DBHelper(this)

            val status = dbHelper.deleteBook(Book(book.id, "", ""))
            if (status > -1) {
                Toast.makeText(this, "Deleted record", Toast.LENGTH_LONG).show()
                setupListOfDataRecyclerView()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, i ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }




}