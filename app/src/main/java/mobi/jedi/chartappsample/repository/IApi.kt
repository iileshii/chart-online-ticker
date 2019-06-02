package mobi.jedi.chartappsample.repository

import com.google.gson.JsonElement
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface IApi {
    @Send
    fun subscribe(action: SubscribeAction)

    @Receive
    fun observeTicker(): Flowable<JsonElement>

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

}
