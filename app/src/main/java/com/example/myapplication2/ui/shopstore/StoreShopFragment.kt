package com.example.myapplication2.ui.shopstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.adapter.Item_Gallery_RV
import com.example.myapplication2.LoginActivity
import com.example.myapplication2.R
import com.example.myapplication2.SignUpShopActivity
import com.example.myapplication2.databinding.FragmentShopStoreBinding
import com.example.myapplication2.model.Item
import com.google.firebase.database.*

/**
 * This fragment shows the items for each shop to edit item information or delete it
 */
class StoreShopFragment : Fragment() {


    private var _binding: FragmentShopStoreBinding? = null

    private lateinit var item_adapter: Item_Gallery_RV
    private lateinit var item_rv: RecyclerView
    private lateinit var items_list: ArrayList<Item>
    private lateinit var ref: DatabaseReference

    private var shop_email_login: String = LoginActivity.shop_email
    private var shop_email_signup: String = SignUpShopActivity.shop_email
    private lateinit var email: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(StoreShopViewModel::class.java)

        _binding = FragmentShopStoreBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (shop_email_login.isEmpty()) {
            email = shop_email_signup
        } else {
            email = shop_email_login
        }
        ref = FirebaseDatabase.getInstance().getReference("Items")
        item_rv = view.findViewById(R.id.gallery_rv)
        item_rv.layoutManager = LinearLayoutManager(context)
        item_rv.setHasFixedSize(true)
        prepareData()

    }


    private fun prepareData() {
        /*Retrive data from firebase as login */
        items_list = arrayListOf<Item>()


        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p!!.exists()) { //Check that database isn't null
                    items_list.clear()
                    for (u in p.children) {
                        val item = u.getValue(Item::class.java)
                        if (item?.email.equals(email)) {
                            items_list.add(item!!)
                        }

                    }

                    item_adapter = Item_Gallery_RV(items_list)
                    item_rv.adapter = item_adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }

}