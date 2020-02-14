package com.apress.spring.springbootjournal;

import com.apress.spring.springbootjournal.domain.Journal;
import com.apress.spring.springbootjournal.repository.JournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintStream;

/* [ @SpringBootApplication ]
 * @Configuration, @EnableAutoConfiguration, @ComponentScan 애너테이션으로 이루어진 애너테이션
 * 스프링 부트 1.0 버전 시절에는 위 3개의 애너테이션을 모두 붙여 스프링 부트 앱을 작성했었지만,
 * 스프링 부트 1.2.0 버전 이후로 @SpringBootApplication이라는 개선된 애너테이션이 등장
 *
 * [ @EnableAutoConfiguration ]
 * 스프링 부트를 움직이는 원동력으로 스프링 부트 자동 구성을 담당
 * 스프링 부트는 클래스패스, 애너테이션, 구성 파일을 보고 가장 적절한 앱에 가장 알맞은 기술을 넣어 구성을 마침
 *
 * [ 정리 ]
 * 따라서 스프링 부트는 @SpringBootApplication과 그 내부의 @EnableAutoConfiguration(자동 구성) 기능 덕분에
 * 전체 어플리케이션 컴포넌트를 식별
 *
 * [ Application 구동 원리 ]
 * 1. 우선, 클래스패스를 조사해서 spring-boot-starter-web 스타터가 선언된 것을 인지한 스프링 부트는 웹 어플리케이션을 구성
 * 2. 따라서, @RequestMapping이 달린 메서드가 있고 @Controller 애너테이션이 달린 JournalController 클래스는 웹 컨트롤러로
 *    보고, 톰캣 서버는 spring-boot-starter-web의 의존체 중 하나이므로 톰캣으로 'Journal' Application을 실행시킴
 *
 * [ 특정 자동 구성 끄기 ]
 * @EnableAutoConfiguration의 exclude 파라미터 속성값에 클래스를 명시
 *
 * 예제)
 * @RestController
 * @EnableAutoConfiguration(exclude=[ActiveMQAutoConfiguration.class])
 * class WebApp {
 *      @RequestMapping("/")
 *      String greetings() {
 *          "스프링 부트 시작!"
 *      }
 * }
 *
 * 예제) - Java로 작성한 스프링 부트 앱에서 자동 구성 제외
 * @SpringBootApplication(exclude={ActiveMQAutoConfiguration.class, DataSourceAutoConfiguration.class})
 * public class DemoApplication {
 *      public static void main(String[] args) {
 *          SpringApplication.run(DemoApplication.class, args);
 *      }
 * }
 * -> @SpringBootApplication은 @EnableAutoConfiguration, @Configuration, @ComponentScan을 상속한
 *    애너테이션이기 때문에 @EnableAutoConfiguration이 아닌 @SpringBootApplication에 직접 exclude
 *    파라미터를 사용해도 됨
 */
@SpringBootApplication
public class SpringBootJournalApplication {
    private static Logger log = LoggerFactory.getLogger(SpringBootJournalApplication.class);

    @Value("${myapp.server-ip}")
    String serverIp;

    @Autowired
    MyAppProperties props;

    @Component
    @ConfigurationProperties(prefix = "myapp")
        public static class MyAppProperties {
            private String name;
            private String description;
            private String serverIp;

            public void setName(String name) {
                this.name = name;
            }
            public String getName() {
                return name;
            }
            public void setDescription(String description) {
                this.description = description;
            }
            public String getDescription() {
                return description;
            }
            public void setServerIp(String serverIp) {
                this.serverIp = serverIp;
            }
            public String getServerIp() {
                return serverIp;
            }
    }

    // InitializingBean은 스프링 엔진이 인스턴스 생성 후 초기화할 때 항상 호출하는 특수 클래스
    @Bean
    InitializingBean saveData(JournalRepository repo) {
        return () -> {
            // Spring Boot Init - JPA Data Save
            log.info("Start JPA Data Save");
            repo.save(new Journal("Spring Boot", "Spring Boot", "01/01/2020"));
            repo.save(new Journal("Spring Framework", "Spring Framework", "02/14/2020"));
            log.info("Start JPA Data End");
        };
    }

