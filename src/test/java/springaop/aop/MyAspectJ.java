package springaop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * Created by author on 2017/9/1.
 */
@Aspect
@Component
public class MyAspectJ {
    static {
        System.out.println("static1");

    }

    static {
        System.out.println("static2");
    }

    @Pointcut("execution(* *.test(..))")
    public void pointcute() {
    }

    @Before("pointcute()")
    public void before() {
        System.out.println("-------before1--------");
    }

    @After("pointcute()")
    public void after() {
        System.out.println("------after1--------");
    }

    @Around("execution(* *.test(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("--------around1 before----------");
        Object o = proceedingJoinPoint.proceed();
        System.out.println("--------around1 after----------");
        return o;
    }

    @AfterReturning("pointcute()")
    public void afterRetuning1() {
        System.out.println("afterReturning1");
    }

    @AfterThrowing("pointcute()")
    public void afterThrowing() {
        System.out.println("afterThrowing1");
    }

    @Before("pointcute()")
    public void before1() {
        System.out.println("-------before2--------");
    }

    @After("pointcute()")
    public void after1() {
        System.out.println("------after2--------");
    }

    @Around("execution(* *.test(..))")
    public Object around1(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("--------around2 before----------");
        Object o = proceedingJoinPoint.proceed();
        System.out.println("--------around2 after----------");
        return o;
    }
}
