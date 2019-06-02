package mobi.jedi.chartappsample.repository

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import mobi.jedi.chartappsample.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class NetworkManager private constructor() : INetworkManager {

    override val api: IApi

    companion object {
        private const val WEB_SOCKET_URL = "wss://api.bitfinex.com/ws"
        val instance: INetworkManager by lazy { NetworkManager() }
    }

    init {

        val logLevel = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
            .build()

        val scarlet = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(WEB_SOCKET_URL))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

        api = scarlet.create(IApi::class.java)
    }

}