package remi.scoreboard.adapter

import androidx.recyclerview.widget.DiffUtil
import remi.scoreboard.data.Game
import remi.scoreboard.data.User

class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Game, newItem: Game) = oldItem == newItem
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
}