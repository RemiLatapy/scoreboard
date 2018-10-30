package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.databinding.ItemCardGameRealmBinding
import remi.scoreboard.realmdata.GameRealm

class GameRealmAdapter : ListAdapter<GameRealm, GameRealmAdapter.ViewHolder>(GameRealmDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = getItem(position)
        holder.apply {
            bind(game)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardGameRealmBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCardGameRealmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GameRealm) {
            binding.apply {
                game = item
                executePendingBindings()
            }
        }
    }
}

class GameRealmDiffCallback : DiffUtil.ItemCallback<GameRealm>() {
    override fun areItemsTheSame(oldItem: GameRealm, newItem: GameRealm) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: GameRealm, newItem: GameRealm) = oldItem == newItem
}