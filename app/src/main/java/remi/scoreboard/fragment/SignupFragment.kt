package remi.scoreboard.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import remi.scoreboard.R
import remi.scoreboard.data.Status
import remi.scoreboard.viewmodel.LoginSignupViewModel

// TODO check connection state
class SignupFragment : Fragment() {

    private lateinit var progressBar: ProgressBar // TODO move control to viewModel
    private lateinit var errorText: TextView // TODO move control to viewModel
    private lateinit var viewModel: LoginSignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(LoginSignupViewModel::class.java)

//        // Observe current user changes (aka callback from create user flow)
//        viewModel.currentUser.observe(this, Observer { resUser ->
//            Log.d("LOGIN", "current user is now: ${resUser?.toString()}")
//
//            if (resUser.status == Status.ERROR) {
//                progressBar.visibility = View.INVISIBLE
//                Log.d("LOGIN", "error while creating user")
//            } else if (resUser.status == Status.SUCCESS) {
//                val action = SignupFragmentDirections.actionSignupToMain()
//                findNavController().navigate(action)
//                activity?.finish()
//            }
//        })

        viewModel.signupState.observe(this, Observer { cb ->
            when (cb.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.INVISIBLE
                    errorText.text = ""
                    val action = SignupFragmentDirections.actionSignupToMain()
                    findNavController().navigate(action)
                    activity?.finish()
                }
                Status.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    errorText.text = cb.message
                    // TODO handle different errors & reset error on focus/typing
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    errorText.text = ""
                }
                Status.IDLE -> {
                    progressBar.visibility = View.INVISIBLE
                    errorText.text = ""
                }
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
        errorText = view.findViewById(R.id.error)
    }

    private fun setupSignup(view: View) {
        view.run {
            findViewById<Button>(R.id.signup_btn)?.setOnClickListener {

                val username = findViewById<EditText>(R.id.username).text.toString()
                val email = findViewById<EditText>(R.id.email).text.toString()
                val password = findViewById<EditText>(R.id.password).text.toString()
                viewModel.createUser(username, email, password)
            }
        }
    }
}
