package remi.scoreboard.fastadapter.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import remi.scoreboard.R
import remi.scoreboard.data.Match
import remi.scoreboard.databinding.ItemCardMatchBinding

class MatchItem(val match: Match) : AbstractItem<MatchItem, MatchItem.ViewHolder>() {

    var formatedDate: String = ""

    override fun getType() = R.id.fastadapter_match_id

    override fun getIdentifier() = match.id.hashCode().toLong()

    override fun getViewHolder(v: View): ViewHolder {
        val binding = ItemCardMatchBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun getLayoutRes() = R.layout.item_card_match

    inner class ViewHolder(private val binding: ItemCardMatchBinding) :
        FastAdapter.ViewHolder<MatchItem>(binding.root) {
        override fun unbindView(item: MatchItem) {
            binding.apply {
                match = null
                playerList = null
                textDate.text = null
                executePendingBindings()
            }
        }

        override fun bindView(item: MatchItem, payloads: MutableList<Any>) {
            binding.apply {
                match = item.match
                playerList = item.match.scorePlayerList.sortedByDescending { it.score }
                textDate.text = formatedDate
                executePendingBindings()
            }
        }

    }
}