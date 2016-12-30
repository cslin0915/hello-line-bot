package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package : hello
 * --
 * Description :
 * Author : jasonlin
 * Date : 2016/12/30
 */
@RestController
public class HelloControler {
    @RequestMapping("/")
    public String index() {
        Greeter greeter = new Greeter();
        return greeter.sayHello();
    }
}
