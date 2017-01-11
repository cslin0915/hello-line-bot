package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Package : hello
 * --
 * Description :
 * Author : jasonlin
 * Date : 2017/1/11
 */
@Component
@PropertySource("classpath:line-bot.properties")
public class LineBotProperties {
    @Value("${line.bot.channelToken}")
    private String channelToken;

    @Value("${line.bot.channelSecret}")
    private String channelSecret;

    //getters and setters
}
