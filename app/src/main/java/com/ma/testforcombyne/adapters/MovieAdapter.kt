package com.ma.testforcombyne.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ma.testforcombyne.GetMoviesQuery
import com.ma.testforcombyne.R
import com.ma.testforcombyne.global.convertDateFormat

class MovieAdapter : ListAdapter<GetMoviesQuery.Node, ViewHolder>(MovieDiffUtil()) {

	var onItemClicked: ((String) -> Unit)? = null
	var onDeleteClicked: ((String) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder.create(parent)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position), onItemClicked, onDeleteClicked)
	}
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

	private val cardHolder: CardView = itemView.findViewById(R.id.cardHolder)
	private val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
	private val txtReleaseDate: TextView = itemView.findViewById(R.id.txtReleaseDate)
	private val txtSeasons: TextView = itemView.findViewById(R.id.txtSeasons)
	private val btnDelete: AppCompatImageButton = itemView.findViewById(R.id.btnDelete)

	fun bind(movie: GetMoviesQuery.Node, onItemClicked: ((String) -> Unit)?, onDeleteClicked: ((String) -> Unit)?) {
		txtTitle.text = movie.title
		txtReleaseDate.text = convertDateFormat(movie.releaseDate)
		txtSeasons.text = movie.seasons?.toInt().toString()
		cardHolder.setOnClickListener {
			onItemClicked?.invoke(movie.id)
		}
		btnDelete.setOnClickListener {
			onDeleteClicked?.invoke(movie.id)
		}
	}

	companion object {
		fun create(parent: ViewGroup): ViewHolder {
			val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
			return ViewHolder(view)
		}
	}
}

class MovieDiffUtil : DiffUtil.ItemCallback<GetMoviesQuery.Node>() {
	override fun areItemsTheSame(oldItem: GetMoviesQuery.Node, newItem: GetMoviesQuery.Node): Boolean {
		return oldItem.id == newItem.id
	}
	override fun areContentsTheSame(oldItem: GetMoviesQuery.Node, newItem: GetMoviesQuery.Node): Boolean {
		return oldItem == newItem
	}
}