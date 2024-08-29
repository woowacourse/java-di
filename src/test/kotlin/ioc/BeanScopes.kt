package ioc

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Bean 스코프
 * Bean definition에서 생성되는 객체는 스코프(scope)도 제어할 수 있습니다.
 * 스코프는 애플리케이션이 실행되는 동안 스프링 컨테이너에 의해 Bean이 생성되고, 존재하고, 소멸되는 범위를 의미합니다.
 * 스프링 프레임워크는 6개의 스코프를 지원하며, 이 중 4개의 스코프는 웹용 ApplicationContext를 사용하는 경우에만 사용할 수 있습니다.
 * 가장 많아 알려진 싱글톤과 프로토타입 스코프에 대해 알아보겠습니다.
 * 스프링 프레임워크는 스레드 스코프(Thread scope)와 사용자 지정 스코프(Custom Scope)도 지원하지만, 여기서는 다루지 않겠습니다.
 *
 * Bean Scope
 * | Scope       | Description                                                                               |
 * |-------------|-------------------------------------------------------------------------------------------|
 * | Singleton   | (기본값) 스프링 컨테이너에서 하나의 Bean 인스턴스만 생성하고, 모든 요청에 대해 동일한 인스턴스를 반환합니다.       |
 * | Prototype   | 스프링 컨테이너에서 요청할 때마다 새로운 Bean 인스턴스를 생성합니다.                                      |
 * | Request     | HTTP 요청마다 하나의 Bean 인스턴스를 생성합니다. 웹용 ApplicationContext의 컨텍스트에서만 유효합니다.       |
 * | Session     | HTTP 세션마다 하나의 Bean 인스턴스를 생성합니다. 웹용 ApplicationContext의 컨텍스트에서만 유효합니다.       |
 * | Application | ServletContext마다 하나의 Bean 인스턴스를 생성합니다. 웹용 ApplicationContext의 컨텍스트에서만 유효합니다. |
 * | WebSocket   | Web Socket마다 하나의 Bean 인스턴스를 생성합니다. 웹용 ApplicationContext의 컨텍스트에서만 유효합니다.     |
 */
