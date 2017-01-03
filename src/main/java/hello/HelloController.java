package hello;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import java.io.IOException;

/**
 * Package : hello
 * --
 * Description :
 * Author : jasonlin
 * Date : 2016/12/30
 */
@LineMessageHandler
@Slf4j
@RestController
public class HelloController {
    private final String channelAccessToken = "hTjsxjjyaox3OXtKQxydgVHW+txEdRu23nVSJ010ATaXUGNBkTAb+3Z1UaA3q+CiaMqz+YipoXVuSk0vnvBlJcAXAvUCMV/avBCX64Nz1IwiCKosNJhZTaVYKW0ujzaHCCtik7/Bt/zGViS2i+WltwdB04t89/1O/w1cDnyilFU=";

    @RequestMapping("/")
    public String index() {
        Greeter greeter = new Greeter();
        return greeter.sayHello();
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        log.debug("Event: " + event);
        return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        log.debug("Event: " + event);
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="targetId") String targetId, @RequestParam(value="message", defaultValue = "Hello!") String message) {
        TextMessage textMessage = new TextMessage(message);
        PushMessage pushMessage = new PushMessage(
                targetId,
                textMessage
        );

        Response<BotApiResponse> response =
                null;
        try {
            response = LineMessagingServiceBuilder
                    .create(channelAccessToken)
                    .build()
                    .pushMessage(pushMessage)
                    .execute();
            return "Success!";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fail!";
    }
}
