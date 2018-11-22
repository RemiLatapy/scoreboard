package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter_extensions.drag.IDraggable
import remi.scoreboard.R
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.databinding.ItemCardPlayerScoreBinding

class PlayerScoreItem(val playerScore: PlayerScore) : AbstractItem<ChoosePlayerItem, PlayerScoreItem.ViewHolder>(),
    IDraggable<Any, PlayerScoreItem> {

    private var draggable: Boolean = true
    override fun withIsDraggable(draggable: Boolean): PlayerScoreItem {
        this.draggable = draggable
        return this
    }

    override fun isDraggable() = draggable

    override fun getIdentifier() = playerScore.id.hashCode().toLong()

    override fun getType() = R.id.fastadapter_playerscore_id

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardPlayerScoreBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_player_score

    inner class ViewHolder(private val binding: ItemCardPlayerScoreBinding) :
        FastAdapter.ViewHolder<PlayerScoreItem>(binding.root) {
        override fun unbindView(item: PlayerScoreItem) {
            binding.apply {
                playerScore = null
                executePendingBindings()
            }
        }

        override fun bindView(item: PlayerScoreItem, payloads: MutableList<Any>) {
            binding.apply {
                playerScore = item.playerScore
                executePendingBindings()
            }
        }
    }
}