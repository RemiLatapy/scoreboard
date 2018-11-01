package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.data.User
import remi.scoreboard.databinding.ItemCardUserBinding

class UserAdapter(private val userSelectedCallback: UserSelectedCallback) :
    ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    interface UserSelectedCallback {
        fun onUserSelected(user: User)
        fun isUserSelected(user: User): Boolean
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = getItem(position)
        holder.apply {
            itemView.isSelected = userSelectedCallback.isUserSelected(user)
            bind(user, View.OnClickListener {
                userSelectedCallback.onUserSelected(user)
                it.isSelected = userSelectedCallback.isUserSelected(user)
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