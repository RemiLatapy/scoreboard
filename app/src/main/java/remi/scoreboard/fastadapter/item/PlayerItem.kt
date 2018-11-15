package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import remi.scoreboard.R
import remi.scoreboard.data.Player
import remi.scoreboard.databinding.ItemCardPlayerBinding

class PlayerItem(val player: Player) : AbstractItem<PlayerItem, PlayerItem.ViewHolder>() {
    override fun getType() = R.id.fastadapter_player_id

    override fun getIdentifier() = player.id.hashCode().toLong()

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardPlayerBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_player

    inner class ViewHolder(private val binding: ItemCardPlayerBinding) :
        FastAdapter.ViewHolder<PlayerItem>(binding.root) {

        override fun unbindView(item: PlayerItem) {
            binding.apply {
                player = null
                executePendingBindings()
            }
        }

        override fun bindView(item: PlayerItem, payloads: MutableList<Any>) {
            binding.apply {
                player = item.player
                matchNum.text = "not implemented yet" // TODO get number of match played
                executePendingBindings()
            }
        }
    }
}