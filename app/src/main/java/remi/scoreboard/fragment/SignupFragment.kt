package remi.scoreboard.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import remi.scoreboard.R
import remi.scoreboard.viewmodel.UserProfileViewModel

// TODO check connection state
class SignupFragment : Fragment() {

    private lateinit var progressBar: ProgressBar // TODO move control to viewModel
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)
        viewModel.isCurrentUserAvailable.observe(this, Observer { b ->
            if (b) // start observing current user
            {
                progressBar.visibility = View.INVISIBLE
                viewModel.currentUser?.observe(this, Observer { user ->
                    Log.d("LOGIN", "current user is now: " + user.toString())
                    val action = SignupFragmentDirections.actionSignupToMain()
                    findNavController().navigate(action)
                    activity?.finish()
                })
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSignup(view)

        view.findViewById<EditText>(R.id.username).setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let { if (!hasFocus) it.setText(it.text.toString().trim()) }
        }

        progressBar = view.findViewById(R.id.progress)
        progressBar.visibility = View.INVISIBLE
    }

    private fun setupSignup(view: View) {
        view.run {
            findViewById<Button>(R.id.signup_btn)?.setOnClickListener {
                progressBar.visibility = View.VISIBLE

                val username = findViewById<EditText>(R.id.username).text.toString()
                val email = findViewById<EditText>(R.id.email).text.toString()
                val password = findViewById<EditText>(R.id.password).text.toString()
                viewModel.createUser(username, email, password)
            }
        }
    }
}
