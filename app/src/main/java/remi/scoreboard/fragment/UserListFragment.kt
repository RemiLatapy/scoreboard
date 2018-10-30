package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import remi.scoreboard.R
import remi.scoreboard.adapter.UserAdapter
import remi.scoreboard.data.User
import remi.scoreboard.databinding.FragmentUserListBinding
import remi.scoreboard.viewmodel.UserViewModel

class UserListFragment : Fragment(), UserAdapter.UserSelectedCallback {

    private lateinit var userViewModel: UserViewModel
    private var fab: FloatingActionButton? = null

    var selectedNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUserListBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val userListAdapter = UserAdapter(this)
        binding.recycler.adapter = userListAdapter
        userViewModel.allUsers.observe(this,
            Observer { userList -> userList?.let { userListAdapter.submitList(it) } })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = activity?.findViewById(R.id.fab)
        fab?.hide()
        fab?.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        fab?.setOnClickListener { Toast.makeText(context, "FAB Click !", Toast.LENGTH_SHORT).show() }
    }

    override fun onUserSelected(selected: Boolean) {
        if (selected) selectedNum++ else selectedNum--
        if (selectedNum >= 2) fab?.show() else fab?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            userViewModel.insert(User("User " + (0..100).random()))
            return true
        } else
            return super.onContextItemSelected(item)
    }


}

