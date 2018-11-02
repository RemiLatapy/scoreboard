package remi.scoreboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import remi.scoreboard.R
import remi.scoreboard.adapter.MatchAdapter
import remi.scoreboard.databinding.FragmentStatsBinding
import remi.scoreboard.viewmodel.MatchViewModel

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatsBinding.inflate(inflater, container, false)

        val matchViewModel = ViewModelProviders.of(this).get(MatchViewModel::class.java)

        val adapter = MatchAdapter()
        binding.recycler.adapter = adapter
        matchViewModel.allMatchs.observe(this, Observer { matchList ->
            matchList?.let { adapter.submitList(it) }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(findNavController())
    }
}
