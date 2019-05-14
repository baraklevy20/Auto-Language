package com.baraklevy20.autolanguage.choose_image_tab

import android.graphics.Bitmap
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.baraklevy20.autolanguage.R
import kotlinx.android.synthetic.main.listitem_image.view.*
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley


class ImageAdapter (
    private val mValues: List<Image>,
    private val mListener: ImageFragment.OnImageInteractionListener?
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val image = v.tag as Image
            mListener?.onImageInteraction(image)
        }
    }

    private fun textToHtml(html: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(html)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        if (item.bitmap == null) {
            Volley.newRequestQueue(holder.mView.context).add(ImageRequest(
                item.imageLink,
                Response.Listener<Bitmap> { response ->
                    item.bitmap = response
                    holder.mImage.setImageBitmap(response)
                },
                0,
                0,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.RGB_565,
                Response.ErrorListener {
                }
            ))
        }
        else {
            holder.mImage.setImageBitmap(item.bitmap)
        }

        holder.mTitle.text = textToHtml(item.title)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImage: ImageView = mView.image
        val mTitle: TextView = mView.title
    }
}
