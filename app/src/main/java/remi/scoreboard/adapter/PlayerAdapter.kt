package remi.scoreboard.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.R
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.databinding.ItemCardPlayerBinding

class PlayerAdapter :
    ListAdapter<PlayerScore, PlayerAdapter.ViewHolder>(PlayerScoreDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playerScore: PlayerScore = getItem(position)
        holder.apply {
            itemView.findViewById<TextView>(R.id.player_num).text = "Player $position"
            bind(playerScore, View.OnClickListener {
                Log.d("PLAY", "click on player ${playerScore.user}")
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardPlayerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCardPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlayerScore, listener: View.OnClickListener) {
            binding.apply {
                clickListener = listener
                playerScore = item
                executePendingBindings()
            }
        }
    }
}