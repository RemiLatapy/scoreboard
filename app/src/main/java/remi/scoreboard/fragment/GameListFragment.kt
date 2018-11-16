package remi.scoreboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import remi.scoreboard.databinding.FragmentGameListBinding
import remi.scoreboard.fastadapter.item.GameItem
import remi.scoreboard.viewmodel.GameViewModel

class GameListFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var fastAdapter: FastAdapter<GameItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameItemAdapter = ItemAdapter<GameItem>()

        fastAdapter = FastAdapter.with(gameItemAdapter)
        fastAdapter.setHasStableIds(true)
        fastAdapter.withOnClickListener { _, _, gameItem, _ ->
            val action = GameListFragmentDirections.actionChoosePlayers(gameItem.game.id)
            findNavController().navigate(action)
            true
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameListBinding.inflate(inflater, container, false)

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        binding.recycler.adapter = fastAdapter

        gameViewModel.allGames.observe(this,
            Observer { gameList ->
                gameList?.let {
                    (fastAdapter.adapter(0) as? ItemAdapter<GameItem>)
                        ?.setNewList(it.map { game -> GameItem(game) })
                }
            })


        return binding.root
    }
}

