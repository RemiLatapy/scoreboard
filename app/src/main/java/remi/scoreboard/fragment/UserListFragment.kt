package remi.scoreboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import remi.scoreboard.R
import remi.scoreboard.adapter.UserAdapter
import remi.scoreboard.databinding.FragmentUserListBinding
import remi.scoreboard.viewmodel.UserViewModel

class UserListFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUserListBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val userListAdapter = UserAdapter()
        binding.recycler.adapter = userListAdapter
        userViewModel.allUsers.observe(this,
            Observer { userList -> userList?.let { userListAdapter.submitList(it) } })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.show()
    }
}

