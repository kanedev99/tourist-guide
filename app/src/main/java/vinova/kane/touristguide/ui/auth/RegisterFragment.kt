package vinova.kane.touristguide.ui.auth

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
import vinova.kane.touristguide.databinding.FragmentRegisterBinding
import vinova.kane.touristguide.vo.User

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
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

        binding = FragmentRegisterBinding.inflate(inflater)

        binding.fragment = this

        return binding.root
    }

    private fun registerAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        initDataUser(email)
                        Toast.makeText(activity, "Registration Success", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_register_to_login)
                    } else {
                        Toast.makeText(activity, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun initDataUser(email: String){
        val user = User(email = email, uid = auth.uid!!)
        dbReference.child(auth.uid!!).setValue(user)
    }


    fun initEvent(view: View) {
        when (view) {
            binding.btnSignUp -> {
                registerAccount(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            }
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }
}