package remi.scoreboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import remi.scoreboard.R
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.databinding.ItemCardPlayerscoreBinding

class PlayerScoreAdapter :
    ListAdapter<PlayerScore, PlayerScoreAdapter.ViewHolder>(PlayerScoreDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playerScore: PlayerScore = getItem(position)
        holder.apply {
            itemView.findViewById<TextView>(R.id.player_num).text = "Player $position"
            bind(playerScore, createOnClickListner(playerScore, holder.itemView))
        }
    }

    private fun createOnClickListner(playerScore: PlayerScore, view: View) = View.OnClickListener {
        val builder: AlertDialog.Builder? =
            AlertDialog.Builder(view.rootView.context)

        builder?.let {
            val inflater = LayoutInflater.from(view.rootView.context)
            val view = inflater.inflate(R.layout.dialog_add_score, null)
            it.setTitle(playerScore.player?.username + " score")
                .setView(view)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    val points = view.findViewById<TextInputEditText>(R.id.txt_points)
                    // TODO add score here
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardPlayerscoreBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCardPlayerscoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlayerScore, listener: View.OnClickListener) {
            binding.apply {
                clickListener = listener
//                playerScore = item // TODO fix
                executePendingBindings()
            }
        }
    }
}