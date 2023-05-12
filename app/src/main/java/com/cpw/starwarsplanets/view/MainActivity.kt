package com.cpw.starwarsplanets.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpw.starwarsplanets.databinding.ActivityMainBinding
import com.cpw.starwarsplanets.model.PlanetData
import com.cpw.starwarsplanets.viewmodel.PlanetsAdapter
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mDataBase: DatabaseReference
    private lateinit var mAdapter: PlanetsAdapter
    private lateinit var planetList: ArrayList<PlanetData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planetList = ArrayList()
        mAdapter = PlanetsAdapter(this, planetList)

        binding.recyclerPlanets.layoutManager = LinearLayoutManager(this)
        binding.recyclerPlanets.setHasFixedSize(true)
        binding.recyclerPlanets.adapter = mAdapter

        getPlanetData()
    }

    private fun getPlanetData() {
        mDataBase = FirebaseDatabase.getInstance().getReference("Planets")
        mDataBase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for(planetSnapshot in snapshot.children) {
                        val planet = planetSnapshot.getValue(PlanetData::class.java)
                        planetList.add(planet!!)
                    }
                    binding.recyclerPlanets.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}