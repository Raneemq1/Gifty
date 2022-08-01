package com.example.myapplication2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication2.Edit_Item_Activity
import com.example.myapplication2.R
import com.example.myapplication2.model.Item
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Item_Gallery_RV(private val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<Item_Gallery_RV.ViewHolder>() {


    val ref = FirebaseDatabase.getInstance().getReference("Items")
    var sRef = FirebaseStorage.getInstance().reference
    var path: String = ""

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemImage: ImageView = itemView.findViewById(R.id.item_img)
        val itemEditBtn: ImageButton = itemView.findViewById(R.id.item_edit_btn)
        val itemDeleteBtn: ImageButton = itemView.findViewById(R.id.item_delete_btn)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.setText(currentItem.name)

        sRef.child(currentItem.pathImage.toString()).downloadUrl
            .addOnSuccessListener { url ->
                // Get the url path
                path = url.toString()
                Glide.with(holder.itemView.context).load(path).centerCrop().into(holder.itemImage)
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Exception: ${exception.message}")
            }



        holder.itemEditBtn.setOnClickListener {
            /*Transfer to edit activity for displaying all item information to allow edit them*/
            holder.itemEditBtn.setImageResource(R.drawable.ic_baseline_edit_25)
            val intent = Intent(holder.itemView.context, Edit_Item_Activity::class.java)
            intent.putExtra("itemDetails", currentItem.id)
            holder.itemView.context.startActivity(intent)

            holder.itemEditBtn.setImageResource(R.drawable.ic_baseline_edit_24)

        }

        holder.itemDeleteBtn.setOnClickListener {
            holder.itemDeleteBtn.setImageResource(R.drawable.ic_baseline_delete_25)
            ref.child(currentItem.id).removeValue()
            holder.itemDeleteBtn.setImageResource(R.drawable.ic_baseline_delete_24)


        }
        holder.itemView.setOnClickListener {
            // To display an image in a afull screen

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}