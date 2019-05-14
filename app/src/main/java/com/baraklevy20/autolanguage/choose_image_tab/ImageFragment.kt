package com.baraklevy20.autolanguage.choose_image_tab

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.*
import com.android.volley.toolbox.*
import android.support.v7.widget.StaggeredGridLayoutManager
import com.baraklevy20.autolanguage.R


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ImageFragment.OnListFragmentInteractionListener] interface.
 */
class ImageFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 2

    private var listener: OnImageInteractionListener? = null

    private val images: MutableList<Image> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_tab, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                setHasFixedSize(false);
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
//                    else -> StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL)
                }

                adapter = ImageAdapter(
                    images,
                    listener
                )

                if (images.isEmpty()) {
                    val phrase = "Banane"
                    val count = 50
                    val offset = 100
                    val url =
                        "https://api.qwant.com/api/search/images?count=$count&offset=$offset&q=$phrase&t=images&safesearch=1&uiv=4"

                    Volley.newRequestQueue(context).add(JsonObjectRequest(Request.Method.GET, url, null,
                        Response.Listener { response ->
                            val items = response.getJSONObject("data").getJSONObject("result").getJSONArray("items")

                            for (i in 0 until items.length()) {
                                with(items.getJSONObject(i)) {
                                    images.add(
                                        Image(
                                            getString("title"),
                                            "https://" + getString("media_preview").substring(2),
                                            getInt("width"),
                                            null
                                        )
                                    )
                                }
                            }

                            (adapter as ImageAdapter).notifyDataSetChanged()
                        },
                        Response.ErrorListener { error ->

                        }
                    ))
                }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnImageInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnImageInteractionListener {
        fun onImageInteraction(image: Image)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
