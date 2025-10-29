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

class EditStrokaAdapter(
    private var mangaList: List<Manga>,
    private val onItemClick: (Manga) -> Unit
) : RecyclerView.Adapter<EditStrokaAdapter.MangaStrokaViewHolder>() {

    inner class MangaStrokaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.manga_img_stolb)
        val title: TextView = itemView.findViewById(R.id.manga_title_stolb)
        val author: TextView = itemView.findViewById(R.id.author_manga_stolb)
        val chapters: TextView = itemView.findViewById(R.id.all_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaStrokaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stolbec_mang, parent, false)
        return MangaStrokaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MangaStrokaViewHolder, position: Int) {
        val manga = mangaList[position]

        holder.title.text = manga.name
        holder.author.text = manga.authors
        holder.chapters.text = "${manga.chapters ?: 0}"

        try {
            Glide.with(holder.itemView.context)
                .load(manga.images)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image)
        } catch (e: Exception) {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.itemView.setOnClickListener {
            onItemClick(manga)
        }
    }

    override fun getItemCount(): Int = mangaList.size

    fun setData(newMangaList: List<Manga>) {
        mangaList = newMangaList
        notifyDataSetChanged()
    }
}