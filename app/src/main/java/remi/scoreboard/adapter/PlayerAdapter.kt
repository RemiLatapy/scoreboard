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
import remi.scoreboard.databinding.ItemCardPlayerBinding
import remi.scoreboard.viewmodel.SharedViewModel

class PlayerAdapter(val sharedViewModel: SharedViewModel) :
    ListAdapter<PlayerScore, PlayerAdapter.ViewHolder>(PlayerScoreDiffCallback()) {

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
            it.setTitle(playerScore.user?.displayName + " score")
                .setView(view)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    val points = view.findViewById<TextInputEditText>(R.id.points)
                    sharedViewModel.addPoints(playerScore, Integer.valueOf(points.text.toString()))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardPlayerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCardPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlayerScore, listener: View.OnClickListener) {
            binding.apply {
                clickListener = listener
                playerScore = item
                executePendingBindings()
            }
        }
    }
}