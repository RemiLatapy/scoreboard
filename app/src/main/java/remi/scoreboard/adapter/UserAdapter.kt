package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.data.User
import remi.scoreboard.databinding.ItemCardUserBinding

class UserAdapter(val userSelectedCallback: UserSelectedCallback) : ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    interface UserSelectedCallback {
        fun onUserSelected(selected: Boolean)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.apply {
            bind(user, View.OnClickListener {
                // TODO
                user.isSelected = !user.isSelected
                userSelectedCallback.onUserSelected(user.isSelected)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }



    inner class ViewHolder(private val binding: ItemCardUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User, listener: View.OnClickListener) {
            binding.apply {
                clickListener = listener
                user = item
                executePendingBindings()
            }
        }
    }
}