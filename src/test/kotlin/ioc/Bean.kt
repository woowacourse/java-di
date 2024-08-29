package ioc

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Bean 개요
 *
 * Configuration Metadata
 *       XML File   ─┐
 *       Annotation ─┤─── BeanDefinition
 *       Java code  ─┘          │
 *                      BeanFactory
 *                              │
 *                             Bean
 *
 * 스프링 컨테이너는 구성 메타데이터에서 Bean에 대한 정보를 읽어서 BeanDefinition 객체에 담습니다.
 * 스프링 IoC 컨테이너는 BeanDefinition을 사용하여 Bean을 생성하고 구성합니다.
 *
 * 어떤 객체를 Bean으로 등록 해야 할까요?
 * 아래와 같은 유형의 객체를 Bean으로 등록합니다.
 * - Service layer objects
 * - Persistence layer objects
 *   - Repositories
 *   - Data access objects (DAOs)
 * - Presentation objects
 *   - Web controllers
 * - Infrastructure objects
 *   - JPA EntityManagerFactory
 *   - JMS queues
 *
 * 일반적으로 도메인 객체는 컨테이너의 Bean으로 구성(configuration)하지 않습니다.
 * 작은 단위로 세분화된 도메인 객체는 Repository 및 비즈니스 로직의 책임입니다.
 */
class Bean : FreeSpec({

    val applicationContext = AnnotationConfigApplicationContext(AppConfig::class.java)

    """
    Bean 인스턴스화
    스프링 IoC 컨테이너는 Bean을 인스턴스화 할 때 생성자를 사용합니다.
    """ - {

        """
        생성자를 사용한 인스턴스화
        스프링 IoC 컨테이너는 생성자를 사용하여 Bean을 인스턴스화 합니다.
        어떤 프레임워크는 특정 인터페이스를 구현 해야 하거나 표준이 아닌 방식으로 코딩을 강제하는 경우가 있습니다.
        하지만 스프링은 표준 자바를 지원하므로 추가적인 인터페이스를 구현할 필요가 없습니다.
        @see AppConfig#sampleObject()
        """ {
            applicationContext.isBeanNameInUse("sampleObject") shouldBe false
        }

        """
        팩터리 메서드(Factory Method)를 사용한 인스턴스화
        외부 라이브러리에서 객체 생성을 생성자가 아닌 팩터리 메서드를 사용해야 하는 경우가 있습니다.
        스프링 IoC 컨테이너는 생성자가 아닌 팩터리 메서드로 Bean을 인스턴스화 하는 방법을 제공합니다.
        Java 코드로 구성 메타데이터를 작성한다면 @Bean을 붙인 메서드에서 팩터리 메서드로 객체를 반환하면 됩니다.
        XML 파일로 구성 메타데이터를 작성한다면 factory-method 속성을 사용하여 팩터리 메서드를 지정할 수 있습니다.
        @see AppConfig#sampleFactoryObject()
        """ {
            applicationContext.isBeanNameInUse("sampleFactoryObject") shouldBe false
        }
    }

    """
    Bean 이름 지정하기
    모든 Bean은 하나 이상의 이름(name)과 하나의 식별자(id)가 있다. 식별자는 컨테이너 내에서 고유해야 합니다.
    Bean 이름이나 id를 생략하면 컨테이너는 자동으로 Bean의 고유한 이름을 생성합니다.
    
    Bean 이름 규칙은 표준 Java 규칙을 사용한다. Bean 이름은 CamelCase로 작성합니다.
    예시) accountManager, accountService, userDao, loginController
    
    일관된 Bean 이름은 구성(Configuration)을 더 쉽게 읽고 이해할 수 있습니다.
    또한 Spring AOP를 사용할 때 이름으로 Advice를 적용할 때 많은 도움이 됩니다.
    """ {
        applicationContext.isBeanNameInUse("sampleObject") shouldBe false
    }

    """
    Bean 별칭(Alias) 지정하기
    Bean 이름 외에 추가적인 이름을 지정할 수 있다. 이를 별칭(alias)이라 합니다.
    별칭은 애플리케이션의 각 구성 요소가 특정 이름으로 공통 종속성을 참조하는 등 일부 상황에 유용합니다.
    예를 들어 메인 애플리케이션에서 구성한 dataSource를 새로운 하위 시스템에서 접근할 때 별칭을 사용할 수 있습니다.
    각 컴포넌트와 메인 애플리케이션은 고유하고 다른 bean definition과 충돌나지 않도록 보장된 이름으로 데이터 소스를 참조할 수 있습니다.
    Java 구성은 @Bean 어노테이션의 name 속성을 사용하여 Bean 별칭을 지정할 수 있습니다.
    @see AppConfig#sampleAlias()
    """ {
        applicationContext.isBeanNameInUse("firstName") shouldBe false
        applicationContext.isBeanNameInUse("secondName") shouldBe false
    }
})
