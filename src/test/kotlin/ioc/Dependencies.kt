package ioc

import circular.CircularDependencyConfig
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.beans.factory.BeanCurrentlyInCreationException
import org.springframework.beans.factory.UnsatisfiedDependencyException
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * 의존성(Dependencies)
 * 일반적인 엔터프라이즈 애플리케이션은 단일 객체(또는 스프링 용어로 Bean)로 구성되지 않습니다.
 * 가장 단순한 애플리케이션이라도 최종 사용자에게 일관된 애플리케이션으로 보이기 위해 함께 작동하는 여러 객체가 있습니다.
 * 객체가 다른 객체와 협업하기 위해 의존성을 갖게 됩니다.
 * 의존성은 컨테이너에 의해 관리되는 객체 간의 관계를 의미합니다.
 * 객체가 협업할 수 있도록 의존성을 설정하는 방법을 살펴보겠습니다.
 */
class Dependencies : FreeSpec({

    """
    의존성 주입(Dependency Injection)
    의존성 주입(DI)은 객체가 생성자 인수나 설정 메서드 인수, 팩터리 메서드 인수로 객체 의존성을 정의하는 프로세스입니다.
    컨테이너는 Bean을 생성할 때 객체 간 의존성을 주입합니다.
    Bean을 생성하고 의존성을 제어하는 주체가 내부 코드가 아닌 외부 컨테이너에 의해 제어되어서 제어의 역전(IoC)이라 부릅니다.
    DI 원칙을 사용하면 코드가 더 깔끔해지며 의존하는 객체의 디커플링에 더 효과적입니다.
    테스트하기가 더 쉬워지고, 인터페이스나 추상 클래스를 사용하면 단위 테스트에서 스텁 또는 가짜 객체를 사용할 수 있습니다.

    DI는 크게 두 가지 변형이 있습니다.
    생성자 기반(Constructor-based) 의존성 주입과 설정자 기반(Setter-based) 의존성 주입입니다.
    스프링은 필드 기반(Field-based) 의존성 주입도 지원하지만 사용을 권장하지 않습니다.

    생성자 기반(Constructor-based) 또는 설정자 기반(setter-based) DI 중 어떤 것을 사용해야 할까요?
    스프링 팀은 일반적으로 생성자 주입을 추천합니다.
    애플리케이션 구성 요소를 불변 객체로 구현할 수 있고 필요한 의존성이 null이 되지 않도록 보장하기 때문입니다.
    또한 생성자 컴포넌트는 항상 완전히 초기화된 상태로 클라이언트(호출) 코드에 반환됩니다.

    필수 의존성은 생성자를 사용하고 선택적 의존성은 setter 메서드 또는 configuration 메서드를 사용하는 방법도 있습니다.
    setter 메서드에 @Autowired를 사용하면 프로퍼티를 필수 의존성으로 만들 수 있지만 인수에 대한 유효성 검사가 포함된 생성자 주입을 사용하는 것이 좋습니다.

    setter DI는 주로 클래스 내에서 합리적인 기본값을 할당할 수 있는 선택적 의존성에만 사용해야 합니다.
    그렇지 않으면 코드가 의존성을 사용하는 모든 곳에서 null이 아닌지 검사를 수행해야 합니다.
    setter DI의 한 가지 장점은 setter 메서드를 사용하면 해당 클래스의 객체를 나중에 재구성하거나 다시 주입할 수 있습니다.
    예를 들어 JMX MBeans를 통한 관리는 setter DI가 유용한 사용 사례입니다.

    특정 클래스에 가장 적합한 DI 스타일을 사용합시다.
    때로는 소스 코드가 없는 타사 클래스를 다룰 때 사용자가 직접 선택해야 하는 경우도 있습니다.
    예를 들어 타사 클래스에 설정자 메서드가 노출되지 않는 경우 생성자 주입이 사용 가능한 유일한 DI 형태일 수 있습니다.
    """ - {

        """
        생성자 기반 의존성 주입(Constructor-based Dependency Injection)
        생성자 기반 DI는 컨테이너가 각각 의존성을 나타내는 여러 인수를 생성자로 전달하며 수행됩니다.
        특정 인수를 사용하여 정적 팩토리 메서드를 호출하여 빈을 생성할 때도 거의 동일합니다.
        @see ConstructorMovieLister
        """ {
            // @ComponentScan으로 ConstructorMovieLister와 DefaultMovieFinder를 찾아서 Bean으로 등록하고 의존성을 주입합니다.
            val applicationContext = AnnotationConfigApplicationContext(AppConfig::class.java)

            val constructorMovieLister =
                applicationContext.getBean("constructorMovieLister", ConstructorMovieLister::class.java)

            // 스프링 컨테이너가 DefaultMovieFinder를 주입했는지 확인합시다.
            constructorMovieLister.movieFinder.shouldBeTypeOf<Object>()
        }

        """
        설정자(또는 세터) 기반 의존성 주입(Setter-based Dependency Injection)
        세터 기반 DI는 컨테이너가 인수가 없는 생성자 또는 인수가 없는 정적 팩토리 메서드로 Bean을 인스턴스화한 다음에 setter 메서드를 호출하여 수행됩니다.
        설정자 기반 DI는 생성자 기반 DI 보다 더 유연하게 의존성을 설정할 수 있습니다.
        생성자 기반 DI는 빈을 생성할 때 모든 의존성을 지정해야 하지만 설정자 기반 DI는 필요한 의존성만 지정할 수 있습니다.
        하지만 의존성이 지정되기 전에 객체의 기능을 사용하면 런타임 오류가 발생할 수 있습니다.
        @see SetterMovieLister
        """ {
            // @ComponentScan으로 SetterMovieLister와 DefaultMovieFinder를 찾아서 Bean으로 등록하고 의존성을 주입합니다.
            val applicationContext = AnnotationConfigApplicationContext(AppConfig::class.java)

            val setterMovieLister = applicationContext.getBean("setterMovieLister", SetterMovieLister::class.java)

            // 스프링 컨테이너가 DefaultMovieFinder를 주입했는지 확인합시다.
            setterMovieLister.movieFinder.shouldBeTypeOf<Object>()
        }

        """
        ❗순환 의존성(Circular dependencies)
        순환 의존성은 두 개 이상의 모듈이나 클래스가 서로 의존하는 상황을 말합니다.
        생성자 주입을 사용할 때 주의해야 할 사항 중 하나는 순환 의존성을 피해야 한다는 것입니다.
        생성자 주입을 잘못 사용하면 순환 의존성 시나리오가 발생할 수 있습니다.
        예를 들어, 클래스 A가 클래스 B에 의존하고, 동시에 클래스 B가 클래스 A에 의존하는 경우를 말합니다.
        클래스 A와 B가 순환 참조 상태라면 Spring IoC 컨테이너는 런타임에 예외를 던집니다.
        
        한 가지 해결책은 클래스의 일부 소스 코드를 생성자 대신 setter로 구성하도록 편집하는 것입니다.
        권장되지는 않지만 setter 주입을 사용하여 순환 의존성을 구성할 수 있습니다.
        """ {
            // CircularDependencyConfig 클래스에서 ACircularObject와 BCircularObject가 서로 의존하도록 설정했습니다.
            shouldThrow<BeanCurrentlyInCreationException> {
                AnnotationConfigApplicationContext(CircularDependencyConfig::class.java)
            }
        }
    }
})
