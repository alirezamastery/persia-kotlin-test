package com.persia.test.tools.ui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter


class CustomArrayAdapter<T>(
    context: Context,
    resource: Int,
    objects: List<T>
) : ArrayAdapter<T>(context, resource, objects) {

    private val filter = CustomFilter<T>(objects)

    // private val customFilter = ArrayFi
    override fun getFilter(): Filter {

        return filter
    }

    class CustomFilter<G> constructor(private val items: List<G>) : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = items
            result.count = items.size
            return result;
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

        }

    }
}