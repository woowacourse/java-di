package ioc

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.support.StaticListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.StaticApplicationContext

/**
 * 스프링 IoC 컨테이너와 Bean 소개
 * 제어의 역전(IoC) 원리를 스프링 프레임워크에서 구현하는 방법을 살펴봅시다.
 */
class Introduction : FreeSpec({

    """
    BeanFactory
    스프링 프레임워크에서 IoC 원리를 실제 구현하기 위해 사용하는 인터페이스입니다.
    BeanFactory를 사용하여 Bean을 등록하고, 조회하고, 관리하는 기능을 제공합니다.
    BeanFactory를 구현한 StaticListableBeanFactory를 사용해봅시다.
    """ {
        // BeanFactory 객체를 생성합니다.
        val beanFactory: BeanFactory = StaticListableBeanFactory()

        // BeanFactory에 SampleObject 객체를 Bean으로 등록합니다.
        (beanFactory as StaticListableBeanFactory).addBean("sampleObject", SampleObject())

        // BeanFactory에서 SampleObject Bean 객체를 조회합니다.
        val sampleObject = beanFactory.getBean("sampleObject", SampleObject::class.java)

        // 조회한 Bean 객체가 SampleObject 타입인지 확인합니다.
        sampleObject.shouldBeTypeOf<Object>()
    }

    """
    ApplicationContext
    스프링 IoC 컨테이너로 BeanFactory를 확장한 인터페이스며 BeanFactory의 모든 기능을 포함하고 있습니다.
    IoC 컨테이너 기능 외에 AOP, i18n, Event publication 같은 엔터프라이즈 전용 기능을 추가로 제공합니다.
    스프링 부트의 SpringApplication.run() 메서드는 내부적으로 ApplicationContext를 사용합니다.
    """ {
        // ApplicationContext 객체를 생성합니다.
        val applicationContext: ApplicationContext = StaticApplicationContext()

        // ApplicationContext에 SampleObject를 Bean으로 등록합니다.
        (applicationContext as StaticApplicationContext).registerSingleton("sampleObject", SampleObject::class.java)

        // ApplicationContext에서 SampleObject Bean 객체를 조회합니다.
        val sampleObject = applicationContext.getBean("sampleObject", SampleObject::class.java)

        // 조회한 Bean 객체가 SampleObject 타입인지 확인합니다.
        sampleObject.shouldBeTypeOf<Object>()

        // ApplicationContext가 BeanFactory를 포함하는지 확인합시다.
        applicationContext.shouldBeInstanceOf<Object>()
    }

    """
    Bean
    스프링 IoC 컨테이너에 의해 관리되는 객체를 빈(Bean)이라 합니다.
    Bean은 스프링 IoC 컨테이너에 의해 인스턴스화, 어셈블 및 관리되는 객체입니다.
    """ {
        // 직접 객체를 만들면 스프링 IoC 컨테이너가 관리하지 않습니다.
        // notBeanObject 객체는 Bean이 아닙니다.
        val notBeanObject = SampleObject()

        val applicationContext = StaticApplicationContext()

        // 스프링 기반 애플리케이션에서 스프링 IoC 컨테이너에 등록한 객체는 Bean이라 부릅니다.
        applicationContext.registerSingleton("sampleObject", SampleObject::class.java)

        // beanObject 객체는 Bean입니다.
        val beanObject = applicationContext.getBean("sampleObject", SampleObject::class.java)

        applicationContext.containsBean("sampleObject") shouldBe false
    }
})
