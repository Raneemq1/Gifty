package com.example.myapplication2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.myapplication2.R
import com.example.myapplication2.databinding.FragmentCategoryItemBinding
import com.example.myapplication2.databinding.FragmentCategorySelectionBinding
import com.example.myapplication2.ui.addItem.AddItemFragment

/**
 * This fragment is a blank fragment that starts categories selection fragment
 */
class CategoryItemFragment : Fragment() {

    private var _binding: FragmentCategoryItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryItemBinding.inflate(inflater, container, false)
        val root = binding.root

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout2, CategorySelectionFragment())
        transaction?.commit()

        return root
    }


}