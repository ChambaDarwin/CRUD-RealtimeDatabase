package app.aplicacion.coroutine.data.repository

import androidx.lifecycle.LiveData
import app.aplicacion.coroutine.data.model.ImageStorage
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject


class UserImplementation @Inject constructor(
    private val reference: DatabaseReference,
    private val store: StorageReference

) : UserRepository {


    override suspend fun insertUser(user: UserData, state: (DataState<String>) -> Unit) {
        try {
            val id = UUID.randomUUID().toString()
            user.id = id
            reference.child(id).setValue(user).await()
            state.invoke(DataState.Sucess(id))
        } catch (e: Exception) {
            e.printStackTrace()
            state.invoke(DataState.Error(e.message))
        }


    }

    override suspend fun insertCloudStorage(
        byteArray: List<ByteArray>,
        state: (DataState<ImageStorage>) -> Unit
    ) {

        try {
            val listaUid = mutableListOf<String>()
            val listaString = mutableListOf<String>()
            coroutineScope {
                async {
                    byteArray.forEach {
                        val id = UUID.randomUUID().toString()
                        val reference = store.child("User/image/$id")
                        val result = reference.putBytes(it).await()
                        val images = result.storage.downloadUrl.await().toString()
                        listaString.add(images)
                        listaUid.add(id)
                    }
                }.await()
                val image = ImageStorage(listaUid, listaString)
                state.invoke(DataState.Sucess(image))
            }


        } catch (e: Exception) {
            e.printStackTrace()
            state.invoke(DataState.Error(e.message))
        }

    }

    override suspend fun deleteUser(user: UserData, state: (DataState<String>) -> Unit) {
        try {
            coroutineScope {
                async {
                    user.image?.let {imageDelete->
                        imageDelete.uid.forEach {
                            store.child("User/image/$it").delete().await()
                        }

                    }
                }.await()
                reference.child(user.id).removeValue().await()
                state.invoke(DataState.Sucess("Registro eliminado con exito"))
            }


        }catch (e:Exception){
            e.printStackTrace()
            state.invoke(DataState.Error("Error: ${e.message}"))
        }
    }



    override suspend fun updateUser(user: UserData, state: (DataState<String>) -> Unit) {
        try {
            coroutineScope {
                async {
                    reference.child(user.id).updateChildren(user.toMap()).await()
                }.await()
                state.invoke(DataState.Sucess("registro modificado con exito"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            state.invoke(DataState.Error(e.message))

        }

    }

    override fun getAllUser(lista: (MutableList<UserData>) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaUser = mutableListOf<UserData>()
                println("objeto que devuelve de realtime datbase ${snapshot.children}")
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val user = i.getValue(UserData::class.java)
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