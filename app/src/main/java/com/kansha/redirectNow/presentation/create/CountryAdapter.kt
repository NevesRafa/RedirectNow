package com.kansha.redirectNow.presentation.create

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kansha.redirectNow.R
import com.kansha.redirectNow.data.model.CountryDetails


class CountryAdapter(
    context: Context,
    private val resource: Int,
    private val countries: List<CountryDetails>,
    private val filteredList: MutableList<CountryDetails> = mutableListOf()
) : ArrayAdapter<CountryDetails>(context, resource, countries) {

    init {
        filteredList.addAll(countries)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val countryItem = getItem(position)
        val countryCodeTextView: TextView = view.findViewById(R.id.dropdown_code)
        val countryFlagImageView: ImageView = view.findViewById(R.id.dropdown_flag)

        countryCodeTextView.text = countryItem.countryCode

        Glide.with(context)
            .load("https://flagcdn.com/w320/${countryItem.flagCode}.png")
            .into(countryFlagImageView)

        return view
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getItem(position: Int): CountryDetails {
        return filteredList[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun convertResultToString(resultValue: Any?): String? {
                // converte um CountryDetails em String pra mostrar no TextView
                return (resultValue as? CountryDetails)?.countryCode
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // se não tiver nenhum texto digitado, não mostra nenhuma sugestão
                if (constraint.isNullOrEmpty()) {
                    return filterResults
                }

                // pega todos os countries que comecem com o que foi digitado
                val suggestions = countries.filter {
                    it.countryCode.startsWith(constraint, ignoreCase = true)
                }

                // aplica e retorna
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // limpa a lista filtrada
                filteredList.clear()

                // se tiver resultados a lista filtrada são os resultados
                if (results != null && results.count > 0) {
                    val parsedResults = (results.values as? List<CountryDetails>) ?: return
                    filteredList.addAll(parsedResults)
                    notifyDataSetChanged()
                    return
                }

                // se não tiver constraint, a lista filtrada são todos os countries
                if (constraint == null) {
                    filteredList.addAll(countries)
                    notifyDataSetInvalidated()
                    return
                }
            }
        }
    }
}