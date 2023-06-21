package com.kansha.redirectNow.presentation.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.kansha.redirectNow.data.model.TutorialDetails
import com.kansha.redirectNow.databinding.ItemPageTutorialBinding

class TutorialViewPager(
    private val tutorialPages: List<TutorialDetails>
) : PagerAdapter() {

    override fun getCount(): Int {
        return tutorialPages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemPageTutorialBinding.inflate(
            LayoutInflater.from(container.context),
            container,
            false
        )

        val item = tutorialPages[position]
        binding.imageView.setImageResource(item.imagem)
        binding.textView.text = item.texto
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
