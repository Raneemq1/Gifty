package com.example.myapplication2.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication2.LoginActivity
import com.example.myapplication2.R
import com.example.myapplication2.SignUpUserActivity
import com.example.myapplication2.model.Cart
import com.example.myapplication2.model.Item
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ItemAdapter(private val itemlists: ArrayList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var exist: Int = 0
    var path: String = ""
    var sRef = FirebaseStorage.getInstance().reference
    var email: String = ""
    private var user_email_login: String = LoginActivity.user_email
    private var user_email_signup: String = SignUpUserActivity.user_email


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val price: TextView = itemView.findViewById(R.id.item_price)
        val name: TextView = itemView.findViewById(R.id.item_name)
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemlists[position]
        holder.name.setText(currentItem.name)
        holder.price.setText(currentItem.price.toString())


        if (user_email_login.isEmpty()) {
            email = user_email_signup
        } else {
            email = user_email_login
        }

        //holder.image.setImageResource(R.drawable.avatar)
        sRef.child(currentItem.pathImage).downloadUrl
            .addOnSuccessListener { url ->

                path = url.toString()
                Glide.with(holder.itemView.context).load(path).centerCrop().into(holder.image)
            }.addOnFailureListener { exception ->
                Log.e("TAG", "Exception: ${exception.message}")
            }

        holder.itemView.setOnClickListener {

            createDialog(holder.itemView.context, currentItem)
        }

    }

    override fun getItemCount(): Int {
        return itemlists.size
    }

    private fun createDialog(context: Context, currentItem: Item) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.product_order_popupwindow, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val img = mDialogView.findViewById<ImageView>(R.id.item_img)
        val btn_add = mDialogView.findViewById<ImageButton>(R.id.btn_add)
        val btn_sub = mDialogView.findViewById<ImageButton>(R.id.btn_sub)
        val add_to_cart = mDialogView.findViewById<Button>(R.id.add_to_cart)
        val cancel = mDialogView.findViewById<ImageButton>(R.id.cancel)
        val txt_quantity = mDialogView.findViewById<TextView>(R.id.text_quantity)
        val description = mDialogView.findViewById<TextView>(R.id.item_desciption)
        val totalPrice = mDialogView.findViewById<TextView>(R.id.total_price_item)

        sRef.child(currentItem.pathImage).downloadUrl
            .addOnSuccessListener { url ->

                path = url.toString()
                Glide.with(context).load(path).into(img)
            }.addOnFailureListener { exception ->
                Log.e("TAG", "Exception: ${exception.message}")
            }

        description.setText(currentItem.description)
        val mAlertDialog = mBuilder.show()
        btn_add.setOnClickListener {


            var num = Integer.parseInt(txt_quantity.text.toString())
            num = num + 1
            txt_quantity.setText(num.toString())
            totalPrice.setText("Total Price = " + (num * currentItem.price).toString())
        }

        btn_sub.setOnClickListener {
            var num = Integer.parseInt(txt_quantity.text.toString())
            if (num != 0) {
                num = num - 1
            } else {
                num = 0
            }
            txt_quantity.setText(num.toString())
            totalPrice.setText("Total Price = " + (num * currentItem.price).toString())


        }
        cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

        /**
         * Check the entry email is a shop or user
         * If shop doesn't allow to add items into cart
         * Allow normal users to add items into the cart after deciding the quantity needed
         */
        add_to_cart.setOnClickListener {
            if (email.isEmpty()) {
                Toast.makeText(
                    context,
                    "Please make a user account to join shopping with us",
                    Toast.LENGTH_SHORT
                ).show()
            } else {


                var database = FirebaseDatabase.getInstance().getReference("Cart")
                // Test for the existence of certain keys within a DataSnapshot
var temp:Int=0
                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot!!.exists()) {
                            for (c in snapshot.children) {
                                val cart = c.getValue(Cart::class.java)
                                if (cart?.itemId == currentItem.id) {

                                    temp=0
                                    break
                                }
                                else{
                                    temp++
                                }

                            }


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })



                //Add new item to cart
                /*         val cartId = database.push().key.toString()
                                val cart = Cart(cartId, email,
                                Integer.parseInt(txt_quantity.text.toString()), currentItem.id)
                                //Store cart item information in the firebase
                                database.child(cartId).setValue(cart).addOnCompleteListener {
                                    Toast.makeText(context, "Moved to your cart", Toast.LENGTH_SHORT).show()
                                }*/


            }


        }
    }

}