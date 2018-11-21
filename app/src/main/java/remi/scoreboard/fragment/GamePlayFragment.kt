package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import remi.scoreboard.R
import remi.scoreboard.databinding.FragmentGamePlayBinding
import remi.scoreboard.fastadapter.item.PlayerScoreItem
import remi.scoreboard.viewmodel.GamePlayViewModel

class GamePlayFragment : Fragment() {

    private lateinit var viewmodel: GamePlayViewModel
    private lateinit var binding: FragmentGamePlayBinding
    private lateinit var fastAdapter: FastItemAdapter<PlayerScoreItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.let {
            val matchId = GamePlayFragmentArgs.fromBundle(it.intent.extras).matchId
            viewmodel = ViewModelProviders.of(this, GamePlayViewModel.GamePlayViewModelFactory(matchId))
                .get(GamePlayViewModel::class.java)

            viewmodel.currentMatch.observe(this, Observer { match ->
                fastAdapter.setNewList(match.scorePlayerList.map { PlayerScoreItem(it) })
            })
        }

        fastAdapter = getFastAdapter()
    }

    private fun getFastAdapter(): FastItemAdapter<PlayerScoreItem> =
        FastItemAdapter<PlayerScoreItem>().apply {
            setHasStableIds(true)
            withOnClickListener { v, adapter, item, position -> true } // TODO click
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGamePlayBinding.inflate(inflater, container, false)
        binding.recycler.adapter = fastAdapter
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gameplay, menu)
    }
}