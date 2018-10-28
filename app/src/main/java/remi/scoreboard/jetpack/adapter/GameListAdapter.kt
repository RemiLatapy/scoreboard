package remi.scoreboard.jetpack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import remi.scoreboard.R
import remi.scoreboard.jetpack.fragment.GameListFragmentDirections
import remi.scoreboard.jetpack.model.Game
import remi.scoreboard.jetpack.model.Match

class GameListAdapter internal constructor(ctx: Context) : RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {

    private val inflater = LayoutInflater.from(ctx)
    private var gameList = emptyList<Game>()

    internal fun setGameList(gameList: List<Game>) {
        this.gameList = gameList
        notifyDataSetChanged()
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageGame = itemView.findViewById<ImageView>(R.id.game_image)!!
        val titleGame = itemView.findViewById<TextView>(R.id.game_title)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = inflater.inflate(R.layout.item_card_game, parent, false)
        return GameViewHolder(itemView)
    }

    override fun getItemCount() = gameList.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val current = gameList[position]
        holder.titleGame.text = current.name
        Glide.with(holder.imageGame.context)
            .load(current.thumbnail)
            .apply(RequestOptions().centerCrop())
            .into(holder.imageGame)

        holder.itemView.setOnClickListener {
            val match = Match(current.gid, emptyList())
            val action = GameListFragmentDirections.ActionGameListDestToGamePlayerFragment(match)
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }
}