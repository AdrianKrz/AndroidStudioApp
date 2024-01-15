package com.example.prm4.adapers

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.prm4.KierowcaCallback
import com.example.prm4.Navigable
import com.example.prm4.R
import com.example.prm4.data.KierowcaDatabase
import com.example.prm4.data.model.KierowcaEntity
import com.example.prm4.databinding.ListItenBinding
import com.example.prm4.fragments.InfoFragment
import kotlin.concurrent.thread

class KierowcaViewHolder(val binding: ListItenBinding)
    : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceType")
    fun bind(kierowca: KierowcaEntity) {
        binding.name.text = kierowca.imie
        binding.ingridients.text = kierowca.osiagniecia
        if(kierowca.icon == "mercedes")
            binding.imageView.setImageResource(R.drawable.mercedes)
        if(kierowca.icon == "red_bulll")
            binding.imageView.setImageResource(R.drawable.red_bulll)
        if(kierowca.icon == "ferrari")
            binding.imageView.setImageResource(R.drawable.ferrari)
    }


}

class KierowcaAdapter(private val navigable: Navigable) : RecyclerView.Adapter<KierowcaViewHolder>() {
    private val data = mutableListOf<KierowcaEntity>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KierowcaViewHolder {
        val binding = ListItenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return KierowcaViewHolder(binding).also { vh ->
            binding.root.setOnClickListener{
                DishImagesAdapter.set(vh.absoluteAdapterPosition)
                InfoFragment.set(data[vh.absoluteAdapterPosition].id)
                InfoFragment.setImie(data[vh.absoluteAdapterPosition].imie)
                InfoFragment.setOsiagniecia(data[vh.absoluteAdapterPosition].osiagniecia)
                navigable.navigate(Navigable.Destination.Info)

            }
        }.also { vh ->
            binding.root.setOnLongClickListener {
                AlertDialog.Builder(parent.context)
                    .setTitle("Czy na pewno chcesz usunąć ten element?")
                    .setMessage("Ta operacja jest nieodwracalna.")
                    .setPositiveButton("Usuń") { dialog, _ ->
                        thread {
                            KierowcaDatabase.open(parent.context).kierowcy.removeKierowca(data[vh.absoluteAdapterPosition])

                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Anuluj") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }

        }
    }


    override fun onBindViewHolder(holder: KierowcaViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun replace(newData: List<KierowcaEntity>) {
        val callback = KierowcaCallback(data, newData)
        data.clear()
        data.addAll(newData)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }

    fun sort() {
        val notSorted = data.toList()
        data.sortBy { it.imie }
        val callback = KierowcaCallback(notSorted, data)
        val result = DiffUtil.calculateDiff(callback)
        handler.post {
            result.dispatchUpdatesTo(this)
        }
    }
}