package com.kansha.redirectNow.presentation.tutorial

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.kansha.redirectNow.R
import com.kansha.redirectNow.data.model.TutorialDetails
import com.kansha.redirectNow.databinding.ActivityTutorialBinding
import com.kansha.redirectNow.internal.extension.gone
import com.kansha.redirectNow.internal.extension.visible
import com.kansha.redirectNow.presentation.home.HomeActivity

class TutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialBinding
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if (isFirstTime) {
            binding = ActivityTutorialBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupPageView()
            setupViewPagerListener()
            setupButtonSkip()

            val redirectButton = binding.redirectButton
            redirectButton.setOnClickListener {
                if (isLastPage) {
                    redirectToHomeScreen()
                } else {
                    showNextPage()
                }
            }

            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTime", false)
            editor.apply()
        } else {
            redirectToHomeScreen()
        }
    }

    private fun setupPageView() {
        val tutorialText = listOf(
            TutorialDetails(R.drawable.start, "Na tela inicial do aplicativo, clique no canto inferior direito para adicionar um novo contato."),
            TutorialDetails(
                R.drawable.name_and_phone,
                "Preencha os campos \"Nome\" e \"Telefone\". É fundamental incluir o DDD, pois sem ele o contato não será localizado corretamente."
            ),
            TutorialDetails(
                R.drawable.go_whats,
                "Após preenchidos, aparecerá uma confirmação se gostaria de ir para o WhatsApp ou somente salvar o contato."
            ),
            TutorialDetails(
                R.drawable.short_click,
                "Para abrir a conversa com o contato, basta tocar uma vez no contato e confirmar se deseja ir para o WhatsApp."
            ),
            TutorialDetails(
                R.drawable.long_click,
                "Para editar o contato ou removê-lo da lista, mantenha pressionado o contato por alguns instantes."
            )
        )

        val adapter = ViewPagerAdapter(this, tutorialText)
        binding.viewPager.adapter = adapter

        val wormDotsIndicator = binding.wormDotsIndicator
        wormDotsIndicator.attachTo(binding.viewPager)
    }

    private fun setupViewPagerListener() {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Esta função é chamada quando uma página está sendo rolada.
            }

            override fun onPageSelected(position: Int) {
                // Função chamada quando uma nova página é selecionada

                val lastPageIndex = binding.viewPager.adapter?.count?.minus(1) ?: 0
                val redirectButton = binding.redirectButton
                isLastPage = position == lastPageIndex

                if (binding.viewPager.currentItem == lastPageIndex) {
                    redirectButton.visible()
                } else {
                    redirectButton.gone()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Esta função é chamada quando o estado de rolagem do ViewPager é alterado.
            }
        })
    }

    private fun redirectToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showNextPage() {
        val viewPager = binding.viewPager
        val nextPage = viewPager.currentItem + 1
        viewPager.setCurrentItem(nextPage, true)
    }

    private fun setupButtonSkip() {
        binding.buttonSkip.setOnClickListener {
            redirectToHomeScreen()
        }
    }
}