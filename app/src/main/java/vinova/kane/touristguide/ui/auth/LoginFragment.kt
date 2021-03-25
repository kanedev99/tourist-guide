package vinova.kane.touristguide.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentLoginBinding
import vinova.kane.touristguide.ui.home.MainActivity

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().getReference("users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater)

        binding.fragment = this

        return binding.root
    }

    private fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(activity, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun initEvent(view: View) {
        when (view) {
            binding.btnLogin -> {
                login(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            }
            binding.btnSignUp -> {
                findNavController().navigate(R.id.action_login_to_register)
            }
            binding.tvForgotPw -> {
                findNavController().navigate(R.id.action_login_to_forgot_password)
            }
        }
    }

}