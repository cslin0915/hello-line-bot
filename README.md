Hello Line Bot
==============

Survey CheckList
----------------

-	[x] 從零開始, 建立 Gradle 的 IntelljJ 專案, e.g., [Building Java Projects with Gradle](https://spring.io/guides/gs/gradle/)
-	[x] 從零開始, 建立 Spring Boot 的 IntelljJ 專案, e.g., [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
-	[x] 從零開始, 建立簡易的 Line Bot, 只做訊息的回應 [Ref.](https://github.com/line/line-bot-sdk-java/tree/master/sample-spring-boot-echo), 並佈署到 Heroku [Ref.](https://devcenter.heroku.com/articles/getting-started-with-gradle-on-heroku)

	> Heroku 的 Settings > Config Variables 中需要有 LINE_BOT_CHANNEL_SECRET 與 LINE_BOT_CHANNEL_TOKEN 這兩個變數的設定

-	[x] 使用 ngrok 將 Line Bot 的執行佈署到本機

	> 可使用 `./gradlew bootRun -Dline.bot.channelToken=YOUR_CHANNEL_TOKEN -Dline.bot.channelSecret=YOUR_CHANNEL_SECRET` 執行程式

-	[x] 測試 IntelljJ 的 Debug 模式是否可直接使用在 Line Bot 專案上? 可以

-	[x] 測試 ngrok 提供的[封包檢視功能](http://localhost:4040/inspect/http)

	> 目前只看到由外部接收到的 Request 與對應的 Response 封包, 並沒看到自己對 Line API Server 發出的封包

-	[x] 查看執行 Line Bot 時, IntelliJ 的 Console 介面顯示的訊息有些什麼意義?

	> 有紀錄對 Line API 操作的 Request 與 Response 封包訊息
	>
	> Request 封包的訊息範例 : `--> POST https://api.line.me/v2/bot/message/reply http/1.1 ...... --> END POST (285-byte body)`
	>
	> Response 封包的訊息範例 : `<-- 200 OK https://api.line.me/v2/bot/message/reply (80ms) ...... <-- END HTTP (2-byte body)`

-	[x] 測試 Group 裡是否也會 ECHO 其他使用者留的訊息? 會, 但並無法知道使用者的 ID

-	[x] 測試 Line Bot 如何藉由 RESTful API (GET) 主動送訊息給使用者

-	[x] 測試如何將 log4j 或其他 logging framework 導入 Spring Boot 的程式中

-	[x] 測試 Line Bot 如何藉由 RESTful API (POST) 主動送訊息給使用者? 使用 Spring Boot 建立 RESTful Web Service 接收 **訊息** 後, 再用 [Push message API](https://devdocs.line.me/en/?java#push-message) 丟訊息即可

-	[x] Bot 加入 Group 後, 拿到的 Source 會是 Group 類型嗎? YES

-	[x] 重構 HelloWorld 的 Line Bot, 當被加入群組時, 會顯示群組 ID

-	[x] 測試 Messaging API 的各項功能

	> RequestMapping 的 Annotation 可同時支援 GET 與 POST, 若使用 postman 做測試時, 除了 Body 使用 form-data 外, Header 也要加上 X-Application-Context=application

-	[x] 測試其他 Messaging API 並重構目前的 HelloWorld 程式

Our Scenario
------------

1.	Let a bot join a group
2.	Let the bot send any message to the group

### How to Achieve that Scenario

1.	[Webhooks](https://devdocs.line.me/en/?java#webhook-event-object)

	-	When an event is triggered, an HTTPS POST request is sent to the webhook URL.
	-	An Event : a user adds your account or sends a message
	-	The Webhook URL : e.g.,  
		https://&lt;YOUR_HEROKU_APP_NAME&gt;.herokuapp.com/callback
		-	A Sample Event:

	```json
	{
	  "events": [
	      {
	        "replyToken": "nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
	        "type": "message",
	        "timestamp": 1462629479859,
	        "source": {
	             "type": "user",
	             "userId": "U206d25c2ea6bd87c17655609a1c37cb8"
	         },
	         "message": {
	             "id": "325708",
	             "type": "text",
	             "text": "Hello, world"
	          }
	      }
	  ]
	}
	```

	-	A Join Event : your account joins a group or talk room.
		-	It conains the group's ID **source.groupId**.
		-	A Sample Event:

	```json
	{
	  "replyToken": "nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
	  "type": "join",
	  "timestamp": 1462629479859,
	  "source": {
	    "type": "group",
	    "groupId": "cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
	  }
	}
	```

2.	[Push message](https://devdocs.line.me/en/?java#push-message)

	-	Send messages to users, groups, and rooms at any time.
	-	Sample Codes :
		-	**&lt;to&gt;** : the ID returned via the webhook event of the source user, group, or room as the ID of the receiver
		-	**&lt;textMessage&gt;** : can be any of [send message objects](https://devdocs.line.me/en/?java#send-message-object)

	```java
	  TextMessage textMessage = new TextMessage("hello");
	  PushMessage pushMessage = new PushMessage(
	        "<to>",
	        textMessage
	  );


	  Response<BotApiResponse> response =
	        LineMessagingServiceBuilder
	                .create("<channel access token>")
	                .build()
	                .pushMessage(pushMessage)
	                .execute();
	  System.out.println(response.code() + " " + response.message());
	```

Line Official Sites
-------------------

-	[Line Business Center](https://business.line.me/en/)
-	[API References](https://devdocs.line.me/en/)
-	[Line Developers](https://developers.line.me)
-	[Line Admins](https://admin-official.line.me/)
-	[Channels](https://developers.line.me/ba/)
-	[Java SDK](https://github.com/line/line-bot-sdk-java)
-	[FAQ](https://developers.line.me/faq#anc1-1)

Online Tutorials
----------------

-	[line-bot-api-v1](http://line.github.io/line-bot-api-doc/en/getting_started.html)
-	[Getting started with the Messaging API](https://developers.line.me/messaging-api/getting-started)
-	[在 Heroku 建立你自己的 Line 機器人](https://blog.ccjeng.com/2016/06/Line-BOT-API.html)
-	[Line BOT API](http://huli.logdown.com/posts/726082-line-bot-api-tutorial)
-	[關於Linebot(2) - 新版Line@ Messaging API使用心得 (Line Bot v2)](http://studyhost.blogspot.tw/2016/10/line-messaging-api-line-bot-v2.html)
-	[LINE BOT + Bluemix 打造你專屬的LINE機器人](https://annhanmovienight.wordpress.com/2016/06/16/line-bot-bluemix-%E6%89%93%E9%80%A0%E4%BD%A0%E5%B0%88%E5%B1%AC%E7%9A%84line%E6%A9%9F%E5%99%A8%E4%BA%BA/)
-	[LineBot 與電影資訊](https://www.ptt.cc/bbs/Soft_Job/M.1483086766.A.933.html)

Code Examples
-------------

-	[sample-spring-boot-echo](https://github.com/line/line-bot-sdk-java/tree/master/sample-spring-boot-echo)
-	[sample-spring-boot-kitchensink](https://github.com/line/line-bot-sdk-java/tree/master/sample-spring-boot-kitchensink)

Other Techs
-----------

### Heroku

-	[Homepage](https://www.heroku.com/home)
-	[Heroku Dashboard](https://dashboard.heroku.com/)
-	[Free Quota](https://www.heroku.com/policy/aup#quota)
-	[Getting Started with Gradle on Heroku](https://devcenter.heroku.com/articles/getting-started-with-gradle-on-heroku)
	-	Set up: `heroku login`
	-	Prepare the app: `git clone ...`
	-	Deploy the app:
		1.	Create an app on Heroku: `heroku create`
		2.	Deploy your code: `git push heroku master`
		3.	Ensure that at least one instance of the app is running: `heroku ps:scale web=1`
		4.	Open the website: `heroku open`
		5.	View logs: `heroku logs --tail`
-	[Change Timezone of papertrail](http://help.papertrailapp.com/discussions/questions/33-how-do-you-change-timezone.html)

### Gradle

-	[Getting Started With Gradle](https://gradle.org/getting-started-gradle/)
-	[IntelliJ IDEA 2016.3 Help/Getting Started with Gradle](https://www.jetbrains.com/help/idea/2016.3/getting-started-with-gradle.html)
-	[認識 Gradle](http://www.codedata.com.tw/search.php?kw=%E8%AA%8D%E8%AD%98%20Gradle)
-	[認識 Gradle 專案建置自動化工具系列文章（2013年iT邦幫忙鐵人賽）](http://blog.lyhdev.com/2013/10/gradle-2013it.html)
-	[Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
-	[IntelliJ IDEA 2016.3 Help - Gradle](https://www.jetbrains.com/help/idea/2016.3/gradle.html)
-	[Display Gradle output in IntelljJ](http://stackoverflow.com/questions/36977735/display-gradle-output-in-console-in-intellij-idea-2016-1-1)
-	其他備註:

	-	IntelliJ 需在 Setting 中指定 Gradle JVM 為 Java 1.8, 也可使用下列程式碼確認 Gradle 所使用的 Java Version

	```java
	// Add in build.gradle
	println System.getProperty("java.home")
	System.exit(1)
	```

	-	[Synchronizing Changes in Gradle Project and IntelliJ IDEA Project](https://www.jetbrains.com/help/idea/2016.2/synchronizing-changes-in-gradle-project-and-intellij-idea-project.html)

-	`build.gradle` 相關設定說明:

	-	[sourceCompatibility, targetCompatibility](http://stackoverflow.com/questions/16654951/gradle-sourcecompatibility-vs-targetcompatibility)
	-	[buildscript](http://stackoverflow.com/questions/17773817/purpose-of-buildscript-in-gradle)
	-	[systemProperties](https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_properties_and_system_properties)
	-	[testCompile](https://docs.gradle.org/current/userguide/artifact_dependencies_tutorial.html#sec:declaring_your_dependencies)
	-	[jar](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html)

### Spring Boot

-	[Spring 教學(1) - 從 Spring Boot 開始](http://peaceful-developer.logdown.com/posts/220887-spring-teaching-1-starting-from-spring-boot)
-	[Working a Getting Started guide with IntelliJ IDEA](https://spring.io/guides/gs/intellij-idea/)
-	[Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)

### ngrok

-	[Official Site](https://ngrok.com/)
-	[Official Document](https://ngrok.com/docs)
-	使用方式:
	1.	若 Spring Boot 執行的 Web 程式可用 `localhost:8080` 開始時, 則在 cmder 執行 `ngrok http 8080` 後, 便會有 https://&lt;A_RANDOM_NAME&gt;.ngrok.io/ 外部網址可以使用
	2.	可使用此網址 http://localhost:4040/inspect/http 查看對上述網址 Request 的封包

### lombok

-	[Annotation Type Slf4j](https://projectlombok.org/api/lombok/extern/slf4j/Slf4j.html)
-	[Annotation Type Log4j](https://projectlombok.org/api/lombok/extern/log4j/Log4j.html)
-	[Intellj:Cannot find symbol log...](http://stackoverflow.com/questions/14866765/building-with-lomboks-slf4j-and-intellij-cannot-find-symbol-log)

### New Tech Terms

-	the Tomcat servlet container
-	bean
-	[REST](https://spring.io/understanding/REST)
-	[Line Beacon](http://www.ithome.com.tw/news/104847)
-	Autowired
