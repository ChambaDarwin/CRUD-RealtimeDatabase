package app.aplicacion.coroutine.ui.adapter.adapterviewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.coroutine.R
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.databinding.AgerShowImagesBinding
import com.bumptech.glide.Glide

class AdapterViewPager : RecyclerView.Adapter<AdapterViewPager.PagerViewHolder>() {


    private var lista = mutableListOf<String>()

    fun setListaImages(listImages: MutableList<String>) {
        lista = listImages
        notifyDataSetChanged()
    }

    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = AgerShowImagesBinding.bind(view)
        fun render(imagen: String) {

                Glide.with(binding.imageView).load(imagen).into(binding.imageView)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ager_show_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.render(lista[position])
    }

    override fun getItemCount() = lista.size

}