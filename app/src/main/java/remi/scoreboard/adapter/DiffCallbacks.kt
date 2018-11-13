package remi.scoreboard.adapter

import androidx.recyclerview.widget.DiffUtil
import remi.scoreboard.data.*

class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Game, newItem: Game) = oldItem == newItem
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
}

class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Player, newItem: Player) = oldItem == newItem
}

class PlayerScoreDiffCallback : DiffUtil.ItemCallback<PlayerScore>() {
    override fun areItemsTheSame(oldItem: PlayerScore, newItem: PlayerScore) = oldItem.player?.id == newItem.player?.id

    override fun areContentsTheSame(oldItem: PlayerScore, newItem: PlayerScore) = oldItem == newItem
}

class MatchDiffCallback : DiffUtil.ItemCallback<Match>() {
    override fun areItemsTheSame(oldItem: Match, newItem: Match) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Match, newItem: Match) = oldItem == newItem
}