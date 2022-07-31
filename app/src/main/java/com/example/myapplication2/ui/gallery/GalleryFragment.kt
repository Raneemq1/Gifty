package com.example.myapplication2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.*
import com.example.myapplication2.R
import com.example.myapplication2.databinding.FragmentGalleryBinding
import com.example.myapplication2.model.Cart
import com.google.firebase.database.*

/**
 * This fragment displays cart fragment by showing the added items in a recycler view
 */
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    //
    private lateinit var cart_adapter: CartAdapter
    private lateinit var cart_items_rv: RecyclerView
    private lateinit var cart_items_list: ArrayList<Cart>
    private lateinit var ref: DatabaseReference
    private lateinit var total:TextView

    var email:String=""
    private var user_email_login: String = LoginActivity.user_email
    private var user_email_signup: String = SignUpUserActivity.user_email


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user_email_login.isEmpty()) {
            email = user_email_signup
        } else {
            email = user_email_login
        }
        total=view.findViewById(R.id.total_price)
        ref = FirebaseDatabase.getInstance().getReference("Cart")
        cart_items_rv = view.findViewById(R.id.cart_items_rv)
        cart_items_rv.layoutManager = LinearLayoutManager(context)
        cart_items_rv.setHasFixedSize(true)
        prepareData()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun prepareData(){
        /*Retrieve cart items */
        cart_items_list = arrayListOf<Cart>()


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) { //Check that database isn't null
                    cart_items_list.clear()
                    for (u in snapshot.children) {
                        val cartItem = u.getValue(Cart::class.java)
                        if(cartItem?.userEmail.toString().equals(email)){
                            cart_items_list.add(cartItem!!)
                        }
                    }

                    cart_adapter=CartAdapter(cart_items_list)
                    cart_items_rv.adapter=cart_adapter
                    total.setText("Total price = "+CartAdapter.totalPrice.toString())
                    CartAdapter.totalPrice=0

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}