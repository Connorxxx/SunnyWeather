package com.sunnyweather.android.ui.place

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.StartActivityIntent
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {

    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            startActivity(StartActivityIntent.startWeatherIntent(place))
            activity?.finish()
            return
        }
        if (searchPlaceEdit.text.isEmpty()) {
            val manager = searchPlaceEdit.context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            searchPlaceEdit.requestFocus()
            manager.showSoftInput(view, 0)
        }
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(
            this,
            viewModel.placeList
        )
        recyclerView.adapter = adapter
        searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)  //发起搜索请求
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {
            val places = it.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })
    }

}