    public static void main(String[] args) {
        // [ SpringApplication ]
        // main 메서드에서 실행 할 스프링 부트 앱의 부트스트랩 역할으로 실행할 클래스를 파라미터로 넘김
        // 실행 방법 1)
        // SpringApplication.run(SpringBootJournalApplication.class, args);

        // 실행 방법 2)
        SpringApplication app = new SpringApplication(SpringBootJournalApplication.class);

        // [ 배너 커스텀 방법 ]
        // org.springframework.boot.Banner 인터페이스 구현 시 기존 Spring 문구가 아닌 아래와 같이 배너 커스텀 가능
        // 아스키 아트(Text -> ASCII) URL : http://patorjk.com
        app.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                out.print("\n\n My Custom Banner Starting !\n\n".toUpperCase());
            }
        });
        app.run();
    }

    @Bean
    CommandLineRunner values() {
        return args -> {
            log.info(" > Server IP : " + serverIp);
            log.info(" > Application Name : " + props.getName());
            log.info(" > Application Info : " + props.getDescription());
        };
    }
}
/* [ 이벤트 처리 애노테이션 ]
 * ApplicationStartedEvent : 앱을 시동할 때 사용
 * ApplicationEnvironmentPreparedEvent : 처음 환경 준비를 마쳤을 때 사용
 * ApplicationReadyEvent : 앱 준비가 끝났을 때 사용
 * ApplicationFailedEvent : 시동 중 예외가 발생했을 때 사용
 */

/* [ ApplicationRunner, CommandLineRunner ]
 * 스프링 부트에선 앱 시동 전에 실행할 코드를 ApplicationRunner, CommandLineRunner
 * 인터페이스의 run 메서드에 구현

@SpringBootApplication
public class SpringBootJournalApplication implements CommandLineRunner, ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(SpringBootJournalApplication.class);

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringBootJournalApplication.class, args);
    }

    @Bean
    String info() {
        return "그냥 간단한 문자열 빈입니다.";
    }

    @Autowired
    String info;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("## > ApplicationRunner 구현체 ...");
        log.info("info 빈에 액세스 : " + info);
        args.getNonOptionArgs().forEach(file -> log.info(file));
    }
    @Override
    public void run(String... args) throws Exception {
        log.info("## > CommandLineRunner 구현체 ...");
        log.info("info 빈에 액세스 : " + info);
        for(String arg : args)
            log.info(arg);
    }
}
*/

/* [ SpringBoot Application 구성 ]
 * URL, IP, Credetial, DB 접속 정보 등은 Application 안에 두기도 하지만, 구성 정보를 하드 코딩하는 일은 극구 피해야 함.
 * 따라서, 안전하면서 배포하기 쉽게 구성을 외부화(externalization) 하는 편이 좋음
 *
 * 1-1. 클래스패스에 application.properties 파일을 둠 (다른 곳에 추가할 수도 있음)
 * 1-2. YAML 표기법에 따라 작성한 application.yml 파일을 클래스패스에 둠 (다른 곳에 추가할 수도 있음)
 *   2. 환경 변수 사용 (클라우드 환경에선 이 방법이 기본)
 *
 * 스프링 부트는 독자적 기술로 application.properties나 application.yml 같은 공통 파일에 정의하며, 아무것도 지정하지
 * 않으면 각 프로퍼티는 기본값이 할당됨.
 *
 * @Value("프로퍼티명") 또는 org.springframework.core.env.Environment 인터페이스를 통해 프로퍼티 값을 가져오는 기능은
 * 스프링(스프링 부트)의 유용한 기능 중 하나임.
 *
 * 예) 사용 방법
 * [ application.properties ] - 프로퍼티 파일
 * data.server=remoteserver:3030
 *
 * [ Application ] - Java 클래스 파일
 * // ...
 * @Service
 * public class MyService {
 *
 *      @Value("${data.server}")
 *      private String server;
 *
 *      //...
 * }
 *
 * 위 코드 실행 시, application.properties에 기술한 data.server 프로퍼티 값인 'remoteserver:3030'을
 * 멤버 변수 server에 넣음
 */

/* [ 스프링 부트에서 application.properties, yaml 파일 스캔 순서 ]
 * - 현재 폴더 하위의 /config 폴더
 * - 현재 폴더
 * - 클래스패스 /config 패키지
 * - 클래스패스 루트 (src/main/resources)
 */