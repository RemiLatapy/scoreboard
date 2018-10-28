package remi.scoreboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.R
import remi.scoreboard.data.User

class UserListAdapter internal constructor(ctx: Context) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val inflater = LayoutInflater.from(ctx)
    private var userList = emptyList<User>()

    internal fun setUserList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUser = itemView.findViewById<ImageView>(R.id.avatar)!!
        val titleUser = itemView.findViewById<TextView>(R.id.player_name)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = inflater.inflate(R.layout.item_card_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = userList[position]
        holder.titleUser.text = current.name


        holder.itemView.setOnClickListener {
            // TODO
        }
    }
}