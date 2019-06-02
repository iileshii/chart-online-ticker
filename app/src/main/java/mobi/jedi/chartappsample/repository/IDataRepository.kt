package mobi.jedi.chartappsample.repository

interface IDataRepository {

    fun subscribe(pairCode: String)

    fun unsubscribe()

    fun observeTickerOnUiThread(onReceive: (ticker: TickerData?) -> Unit)
}
