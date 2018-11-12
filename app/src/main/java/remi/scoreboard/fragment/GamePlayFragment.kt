package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import remi.scoreboard.R
import remi.scoreboard.adapter.PlayerAdapter
import remi.scoreboard.databinding.FragmentGamePlayBinding

class GamePlayFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // TODO get match/players here (from DB)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGamePlayBinding.inflate(inflater, container, false)

        val adapter = PlayerAdapter()
        binding.recycler.adapter = adapter

        // TODO submit list of playerscore & observe
        adapter.submitList(null)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gameplay, menu)
    }
}