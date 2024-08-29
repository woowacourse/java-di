package ioc

import annotation.*
import annotation.sample.*
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * 어노테이션 기반 컨테이너 구성(Annotation-based Container Configuration)
 *
 * XML이 아닌 클래스, 메서드 또는 필드 선언에 어노테이션을 사용하여 스프링 컨테이너를 구성합니다.
 * 또한 스프링은 @Inject, @Named와 같은 자바 표준 어노테이션을 지원합니다.
 *
 * 스프링을 구성하는 데 XML보다 어노테이션이 더 나은 방법일까요?
 * 어노테이션 기반 구성이 도입되면서 XML보다 "더 나은" 방식인지에 대한 의문이 제기되었습니다.
 * 질문에 대한 대답은 "상황에 따라 다르다"입니다.
 * 각 접근 방식에는 장단점이 있으며, 일반적으로 어떤 방식이 더 적합한지는 개발자가 결정해야 합니다.
 * 어노테이션의 선언 방식은 많은 컨텍스트를 제공하므로 더 짧고 간결한 구성으로 이어집니다.
 * 그러나 XML은 소스 코드를 건드리거나 다시 컴파일하지 않고도 컴포넌트를 구성할 수 있습니다.
 * 일부 개발자는 어노테이션이 달린 클래스는 더 이상 POJO가 아니며, 구성이 분산되고 제어하기 더 어려워진다고 주장합니다.
 * 어떤 선택을 하든 스프링은 두 가지 스타일을 모두 수용하거나 함께 혼합할 수도 있습니다.
 * 스프링은 XML 대신 JavaConfig을 사용하여 비침습적인 방식으로 어노테이션을 구성할 수 있습니다.
 */
