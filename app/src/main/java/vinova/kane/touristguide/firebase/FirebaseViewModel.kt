package vinova.kane.touristguide.firebase

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class FirebaseViewModel: ViewModel() {

    private var userRef = FirebaseDatabase.getInstance().getReference("users")
    private var commentRef = FirebaseDatabase.getInstance().getReference("comments")
    var addCommentFlag = MutableLiveData<Boolean>().apply { value = false }

    @NonNull
    fun getUserLiveData(): LiveData<DataSnapshot>? {
        return FirebaseQueryLiveData(userRef)
    }

    @NonNull
    fun getCommentLiveData(placeId: String): LiveData<DataSnapshot>{
        return FirebaseQueryLiveData(commentRef.child(placeId))
    }

    fun setAddComment(){
        addCommentFlag.postValue(!addCommentFlag.value!!)
    }

}