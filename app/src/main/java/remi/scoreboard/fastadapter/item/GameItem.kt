package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import remi.scoreboard.R
import remi.scoreboard.data.Game
import remi.scoreboard.databinding.ItemCardGameBinding

class GameItem(val game: Game) : AbstractItem<GameItem, GameItem.ViewHolder>() {

    override fun getType() = R.id.fastadapter_game_id

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardGameBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_game

    inner class ViewHolder(private val binding: ItemCardGameBinding) : FastAdapter.ViewHolder<GameItem>(binding.root) {
        override fun unbindView(item: GameItem) {
            binding.apply {
                game = null
                executePendingBindings()
            }
        }

        override fun bindView(item: GameItem, payloads: MutableList<Any>) {
            binding.apply {
                game = item.game
                executePendingBindings()
            }
        }

    }
}