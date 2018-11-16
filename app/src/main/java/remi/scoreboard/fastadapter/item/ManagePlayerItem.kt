package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import remi.scoreboard.R
import remi.scoreboard.data.Player
import remi.scoreboard.databinding.ItemCardManagePlayerBinding

class ManagePlayerItem(val player: Player) : AbstractItem<ManagePlayerItem, ManagePlayerItem.ViewHolder>() {
    override fun getType() = R.id.fastadapter_player_id

    override fun getIdentifier() = player.id.hashCode().toLong()

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardManagePlayerBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_manage_player

    inner class ViewHolder(private val binding: ItemCardManagePlayerBinding) :
        FastAdapter.ViewHolder<ManagePlayerItem>(binding.root) {

        override fun unbindView(item: ManagePlayerItem) {
            binding.apply {
                player = null
                executePendingBindings()
            }
        }

        override fun bindView(item: ManagePlayerItem, payloads: MutableList<Any>) {
            binding.apply {
                player = item.player
                matchNum.text = "12 games played" // TODO get number of match played
                lastGameDate.text = "Played last on 16/04/2019" // TODO get last game date
                executePendingBindings()
            }
        }
    }
}