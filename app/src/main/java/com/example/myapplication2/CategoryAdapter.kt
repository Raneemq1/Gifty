package com.example.myapplication2

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.model.Category

class CategoryAdapter(private val catgoryList: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textCategoryName: TextView = view.findViewById<TextView>(R.id.category_name)
        val categoryImage: ImageView = view.findViewById<ImageView>(R.id.category_img)
        val linearLayout: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.linearLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)

        return ViewHolder(itemView)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory: String
        currentCategory = catgoryList[position].categoryName
        holder.textCategoryName.setText(currentCategory)
        holder.categoryImage.setImageResource(catgoryList[position].categoryImg)
        val activity = holder.itemView.context as ShopItemsActivity

        holder.textCategoryName.setOnClickListener {
            holder.linearLayout.setBackgroundColor(R.color.pressedColor)

            val intent = Intent(holder.itemView.context, ShopItemsActivity::class.java)
            intent.putExtra("category", currentCategory)
            holder.itemView.context.startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )

        }
    }

    override fun getItemCount(): Int {
        return catgoryList.size
    }
}
