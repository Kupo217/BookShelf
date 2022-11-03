package com.example.bookshelf

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_TITLE + " TEXT," +
                KEY_AUTHOR + " TEXT" + ")")

        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addBook(book: Book): Long {
        val values = ContentValues()

        values.put(KEY_TITLE, book.title)
        values.put(KEY_AUTHOR, book.author)

        val db = this.writableDatabase
        val success = db.insertOrThrow(TABLE_NAME, null, values)

        db.close()
        return success
    }

    fun viewBooks(): ArrayList<Book>
    {
        val bookList = ArrayList<Book>()

        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var title: String
        var author: String
        var id: Int

        if (cursor.moveToFirst())
        {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE))
                author = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AUTHOR))

                val book = Book(id, title, author)
                bookList.add(book)
            }while (cursor.moveToNext())
        }

        return bookList
    }

    fun updateBook(book: Book): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_TITLE, book.title)
        values.put(KEY_AUTHOR, book.author)

        val success = db.update(TABLE_NAME, values, "$KEY_ID = ${book.id}" , null)

        db.close()

        return success
    }

    fun deleteBook(book: Book): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_ID, book.id)

        val success = db.delete(TABLE_NAME, "$KEY_ID = ${book.id}", null)
        db.close()

        return success
    }

    companion object {
        val DATABASE_VERSION = 1

        val DATABASE_NAME = "FAVORITE-BOOK-SHELF"

        val TABLE_NAME = "favorite_books"

        val KEY_ID = "id"
        val KEY_TITLE = "title"
        val KEY_AUTHOR = "author"
    }
}