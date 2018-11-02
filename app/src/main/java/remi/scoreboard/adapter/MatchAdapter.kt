package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.data.Match
import remi.scoreboard.databinding.ItemCardMatchBinding

class MatchAdapter : ListAdapter<Match, MatchAdapter.ViewHolder>(MatchDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = getItem(position)
        holder.apply {
            bind(createOnClickListener(match), match)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardMatchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    private fun createOnClickListener(
        match: Match
    ): View.OnClickListener {
        return View.OnClickListener {
        }
    }

    inner class ViewHolder(private val binding: ItemCardMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Match) {
            binding.apply {
                clickListener = listener
                match = item
                playerList = item.scorePlayerList.sortedByDescending { it.score }
                executePendingBindings()
            }
        }
    }
}