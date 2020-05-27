package com.guoj.gallery.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.guoj.gallery.R
import com.guoj.gallery.adpter.GalleryAdapter
import kotlinx.android.synthetic.main.gallery_fragment.*

class GalleryFragment : Fragment() {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        val galleryAdapter=GalleryAdapter()
        recyclerView.apply {
            adapter=galleryAdapter
            layoutManager=GridLayoutManager(requireContext(),2)
        }
        galleryViewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GalleryViewModel::class.java)
        galleryViewModel.photoListLive.observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
            swiperRefreshLayout.isRefreshing=false
        })
        galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()

        swiperRefreshLayout.setOnRefreshListener {
            galleryViewModel.fetchData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_refresh->{
                swiperRefreshLayout.isRefreshing = true
//                Handler().postDelayed(Runnable {galleryViewModel.fetchData() },1000)
                galleryViewModel.fetchData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
