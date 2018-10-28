package remi.scoreboard.adapter

import androidx.recyclerview.widget.DiffUtil
import remi.scoreboard.data.Game

class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game) = oldItem.gid == newItem.gid

    override fun areContentsTheSame(oldItem: Game, newItem: Game) = oldItem == newItem
}
