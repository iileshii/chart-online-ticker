package mobi.jedi.chartappsample.repository

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.tinder.scarlet.WebSocket
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DataRepository private constructor() : IDataRepository {

    private val api = NetworkManager.instance.api

    private var disposables: CompositeDisposable = CompositeDisposable()

    companion object {
        private const val LOG_TAG = "fatality"
        val instance: IDataRepository by lazy { DataRepository() }
    }

    override fun subscribe(pairCode: String) {
        disposables.add(
            api.observeWebSocketEvent()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it is WebSocket.Event.OnConnectionOpened<*>) {
                        api.subscribe(SubscribeAction(pair = pairCode))
                    }
                }, { error ->
                    Log.e(LOG_TAG, "Error while observing socket ${error.cause}")
                })
        )
    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun observeTickerOnUiThread(onReceive: (ticker: TickerData?) -> Unit) {
        var lastData: TickerData? = null

        disposables.add(api.observeTicker()
            .filter(::filterArray)
            .map(::mapTicker)
            .map { ticker -> fillHBbyPrevious(ticker, lastData) }
            .filter(::filterNonNull)
            .doOnNext { lastData = it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onReceive(it) }, { throwable -> Log.e(LOG_TAG, throwable.toString(), throwable) }
            ))
    }

    private fun filterArray(jsonElement: JsonElement): Boolean = jsonElement.isJsonArray

    private fun filterNonNull(ticker: TickerData?): Boolean = ticker != null

    private fun mapTicker(jsonElement: JsonElement): Ticker {
        if (jsonElement.isJsonArray.not()) {
            throw IllegalArgumentException("jsonElement must be an array")
        }

        val array = jsonElement.asJsonArray

        return if (array.size() < 10) {
            mapHB(array)
        } else {
            mapTickerData(array)
        }
    }

    private fun mapTickerData(array: JsonArray) =
        TickerData(
            channelId = array[TickerData.CHANNEL_ID_INDEX].asInt,
            BID = array[TickerData.BID].asDouble,
            BID_SIZE = array[TickerData.BID_SIZE].asDouble,
            ASK = array[TickerData.ASK].asDouble,
            ASK_SIZE = array[TickerData.ASK_SIZE].asDouble,
            DAILY_CHANGE = array[TickerData.DAILY_CHANGE].asDouble,
            DAILY_CHANGE_PERC = array[TickerData.DAILY_CHANGE_PERC].asDouble,
            LAST_PRICE = array[TickerData.LAST_PRICE].asDouble,
            VOLUME = array[TickerData.VOLUME].asDouble,
            LOW = array[TickerData.LOW].asDouble,
            HIGH = array[TickerData.HIGH].asDouble
        )

    private fun mapHB(array: JsonArray) =
        HB(array[HB.CHANNEL_ID_INDEX].asInt)

    private fun fillHBbyPrevious(ticker: Ticker, previous: TickerData?): TickerData? {
        return when (ticker) {
            is TickerData -> ticker
            else -> previous
        }
    }

}