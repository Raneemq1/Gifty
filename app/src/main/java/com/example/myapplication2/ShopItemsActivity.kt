package com.example.myapplication2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.model.Category
import com.example.myapplication2.model.Item
import com.example.myapplication2.model.Shop
import com.google.firebase.database.*

class ShopItemsActivity : AppCompatActivity() {
    lateinit var text: TextView
    private lateinit var shopName: String
    private lateinit var rv_category: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var rv_item: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    var type: String = ""
    var email: String = ""
    val ref = FirebaseDatabase.getInstance().getReference("Shops")
    val ref1 = FirebaseDatabase.getInstance().getReference("Items")
    var categoryDisplayList = ArrayList<Category>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        shopName = Shops_RV_Adapter.shop_name
        setContentView(R.layout.activity_shop_items)
        text = findViewById(R.id.display_shopName)
        text.setText(shopName)
        type = intent.getStringExtra("category").toString()

        prepareData()


        rv_category = findViewById(R.id.rv_category)
        rv_category.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_category.setHasFixedSize(true)

        prepareItem()

        rv_item = findViewById(R.id.item_rv)
        rv_item.layoutManager = LinearLayoutManager(this)
        rv_item.setHasFixedSize(true)


    }

    private fun prepareData() {
        val category = ArrayList<Category>()
        var category_list = ArrayList<String>()
        category.add(Category("Accessories", R.drawable.a))
        category.add(Category("Antiques", R.drawable.b))
        category.add(Category("Perfume", R.drawable.c))
        category.add(Category("Books", R.drawable.d))
        category.add(Category("Electronics", R.drawable.e))
        category.add(Category("Men Clothe", R.drawable.f))
        category.add(Category("Women Clothe", R.drawable.g))
        category.add(Category("Children Clothe", R.drawable.h))
        category.add(Category("Toys", R.drawable.i))
        category.add(Category("Candles", R.drawable.j))
        category.add(Category("Painting", R.drawable.l))

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p.exists()) { //Check that database isn't null
                    category_list.clear()
                    for (u in p.children) {
                        val shop = u.getValue(Shop::class.java)
                        if (shop?.name.equals(shopName)) {
                            category_list = shop?.categories!!

                        }

                    }
                    for (i in 0..category_list.size - 1) {
                        for (j in 0..category.size - 1) {
                            if (category_list[i].equals(category[j].categoryName)) {
                                categoryDisplayList.add(category[j])

                            }
                        }
                    }
                    categoryAdapter = CategoryAdapter(categoryDisplayList)
                    rv_category.adapter = categoryAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun getShopNameFromEmail() {
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p.exists()) { //Check that database isn't null
                    for (u in p.children) {
                        val shop = u.getValue(Shop::class.java)
                        if (shop?.name.equals(shopName)) {
                            email = shop?.email!!

                        }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun prepareItem() {
        var item = ArrayList<Item>()
        var typelist = ArrayList<Item>()
        var returnlist = ArrayList<Item>()
        getShopNameFromEmail()
        ref1.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p: DataSnapshot) {
                if (p.exists()) { //Check that database isn't null
                    //category_list.clear()
                    for (u in p.children) {
                        val product = u.getValue(Item::class.java)
                        if (product?.email.equals(email)) {
                            item.add(
                                Item(
                                    product?.id.toString(),
                                    product?.name.toString(),
                                    Integer.parseInt(product?.price.toString()),
                                    Integer.parseInt(product?.quantity.toString()),
                                    product?.category.toString(),
                                    product?.description.toString(),
                                    product?.pathImage.toString(),
                                    product?.email.toString()
                                )
                            )
                        }

                    }
                    for (i in 0..(item.size - 1)) {
                        if (type.equals(item.get(i).category)) {
                            typelist.add(item.get(i))
                        }

                    }

                    if (!type.equals("null")) {
                        returnlist.addAll(typelist)

                    } else {

                        returnlist.addAll(item)
                    }
                    itemAdapter = ItemAdapter(returnlist)
                    rv_item.adapter = itemAdapter
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}