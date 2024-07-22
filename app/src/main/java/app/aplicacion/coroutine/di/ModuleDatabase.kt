package app.aplicacion.coroutine.di

import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleDatabase {
    @Provides
    @Singleton
    fun providesDatabase() = FirebaseDatabase.getInstance()


    @Provides
    @Singleton
    fun providesReference(data: FirebaseDatabase) = data.getReference("User")


    @Provides
    @Singleton
    fun providesRepository(reference:DatabaseReference):UserRepository =UserImplementation(reference)

}