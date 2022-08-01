package com.example.myapplication2.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.R
import com.example.myapplication2.ShopItemsActivity
import com.example.myapplication2.model.Shop

class Shops_RV_Adapter(private val shopsList: ArrayList<Shop>) :
    RecyclerView.Adapter<Shops_RV_Adapter.ViewHolder>() {

    //Static variable to use it from different classes
    companion object {
        @JvmStatic
        var shop_name: String = ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.shop_item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentShop = shopsList[position]
        holder.shopName.setText(currentShop.name)
        holder.shopAddress.setText(currentShop.address)
        holder.shopPhone.setText(currentShop.phone)

        holder.itemView.setOnClickListener {

            //Display shop categories and items when pressed on the shop card info
            val intent = Intent(holder.itemView.context, ShopItemsActivity::class.java)
            shop_name = currentShop.name
            holder.itemView.context.startActivity(intent)

        }
        //Open a phone dial when pressed on phone icon
        holder.shopPhoneIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+97" + currentShop.phone))
            holder.itemView.context.startActivity(intent)
        }

        //Open a google map when pressed on check in icon
        holder.shopLocationIcon.setOnClickListener {
            var gmmIntentUri = Uri.parse("geo:0,0?q=");
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            holder.itemView.context.startActivity(mapIntent);
        }

    }

    override fun getItemCount(): Int {
        return shopsList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val shopName: TextView = itemView.findViewById(R.id.shop_view_name)
        val shopAddress: TextView = itemView.findViewById(R.id.shop_view_address)
        val shopPhone: TextView = itemView.findViewById(R.id.shop_phone)
        val shopLocationIcon: ImageView = itemView.findViewById(R.id.shop_view_location)
        val shopPhoneIcon: ImageView = itemView.findViewById(R.id.shop_view_phone)


    }


}