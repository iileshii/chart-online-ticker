package mobi.jedi.chartappsample.view.adapter

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import mobi.jedi.chartappsample.repository.TickerData
import java.util.*

class DataChartAdapter(private val view: LineChart) {

    private val items = LinkedList<TickerData>()
    private val capacity = 10

    fun addItem(ticker: TickerData?) {
        if (ticker == null) {
            return
        }

        items.add(ticker)

        if (items.size > capacity) {
            items.removeFirst()
        } else {
            // just to make more smoothie scaling on X-axis
            while (items.size < capacity) {
                items.addFirst(items.first)
            }
        }

        updateEntires()
    }

    private fun updateEntires() {
        val entries = LinkedList<Entry>()

        for (i in 0 until items.size) {
            entries.add(Entry(i.toFloat(), items[i].LAST_PRICE.toFloat()))
        }

        val dataSet = LineDataSet(entries, "BTC -> USD")

        val lineData = LineData(dataSet)

        lineData.dataSetLabels

        view.data = lineData
        view.invalidate()
    }


}