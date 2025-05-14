package com.example.practic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practic.R
import com.example.practic.data.Manga

class PopularityAdapter(private var mangaList: List<Manga>) : RecyclerView.Adapter<PopularityAdapter.MangaViewHolder>() {

    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImage: ImageView = itemView.findViewById(R.id.manga_card_image) // Make sure this ID exists in your stroka_mang.xml
        val mangaTitle: TextView = itemView.findViewById(R.id.manga_title) // Make sure this ID exists in your stroka_mang.xml
        val mangaAuthor: TextView = itemView.findViewById(R.id.manga_author) // Make sure this ID exists in your stroka_mang.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stroka_mang, parent, false)
        return MangaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        val currentItem = mangaList[position]

        holder.mangaTitle.text = currentItem.name
        holder.mangaAuthor.text = "${currentItem.authors}"

        // Load image using Glide (or Picasso)
        Glide.with(holder.itemView.context)
            .load(currentItem.images) // Assuming images is a URL or resource
            .placeholder(R.drawable.ic_launcher_background) // Replace with your placeholder
            .error(R.drawable.ic_launcher_foreground) // Replace with your error image
            .into(holder.mangaImage)
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

    // Method to update the data in the adapter
    fun setData(newMangaList: List<Manga>) {
        mangaList = newMangaList
        notifyDataSetChanged()
    }
}