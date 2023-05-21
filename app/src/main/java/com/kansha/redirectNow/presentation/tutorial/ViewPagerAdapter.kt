package com.kansha.redirectNow.presentation.tutorial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.kansha.redirectNow.R
import com.kansha.redirectNow.data.model.TutorialDetails

class ViewPagerAdapter(private val context: Context, private val dados: List<TutorialDetails>) : PagerAdapter() {

    override fun getCount(): Int {
        return dados.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_page_tutorial, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val textView = view.findViewById<TextView>(R.id.textView)

        val item = dados[position]
        imageView.setImageResource(item.imagem)
        textView.text = item.texto

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
