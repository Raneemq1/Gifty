package com.example.myapplication2.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.myapplication2.adapter.GridAdapter
import com.example.myapplication2.LoginActivity
import com.example.myapplication2.R
import com.example.myapplication2.SignUpShopActivity
import com.example.myapplication2.databinding.FragmentCategorySelectionBinding
import com.example.myapplication2.model.Category
import com.example.myapplication2.model.Shop
import com.example.myapplication2.ui.addItem.AddItemFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CategorySelectionFragment : Fragment() {

    private var _binding: FragmentCategorySelectionBinding? = null
    private val binding get() = _binding!!
    lateinit var categoryGRV: GridView
    lateinit var categoryList: ArrayList<Category>
    private var shop_email_login: String = LoginActivity.shop_email
    private var shop_email_signup: String = SignUpShopActivity.shop_email
    private lateinit var email: String
    val ref = FirebaseDatabase.getInstance().getReference("Shops")
    var categoryDisplayList = ArrayList<Category>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorySelectionBinding.inflate(inflater, container, false)

        val root: View = binding.root
        categoryGRV = root.findViewById(R.id.idGRV)
        categoryList = ArrayList<Category>()

        if (shop_email_login.isEmpty()) {
            email = shop_email_signup
        } else {
            email = shop_email_login
        }

        prepareCategoryList(root.context)


        categoryGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            //Toast.makeText(context, categoryList[position].categoryName + " selected", Toast.LENGTH_SHORT).show()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout2, AddItemFragment())
            transaction?.commit()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Create static categories list for all shops
     */
    private fun prepareCategoryList(context: Context) {
        categoryList.add(Category("Accessories", R.drawable.a))
        categoryList.add(Category("Antiques", R.drawable.b))
        categoryList.add(Category("Perfume", R.drawable.c))
        categoryList.add(Category("Books", R.drawable.d))
        categoryList.add(Category("Watches", R.drawable.e))
        categoryList.add(Category("Men Clothes", R.drawable.f))
        categoryList.add(Category("Women Clothes", R.drawable.g))
        categoryList.add(Category("Children Clothes", R.drawable.h))
        categoryList.add(Category("Toys", R.drawable.i))
        categoryList.add(Category("Candles", R.drawable.j))
        categoryList.add(Category("Painting", R.drawable.l))
        categoryList.add(Category("Others", R.drawable.m))
        var category_list = ArrayList<String>()

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p!!.exists()) { //Check that database isn't null
                    category_list.clear()
                    for (u in p.children) {
                        val shop = u.getValue(Shop::class.java)
                        if (shop?.email.equals(email)) {
                            category_list = shop?.categories!!

                        }

                    }

                    for (i in 0..category_list.size - 1) {
                        for (j in 0..categoryList.size - 1) {
                            if (category_list[i].equals(categoryList[j].categoryName)) {
                                categoryDisplayList.add(categoryList[j])
                            }
                        }
                    }

                    val categoryAdapter = GridAdapter(categoryDisplayList, context)
                    categoryGRV.adapter = categoryAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }


}