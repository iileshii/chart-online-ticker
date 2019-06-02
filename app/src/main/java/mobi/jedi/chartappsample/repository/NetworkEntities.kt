package mobi.jedi.chartappsample.repository

data class SubscribeAction(
    val pair: String,
    val event: String = "subscribe",
    val channel: String = "ticker"
)

abstract class Ticker {
    abstract val channelId: Int
}

data class HB(override val channelId: Int) : Ticker() {
    companion object {
        const val CHANNEL_ID_INDEX = 0
    }
}

data class TickerData(
    override val channelId: Int,
    val BID: Double,
    val BID_SIZE: Double,
    val ASK: Double,
    val ASK_SIZE: Double,
    val DAILY_CHANGE: Double,
    val DAILY_CHANGE_PERC: Double,
    val LAST_PRICE: Double,
    val VOLUME: Double,
    val HIGH: Double,
    val LOW: Double
) : Ticker() {
    companion object {
        const val CHANNEL_ID_INDEX = 0
        const val BID = 1
        const val BID_SIZE = 2
        const val ASK = 3
        const val ASK_SIZE = 4
        const val DAILY_CHANGE = 5
        const val DAILY_CHANGE_PERC = 6
        const val LAST_PRICE = 7
        const val VOLUME = 8
        const val HIGH = 9
        const val LOW = 10
    }
}