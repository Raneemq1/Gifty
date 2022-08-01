package com.example.myapplication2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.R
import com.example.myapplication2.adapter.Shops_RV_Adapter
import com.example.myapplication2.databinding.FragmentHomeBinding
import com.example.myapplication2.model.Shop
import com.google.firebase.database.*

/**
 * This fragment displays shops information
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    //Define item adapter and recycler view to show the added itms in the cart
    private lateinit var shop_adapter: Shops_RV_Adapter
    private lateinit var shop_rv: RecyclerView
    private lateinit var shops_list: ArrayList<Shop>

    private lateinit var ref: DatabaseReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("Shops")
        shop_rv = view.findViewById(R.id.shops_rv)
        shop_rv.layoutManager = LinearLayoutManager(context)
        shop_rv.setHasFixedSize(true)
        prepareData()


    }

    /**
     * This function unreturned type of function
     * Read elements from Shop table in a realtime firebase
     */
    private fun prepareData() {

        shops_list = arrayListOf<Shop>()

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p!!.exists()) { //Check that database isn't null
                    shops_list.clear()
                    for (u in p.children) {
                        val shop = u.getValue(Shop::class.java)
                        shops_list.add(shop!!) // add shop object information to shop arraylist

                    }
                    shop_adapter = Shops_RV_Adapter(shops_list)
                    shop_rv.adapter = shop_adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }

}