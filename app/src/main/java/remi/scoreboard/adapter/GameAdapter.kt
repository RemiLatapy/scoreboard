package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import remi.scoreboard.data.Game
import remi.scoreboard.databinding.ItemCardGameBinding

class GameAdapter(val gameSelectedCallback: GameSelectedCallback) : ListAdapter<Game, GameAdapter.ViewHolder>(GameDiffCallback()) {

    interface GameSelectedCallback {
        fun onGameSelected(game: Game)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = getItem(position)
        holder.apply {
            bind(createOnClickListener(game), game)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardGameBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    private fun createOnClickListener(game: Game): View.OnClickListener {
        return View.OnClickListener {
            gameSelectedCallback.onGameSelected(game)
        }
    }

    inner class ViewHolder(private val binding: ItemCardGameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Game) {
            binding.apply {
                clickListener = listener
                game = item
                executePendingBindings()
            }
        }
    }
}