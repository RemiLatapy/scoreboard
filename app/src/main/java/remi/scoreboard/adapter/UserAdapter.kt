package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.data.User
import remi.scoreboard.databinding.ItemCardUserBinding

class UserAdapter : ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.apply {
            bind(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCardUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.apply {
                user = item
                executePendingBindings()
            }
        }
    }
}