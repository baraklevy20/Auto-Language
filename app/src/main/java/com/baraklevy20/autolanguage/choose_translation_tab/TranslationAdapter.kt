package com.baraklevy20.autolanguage.choose_translation_tab

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.baraklevy20.autolanguage.R



import kotlinx.android.synthetic.main.listitem_translation.view.*


class TranslationAdapter (
    private val mValues: List<Translation>,
    private val mListener: TranslationFragment.OnTranslationInteractionListener?
) : RecyclerView.Adapter<TranslationAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val translation = v.tag as Translation
            mListener?.onTranslationInteraction(translation)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_translation, parent, false)
        return ViewHolder(view)
    }

    private fun textToHtml(html: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(html)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTranslatedTermView.text = item.translatedWord

        var exampleSentencesText = ""

        for (i in 0 until item.sourceSentences.size - 1) {
            exampleSentencesText += "${item.sourceSentences[i]}<br>${item.targetSentences[i]}<br><br>"
        }

        exampleSentencesText += "${item.sourceSentences[item.sourceSentences.size - 1]}<br>" +
                "${item.targetSentences[item.sourceSentences.size - 1]}<br>"

        holder.mExampleSentencesView.text = textToHtml(exampleSentencesText)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTranslatedTermView: TextView = mView.translated_term
        val mExampleSentencesView: TextView = mView.example_sentences
    }
}
