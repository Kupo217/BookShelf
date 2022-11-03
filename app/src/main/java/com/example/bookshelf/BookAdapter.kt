package com.example.bookshelf

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(val bookList: List<Book>, val context: Context) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val ivEdit: ImageView = itemView.findViewById(R.id.btnEdit)
        val ivDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]

        holder.tvTitle.text = book.title
        holder.tvAuthor.text = book.author

        holder.ivEdit.setOnClickListener {
            if (context is MainActivity)
            {
                context.updateRecordDialog(book)
            }
        }

        holder.ivDelete.setOnClickListener {
            if (context is MainActivity){
                context.deleteRecordAlertDialog(book)
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}