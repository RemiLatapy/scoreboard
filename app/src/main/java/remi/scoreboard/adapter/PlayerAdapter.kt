package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.data.Player
import remi.scoreboard.databinding.ItemCardPlayerBinding

class PlayerAdapter:
    ListAdapter<Player, PlayerAdapter.ViewHolder>(PlayerDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player: Player = getItem(position)
        // TODO maintain selection in list
        holder.apply {
//            itemView.isSelected = userSelectedCallback.isUserSelected(user)
            bind(player, View.OnClickListener {
//                userSelectedCallback.onUserSelected(user)
//                it.isSelected = userSelectedCallback.isUserSelected(user)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardPlayerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCardPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Player, listener: View.OnClickListener) {
            binding.apply {
                clickListener = listener
                player = item
                executePendingBindings()
            }
        }
    }
}