package iberoplast.pe.gespro.ui.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import iberoplast.pe.gespro.model.Countrie

class CountryAdapter(
    context: Context,
    resource: Int,
    countries: List<Countrie>
) : ArrayAdapter<Countrie>(context, resource, countries) {

    private val originalCountries: List<Countrie> = ArrayList(countries)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredCountries = ArrayList<Countrie>()

                if (constraint.isNullOrEmpty()) {
                    filteredCountries.addAll(originalCountries)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()

                    for (country in originalCountries) {
                        if (country.name.toLowerCase().contains(filterPattern)) {
                            filteredCountries.add(country)
                        }
                    }
                }

                results.values = filteredCountries
                results.count = filteredCountries.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll(results?.values as List<Countrie>)
                notifyDataSetChanged()
            }
        }
    }
}