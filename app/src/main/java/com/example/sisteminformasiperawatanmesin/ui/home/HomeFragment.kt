package com.example.sisteminformasiperawatanmesin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sisteminformasiperawatanmesin.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import com.example.sisteminformasiperawatanmesin.jadwal
import android.widget.Toast
import com.example.sisteminformasiperawatanmesin.R

class HomeFragment : Fragment() {

    lateinit var db : DatabaseReference
    lateinit var adapter : ListAdapter
    var allJdw = ArrayList<HashMap<String,Any>>()
    var jdw = jadwal()
    var hm = HashMap<String,Any>()

    private var _binding: FragmentHomeBinding? = null
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

    override fun onStart() {
        super.onStart()
        db = FirebaseDatabase.getInstance().getReference("Jadwal")
        showData()
    }

    fun showData() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allJdw.clear()
                for (dataSnapshot in snapshot.children) {
                    val jadwalData = dataSnapshot.getValue(jadwal::class.java)

                    if (jadwalData != null) {
                        val hm = HashMap<String, Any>()
                        hm["komponen"] = jadwalData.komponen ?: ""
                        hm["mesin"] = jadwalData.mesin ?: ""
                        hm["tanggal"] = jadwalData.tanggal ?: ""
                        hm["user"] = jadwalData.user ?: ""

                        allJdw.add(hm)
                    }
                }

                adapter = SimpleAdapter(
                    context,
                    allJdw,
                    R.layout.row_data,
                    arrayOf("komponen", "mesin", "tanggal", "user"),
                    intArrayOf(R.id.txKomponen, R.id.txMesin, R.id.txTgl, R.id.txUser)
                )
                binding.ListViewJadwal.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}