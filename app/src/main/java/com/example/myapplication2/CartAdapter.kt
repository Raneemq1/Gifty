package com.example.myapplication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication2.model.Cart
import com.example.myapplication2.model.Item
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class CartAdapter(private val itemsList: ArrayList<Cart>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    companion object {
        @JvmStatic
         var totalPrice: Int = 0

    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cartAdd: Button = view.findViewById(R.id.cart_add)
        val cartSub: Button = view.findViewById(R.id.cart_sub)
        val cartDelete: Button = view.findViewById(R.id.cart_delete)
        val cartImg: ImageView = view.findViewById(R.id.cart_img)
        val cartQuantity: TextView = view.findViewById(R.id.cart_quantity)
        val cartPrice: TextView = view.findViewById(R.id.cart_price)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemsList[position]
        holder.cartPrice.setText(currentItem.requestedQuantity.toString())
        val itemId = currentItem.itemId
        /*Get the url of product image*/
        var sRef = FirebaseStorage.getInstance().reference
        var ref: DatabaseReference
        var path: String = ""
        ref = FirebaseDatabase.getInstance().getReference("Items")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p: DataSnapshot) {
                if (p!!.exists()) { //Check that database isn't null
                    for (u in p.children) {
                        val item = u.getValue(Item::class.java)
                        if (item?.id.toString().equals(itemId)) {
                            sRef.child(item?.pathImage.toString()).downloadUrl
                                .addOnSuccessListener { url ->

                                    path = url.toString()
                                    Glide.with(holder.view.context).load(path).centerCrop()
                                        .into(holder.cartImg)
                                }
                            holder.cartPrice.setText(((item?.price)?.times((currentItem.requestedQuantity))).toString())
                            totalPrice += Integer.parseInt(holder.cartPrice.text.toString())

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        holder.cartQuantity.setText(currentItem.requestedQuantity.toString())

        holder.cartAdd.setOnClickListener {
            //holder.
        }
        holder.cartSub.setOnClickListener {

        }
        holder.cartDelete.setOnClickListener {

        }


    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}