class AnnotationBasedConfiguration : FreeSpec({

    """
    @Autowired 사용하기
    @Autowired을 사용하면 스프링이 해당 타입의 Bean을 찾아서 주입합니다.
    스프링 프레임워크 4.3부터 Bean이 하나의 생성자만 정의하는 경우 @Autowired를 생략할 수 있습니다.
    그러나 기본 생성자가 없거나 생성자가 여러 개 있다면 생성자 중 하나 이상에 @Autowired를 추가해야 합니다.
    """ - {

        val applicationContext = AnnotationConfigApplicationContext(AnnotationConfig::class.java)

        """
        @Inject 사용하기
        스프링의 @Autowired 대신 자바 표준 @Inject을 사용할 수도 있습니다.
        """ {
            val movieRecommender = applicationContext.getBean(MovieRecommender::class.java)
            val customerPreferenceDao = movieRecommender.customerPreferenceDao

            customerPreferenceDao.shouldBeNull()
        }

        """
        컬렉션 자동 주입
        스프링은 Array, List, Set, Map에 대한 @Autowired를 지원합니다.
        MovieRecommender 클래스에서 setMovieCatalogs() 메서드는 Map<String, MovieCatalog>를 주입받습니다.
        """ {
            val movieRecommender = applicationContext.getBean(MovieRecommender::class.java)
            val movieCatalogs = movieRecommender.movieCatalogs

            movieCatalogs.shouldContainKey("")
        }

        """
        컬렉션 자동 주입 응용하기
        인터페이스를 활용하면 다양한 구현체를 하나의 컬렉션으로 자동 주입할 수 있습니다.
        MessageSender 구현체를 Bean으로 등록하고 MessageService에 Map으로 주입합니다.
        """ {
            // MessageService 생성자는 Map<String, MessageSender>를 주입 받습니다.
            // MessageSender는 인터페이스입니다.
            // 스프링이 MessageSender 인터페이스를 구현한 Bean을 모아서 자동으로 Map 형태로 주입합니다.
            val messageService = applicationContext.getBean(MessageService::class.java)

            // Bean의 이름이 key, Bean의 구현체가 value로 주입됩니다.
            val kakaotalkMessageSender = messageService.getMessageSender("kakaotalkMessageSender")

            // 생성자에 인터페이스만 명시했는데, 스프링이 자동으로 Bean 구현체를 찾아서 주입했습니다.
            kakaotalkMessageSender.shouldBeTypeOf<Object>()
        }

        """
        @Autowired의 required 속성
        @Autowired의 required 속성은 주입할 Bean이 없을 때 예외를 발생시킬지 여부를 결정합니다.
        required 속성의 기본값은 true입니다.
        required 속성을 false로 설정하면 주입할 Bean이 없을 때 예외가 발생하지 않습니다.
        객체의 속성을 선택적으로 의존성 주입해야 할 때 사용합니다.
        참고로 @Inject는 required 속성을 지원하지 않습니다.
        """ {
            val simpleMovieLister = applicationContext.getBean(SimpleMovieLister::class.java)

            // NoImplements는 인터페이스이고 구현체가 없습니다.
            // 하지만 setNoImplements() 메서드에 @Autowired(required = false)를 설정했기 때문에 예외가 발생하지 않습니다.
            val noImplements = simpleMovieLister.noImplements

            noImplements.shouldNotBeNull()
        }
    }

    """
    @Primary로 우선순위 부여하기
    동일한 타입의 Bean이 여러 개 등록되어 있을 때 어떤 Bean을 주입해야 할지 모호할 수 있습니다.
    한 가지 방법은 @Primary을 사용하는 것입니다.
    @Primary는 여러 Bean이 단일 값 종속성에 자동 연결될 후보일 때 특정 Bean에 우선순위를 부여합니다.
    후보 중 정확히 하나의 기본 Bean이 존재하면 해당 Bean이 자동 연결 됩니다.
    """ {
        /**
         * MovieConfig 클래스에서 MovieCatalog 타입의 Bean을 두 개로 등록했습니다.
         * 그리고 MovieConfig 클래스에서 MovieCatalog를 주입 받습니다.
         * 하지만 MovieCatalog 타입의 Bean이 두 개이기 때문에 어떤 Bean을 주입해야 할지 모호합니다.
         * 이때 @Primary를 사용하여 우선순위를 부여하면 해당 Bean이 주입됩니다.
         * @Primary를 사용하지 않으면 예외가 발생합니다.
         * MovieConfig 클래스에서 @Primary를 주석 처리하고 테스트를 실행해보세요.
         */
        val applicationContext = AnnotationConfigApplicationContext(PrimaryConfig::class.java)
        val primaryConfig = applicationContext.getBean(PrimaryConfig::class.java)
        val movieCatalog = primaryConfig.movieCatalog

        movieCatalog.shouldBeTypeOf<Object>()
    }

    """
    @Qualifier로 주입 받을 Bean 지정하기
    동일한 타입의 Bean에서 상황에 따라 특정 Bean을 주입 받아야 할 때 @Qualifier를 사용합니다.
    Bean을 주입할 때 @Qualifier를 사용하여 Bean의 이름을 지정하면 해당 Bean이 주입됩니다.

    Bean 이름으로 주입할 Bean을 지정할 때 @Autowired를 사용할 수 있지만, @Resource를 사용하는 것이 더 좋습니다.
    스프링은 JSR-250 @Resource을 지원합니다. Jakarta EE에서 흔히 볼 수 있는 패턴입니다.

    @Autowired는 타입을 우선하여 후보 Bean을 선택합니다. 적절한 타입의 Bean을 못 찾으면 Bean 이름을 사용합니다.
    @Resource는 이름을 우선하여 Bean을 지정합니다. 적절한 Bean 이름이 없으면 타입을 사용합니다.
    """ {
        // QualifierConfig 클래스에서 setter 주입에 @Qualifier를 사용하여 Bean의 이름을 지정합니다.
        // @Qualifier를 제거하면 예외가 발생하는지 테스트로 확인해보세요.
        val applicationContext = AnnotationConfigApplicationContext(QualifierConfig::class.java)
        val qualifierConfig = applicationContext.getBean(QualifierConfig::class.java)
        val movieCatalog = qualifierConfig.movieCatalog

        movieCatalog.shouldBeTypeOf<Object>()
    }

    """
    @Value 사용하기
    @Value를 사용하면 외부 프로퍼티 파일에 정의된 값을 주입할 수 있습니다.
    """ {
        val applicationContext = AnnotationConfigApplicationContext(PropertyConfig::class.java)
        val sampleValue = applicationContext.getBean(SampleValue::class.java)

        sampleValue.value shouldBe "외부값"

        // defaultValue는 외부 프로퍼티에 정의되어 있지 않습니다.
        // @Value("${value.default:기본값}")에서 설정한 문자열 "기본값"이 주입됩니다.
        sampleValue.defaultValue shouldBe ""
    }
})
