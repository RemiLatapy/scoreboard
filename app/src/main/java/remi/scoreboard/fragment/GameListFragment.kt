package remi.scoreboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import remi.scoreboard.adapter.GameAdapter
import remi.scoreboard.databinding.FragmentGameListBinding
import remi.scoreboard.viewmodel.GameViewModel

class GameListFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGameListBinding.inflate(inflater, container, false)

        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        val gameListAdapter = GameAdapter()
        binding.recycler.adapter = gameListAdapter
        gameViewModel.allGames.observe(this,
            Observer { gameList -> gameList?.let { gameListAdapter.submitList(it) } })

        return binding.root
    }
}
