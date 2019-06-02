package mobi.jedi.chartappsample.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import mobi.jedi.chartappsample.R
import mobi.jedi.chartappsample.repository.DataRepository
import mobi.jedi.chartappsample.repository.TickerData
import mobi.jedi.chartappsample.view.adapter.DataChartAdapter

class MainActivity : AppCompatActivity() {

    private val dataRepository = DataRepository.instance
    private val observingPairCode = "BTCUSD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataChartAdapter = DataChartAdapter(chart)

        dataRepository.subscribe(observingPairCode)

        dataRepository.observeTickerOnUiThread { addItem(dataChartAdapter, it) }

    }

    private fun addItem(adapter: DataChartAdapter, ticker: TickerData?) {
        adapter.addItem(ticker)
    }

    override fun onDestroy() {
        super.onDestroy()
        dataRepository.unsubscribe()
    }
}




