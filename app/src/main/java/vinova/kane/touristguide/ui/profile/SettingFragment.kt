package vinova.kane.touristguide.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ir.androidexception.andexalertdialog.AndExAlertDialog
import ir.androidexception.andexalertdialog.InputType
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vinova.kane.touristguide.R
import vinova.kane.touristguide.databinding.FragmentSettingBinding
import vinova.kane.touristguide.firebase.FirebaseViewModel
import vinova.kane.touristguide.ui.auth.AuthActivity
import vinova.kane.touristguide.utils.SaveSharedPreference
import vinova.kane.touristguide.vo.User

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    private val firebaseViewModel: FirebaseViewModel by viewModel()
    private lateinit var userLiveData: LiveData<DataSnapshot>
    private lateinit var storageReference: StorageReference


    var user: User? = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
//        val credential = EmailAuthProvider.getCredential(auth.currentUser?.email!!, "password")
        dbReference = FirebaseDatabase.getInstance().getReference("users").child(auth.uid!!)
        getUserData()

        userLiveData = firebaseViewModel.getUserLiveData()!!
        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingBinding.inflate(inflater)

        binding.fragment = this

        getImageFromStorage()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLiveData.observe(viewLifecycleOwner,
            Observer<DataSnapshot> {dataSnapShot ->
                user = dataSnapShot.child(auth.uid!!).getValue(User::class.java)
                binding.tvUsername.text = user?.name
                binding.tvEmail.text= user?.email
            })
    }

    private fun getImageFromStorage() {
        val imgUri = SaveSharedPreference().getImageUri(requireContext())
        if (imgUri?.isNotEmpty()!!){
            Glide.with(requireContext())
                .load(imgUri)
                .into(binding.ivAvatar)
        } else {
            binding.ivAvatar.setImageResource(R.drawable.avatar_default)
        }
    }

    fun initEvent(view: View) {
        when (view) {
            binding.ivEditProfile -> {
                findNavController().navigate(R.id.action_setting_to_edit_profile)
            }

            binding.tvSignOut -> {
                Toast.makeText(activity, "Successfully Signed out", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            binding.tvChangePassword -> {
                AndExAlertDialog.Builder(requireContext())
                    .setTitle("Change your password")
                    .setMessage("Enter new password")
                    .setPositiveBtnText("OK")
                    .setNegativeBtnText("Cancel")
                    .setImage(R.drawable.password, 20)
                    .setCancelableOnTouchOutside(false)
                    .setEditText(true, false, "Your new password", InputType.PASSWORD)
                    .OnPositiveClicked { input ->
                        changePassword(input)
                    }
                    .OnNegativeClicked {}
                    .build()
            }
        }
    }

    private fun changePassword(newPassword: String){
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        activity,
                        "Password changes successfully",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        activity,
                        "password not changed",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

    private fun getUserData() {
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
            }
        })
    }

}