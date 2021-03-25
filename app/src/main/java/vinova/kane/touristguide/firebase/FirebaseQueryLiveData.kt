package vinova.kane.touristguide.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import timber.log.Timber


class FirebaseQueryLiveData : LiveData<DataSnapshot> {
    private val query: Query
    private val listener =
        MyValueEventListener()

    constructor(query: Query) {
        this.query = query
    }

    constructor(ref: DatabaseReference) {
        query = ref
    }

    override fun onActive() {
        Timber.d("onActive")
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        Timber.d("onInactive")
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Timber.e("Can't listen to query $query")
        }
    }
}