package com.nasser.snapshots.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nasser.snapshots.R
import com.nasser.snapshots.databinding.FragmentHomeBinding
import com.nasser.snapshots.databinding.ItemSnapshotsBinding
import com.nasser.snapshots.entity.Snapshots

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Snapshots, SnapshotHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Rama snapshots en la base de datos de firebase
        val query = FirebaseDatabase.getInstance().reference.child("snapshots")

        val options = FirebaseRecyclerOptions.Builder<Snapshots>()
            .setQuery(query, Snapshots::class.java).build()

        mFirebaseAdapter = object: FirebaseRecyclerAdapter<Snapshots, SnapshotHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_snapshots, parent, false)

                return SnapshotHolder(view)
            }

            override fun onBindViewHolder(holder: SnapshotHolder, position: Int, model: Snapshots) {
                val snapshot = getItem(position)

                with(holder) {
                    setListener(snapshot)

                    binding.textViewTittle.text = snapshot.tittle
                    Glide.with(mContext)
                        .load(snapshot.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.imagePhoto)
                }
                
            }

            override fun onDataChanged() {
                super.onDataChanged()

                mBinding.progressBarr.visibility = View.GONE
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        mLayoutManager = LinearLayoutManager(context)

        mBinding.recylerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    //Configuramos el viewholder del recyclerview en el fragmmento gracias a la libreria de firebase ui
    inner class SnapshotHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemSnapshotsBinding.bind(view)

        fun setListener(snapshot: Snapshots) {

        }
    }
}