class BeanScopes : FreeSpec({

    val applicationContext = AnnotationConfigApplicationContext(BeanScopesConfig::class.java)

    """
    싱글톤(Singleton)
    싱글톤 스코프는 스프링의 기본 스코프입니다.
    싱글톤 Bean의 인스턴스는 컨테이너에서 하나만 생성되며, 모든 요청에 대해 동일한 인스턴스가 반환됩니다.
    Bean definition에서 싱글톤으로 스코프를 지정하면 스프링 IoC 컨테이너는 해당 빈 정의에 의해 객체의 인스턴스를 정확히 하나만 생성합니다.

    웹 환경에서 싱글톤 Bean은 여러 스레드가 동시에 접근할 수 있기 때문에 상태를 가지면 안 됩니다.
    Bean이 변경 가능한 상태가 되면 동시성 문제가 발생할 수 있습니다.
    따라서 싱글톤 Bean은 상태 비저장(stateless) 객체로 구현해야 합니다.
    이 단일 인스턴스는 싱글톤 Bean의 캐시에 저장되며, 해당 명명된 Bean에 대한 모든 후속 요청 및 참조는 캐시된 객체를 반환합니다.

    ❗스프링의 싱글톤 Bean 개념은 GoF(Gang of Four) 패턴 책에 정의된 싱글톤 패턴과 다릅니다.
    GoF 싱글톤은 특정 클래스의 인스턴스가 클래스 로더(Class Loader)당 하나만 생성되도록 객체의 스코프를 하드코딩합니다.
    스프링은 컨테이너에서 특정 클래스에 대해 하나의 Bean을 정의하면 컨테이너는 해당 빈 정의에 의해 정의된 클래스의 인스턴스를 하나만 생성합니다.
    """ {
        val firstSingletonObject = applicationContext.getBean("singletonBean", SampleObject::class.java)
        val secondSingletonObject = applicationContext.getBean("singletonBean", SampleObject::class.java)

        // 싱글톤 스코프로 정의된 Bean은 여러 번 요청해도 동일한 인스턴스를 반환합니다.
        firstSingletonObject shouldBe null // secondSingletonObject
    }

    """
    프로토타입(Prototype)
    프로토타입 스코프의 Bean은 특정 Bean에 대한 요청이 있을 때마다 새로운 빈 인스턴스를 생성합니다.
    즉, Bean이 다른 Bean에 주입되거나 컨테이너에서 getBean() 메서드 호출을 통해 요청됩니다.
    원칙적으로 모든 상태 저장(stateful) Bean은 프로토타입 스코프를 사용하고 상태 비저장(stateless) Bean은 싱글톤 스코프를 사용해야 합니다.
    예를 들어, 데이터 액세스 객체(DAO)는 싱글톤으로 구성합니다. 일반적인 DAO는 상태를 보유하지 않기(stateless) 때문입니다.

    ❗다른 스코프와 달리 스프링은 프로토타입 Bean의 전체 라이프사이클을 관리하지 않습니다.
    컨테이너는 프로토타입 객체를 인스턴스화, 구성 및 기타 방식으로 어셈블하여 클라이언트에 전달하며, 해당 프로토타입 인스턴스에 대한 추가 등록은 하지 않습니다.
    따라서 초기화 수명 주기 콜백 메서드는 범위에 관계없이 모든 객체에서 호출되지만, 프로토타입의 경우 구성된 소멸 수명 주기 콜백은 호출되지 않습니다.
    클라이언트 코드는 프로토타입 객체를 정리하고 프로토타입 Bean이 보유하고 있는 리소스를 직접 해제해야 합니다.
    컨테이너가 프로토타입 Bean이 보유한 리소스를 해제하려면 Bean에 대한 참조가 있는 사용자 정의 Bean Post-Processor를 사용하세요.
    """ {
        val firstPrototypeObject = applicationContext.getBean("prototypeBean", SampleObject::class.java)
        val secondPrototypeObject = applicationContext.getBean("prototypeBean", SampleObject::class.java)

        // 프로토타입 스코프로 정의된 Bean은 요청할 때마다 새로운 인스턴스를 생성합니다.
        firstPrototypeObject shouldBe null // secondPrototypeObject
    }

    """
    ❗프로토타입 Bean 의존성이 있는 싱글톤 Bean
    프로토타입 Bean에 대한 의존성이 있는 싱글톤 Bean을 사용하는 경우, 의존성은 인스턴스화 시점에 결정된다는 점에 유의하세요.
    프로토타입 Bean을 싱글톤 Bean에 의존성 주입하면 새 프로토타입 Bean이 인스턴스화된 다음 싱글톤 Bean에 의존성 주입됩니다.
    그러나 싱글톤 Bean이 런타임에 프로토타입 Bean의 새 인스턴스를 반복적으로 요청한다고 가정해 보겠습니다.
    컨테이너가 싱글톤 Bean을 인스턴스화하고 해당 의존성을 주입할 때 한 번만 발생하므로 프로토타입 Bean을 싱글톤 Bean에 의존성 주입할 수 없습니다.
    런타임에 프로토타입 Bean의 새 인스턴스가 두 번 이상 필요한 경우 메서드 주입(method injection)을 참조하세요.
    메서드 주입은 강의에서 다루지 않습니다. 필요하다면 공식 문서를 참고하세요.
    """ - {

        """
        PrototypeIntoSingleton 클래스
        프로토타입 Bean --의존성 주입--> 싱글톤 Bean
        프로토타입 Bean에 대한 의존성이 있는 싱글톤 Bean을 사용하는 경우
        """ {
            val firstPrototypeIntoSingleton =
                applicationContext.getBean("prototypeIntoSingleton", PrototypeIntoSingleton::class.java)
            val secondPrototypeIntoSingleton =
                applicationContext.getBean("prototypeIntoSingleton", PrototypeIntoSingleton::class.java)

            firstPrototypeIntoSingleton shouldBe secondPrototypeIntoSingleton

            // ❓프로토타입 Bean인데 왜 같은 인스턴스를 반환할까요?
            firstPrototypeIntoSingleton.prototypeBean shouldBe null // secondPrototypeIntoSingleton.prototypeBean
        }

        """
        SingletonIntoPrototype 클래스
        싱글톤 Bean --의존성 주입--> 프로토타입 Bean
        싱글톤 Bean에 대한 의존성이 있는 프로토타입 Bean을 사용하는 경우      
        """ {
            val firstSingletonIntoPrototype =
                applicationContext.getBean("singletonIntoPrototype", SingletonIntoPrototype::class.java)
            val secondSingletonIntoPrototype =
                applicationContext.getBean("singletonIntoPrototype", SingletonIntoPrototype::class.java)

            // 둘 다 프로토타입 Bean이므로 서로 다른 인스턴스를 반환합니다.
            firstSingletonIntoPrototype shouldNotBe secondSingletonIntoPrototype

            // 프로토타입 Bean이 의존하는 싱글톤 Bean은 동일한 인스턴스를 사용합니다.
            firstSingletonIntoPrototype.singletonBean shouldBe null // secondSingletonIntoPrototype.singletonBean
        }
    }
})
