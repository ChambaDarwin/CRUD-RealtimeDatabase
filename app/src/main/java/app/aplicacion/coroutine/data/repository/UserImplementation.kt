package app.aplicacion.coroutine.data.repository

import androidx.lifecycle.LiveData
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.util.DataState
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import javax.inject.Inject


class UserImplementation @Inject constructor(
   private val reference: DatabaseReference,

) :UserRepository  {



    override fun insertUser(user: UserData, state: (DataState<Boolean>) -> Unit) {
        val id=reference.push().key!!
        user.id=id

        reference.child(id).setValue(user).addOnSuccessListener {
            state.invoke(DataState.Sucess(true))
        }.addOnFailureListener {
            state.invoke(DataState.Error(it.message))
        }

    }

    override fun deleteUser(user:UserData) {
        reference.child(user.id).removeValue()

    }

    override fun updateUser(user: UserData, state: (DataState<Boolean>) -> Unit) {
        reference.child(user.id).updateChildren(user.toMap()).addOnSuccessListener {
            state.invoke(DataState.Sucess(true))
        }.addOnFailureListener {
            state.invoke(DataState.Error(it.message))
        }
    }

    override fun getAllUser(lista: (MutableList<UserData>) -> Unit) {
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaUser= mutableListOf<UserData>()
                if(snapshot.exists()){
                    for (i in snapshot.children){
                        val user=i.getValue(UserData::class.java)
                        user?.let {
                            listaUser.add(it)
                        }
                    }
                    lista.invoke(listaUser)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }






}