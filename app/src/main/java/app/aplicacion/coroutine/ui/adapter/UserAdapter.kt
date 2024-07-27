package app.aplicacion.coroutine.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.coroutine.R
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.databinding.ItemRecyclerBinding
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val userItem = object : DiffUtil.ItemCallback<UserData>() {
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem == newItem
        }


    }
    val diff = AsyncListDiffer(this, userItem)

    inner class UserViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = ItemRecyclerBinding.bind(view)


        fun render(user: UserData, ids: ((UserData) -> Unit)?) {

            binding.nombre.text = user.nombre
            binding.apellido.text = user.apellido
            binding.email.text = user.email
            binding.materia.text = user.materia

            Glide.with(binding.imageView).load(user.image!!.donwload[0]).into(binding.imageView)


            itemView.setOnClickListener {
                ids?.invoke(user)

            }


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent,false))
    }

    private var user: ((UserData) -> Unit)? = null

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.render(diff.currentList[position], user)
    }

    fun sendId(userSeleted: ((UserData) -> Unit)?) {
        user = userSeleted

    }

    override fun getItemCount() = diff.currentList.size
}