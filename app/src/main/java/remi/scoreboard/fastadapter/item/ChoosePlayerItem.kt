package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import remi.scoreboard.R
import remi.scoreboard.data.Player
import remi.scoreboard.databinding.ItemCardChoosePlayerBinding

class ChoosePlayerItem(val player: Player) : AbstractItem<ChoosePlayerItem, ChoosePlayerItem.ViewHolder>() {
    override fun getType() = R.id.fastadapter_chooseplayer_id

    override fun getIdentifier() = player.id.hashCode().toLong()

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardChoosePlayerBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_choose_player

    inner class ViewHolder(private val binding: ItemCardChoosePlayerBinding) :
        FastAdapter.ViewHolder<ChoosePlayerItem>(binding.root) {

        override fun unbindView(item: ChoosePlayerItem) {
            binding.apply {
                player = null
                if (avatarFlipview.isBackSide) { // reset flipped view
                    avatarFlipview.flipDuration = 0
                    avatarFlipview.flipTheView()
                }
                executePendingBindings()
            }
        }

        override fun bindView(item: ChoosePlayerItem, payloads: MutableList<Any>) {
            binding.apply {
                player = item.player
                if (item.isSelected && avatarFlipview.isFrontSide) {
                    avatarFlipview.flipDuration = 0
                    avatarFlipview.flipTheView()
                    avatarFlipview.flipDuration = 400
                }
                executePendingBindings()
            }
        }
    }
}