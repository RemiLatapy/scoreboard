package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import remi.scoreboard.R
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.databinding.ItemCardPlayerScoreBinding

class PlayerScoreItem(val player: PlayerScore) : AbstractItem<ChoosePlayerItem, PlayerScoreItem.ViewHolder>() {

    override fun getIdentifier() = player.id.hashCode().toLong()

    override fun getType() = R.id.fastadapter_playerscore_id

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardPlayerScoreBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_player_score

    inner class ViewHolder(private val binding: ItemCardPlayerScoreBinding) :
        FastAdapter.ViewHolder<ChoosePlayerItem>(binding.root) {
        override fun unbindView(item: ChoosePlayerItem) {

        }

        override fun bindView(item: ChoosePlayerItem, payloads: MutableList<Any>) {
        }
    }
}