package com.baraklevy20.autolanguage

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.*
import com.android.volley.toolbox.*
import android.support.v7.widget.StaggeredGridLayoutManager



/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TranslationFragment.OnListFragmentInteractionListener] interface.
 */
class TranslationFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 2

    private var listener: OnListFragmentInteractionListener? = null

    private val translations: MutableList<Translation> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_translation_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                setHasFixedSize(false);
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
                    else -> StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL)
                }


                val phrase = "pretty"
                val from = "en"
                val dest = "de"
                val url = "https://context.reverso.net/bst-query-service?source_text=$phrase&source_lang=$from&target_lang=$dest&json=1&nrows=10"

                val translationDictionary = sortedMapOf<String, Translation>()

                val regex = """.*<b>(.*)</b>.*""".toRegex()
                Volley.newRequestQueue(context).add(JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        val list = response.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            val currentTranslation = list.getJSONObject(i)
                            val sourceSentence = currentTranslation.getString("s_text")
                                .replace("<em>", "<b>")
                                .replace("</em>","</b>")
                            val targetSentence = currentTranslation.getString("t_text")
                                .replace("<em>", "<b>")
                                .replace("</em>","</b>")

                            val translatedTerm = regex.find(targetSentence)!!.groupValues[1]

                            if (!translationDictionary.containsKey(translatedTerm)) {
                                translationDictionary[translatedTerm] =
                                    Translation(translatedTerm, mutableListOf(), mutableListOf())
                            }

                            translationDictionary[translatedTerm]!!.sourceSentences.add(sourceSentence)
                            translationDictionary[translatedTerm]!!.targetSentences.add(targetSentence)
                        }

                        for (key in translationDictionary.keys) {
                            translations.add(translationDictionary[key]!!)
                        }

                        adapter = MyTranslationRecyclerViewAdapter(translations, listener)
                    },
                    Response.ErrorListener { error ->
                        // TODO: Handle error
                    }
                ))
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
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
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(translation: Translation)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            TranslationFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
