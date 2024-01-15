package com.example.prm4.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm4.adapers.DishImagesAdapter
import com.example.prm4.Navigable
import com.example.prm4.R
import com.example.prm4.data.KierowcaDatabase
import com.example.prm4.data.model.KierowcaEntity
import com.example.prm4.databinding.FragmentEditBinding
import kotlin.concurrent.thread


class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: DishImagesAdapter

    companion object {
        @JvmStatic
        var idaaa: Long = 0
            private set(value) {
                field = value
            }

        @JvmStatic
        fun set(value: Long) {
            idaaa = value
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DishImagesAdapter()

        binding.photo.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }


        if(!idaaa.equals(0)){
            if(!InfoFragment.osiagniecia.length.equals(0)){
                binding.dishName.setText(InfoFragment.imie)
                binding.ingredientsName.setText(InfoFragment.osiagniecia.substring(1, InfoFragment.osiagniecia.length -1))
            }
        }

        binding.save.setOnClickListener {

            val newKierowca = KierowcaEntity(
                imie = binding.dishName.text.toString(),
                osiagniecia = binding.ingredientsName.text.toString(),
                icon = resources.getResourceEntryName(adapter.selectedIdRes)
            )

            thread {
                KierowcaDatabase.open(requireContext()).kierowcy.addKierowca(newKierowca)
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
            }

        }

        binding.button2.setOnClickListener {
            thread {
                val newKierowca = KierowcaEntity(
                    idaaa,
                    imie = binding.dishName.text.toString(),
                    osiagniecia = binding.ingredientsName.text.toString(),
                    icon = resources.getResourceEntryName(adapter.selectedIdRes)
                )
                KierowcaDatabase.open(requireContext()).kierowcy.updateKierowca(newKierowca)
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
                idaaa = 0
            }

        }



    }


}