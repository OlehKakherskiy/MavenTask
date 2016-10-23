package ua.rd.pizzaservice.infrastructure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oleg on 17.10.2016.
 */
public class BenchmarkBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(final Object proxiedBean, String s) throws BeansException {
        final Class<?> clazz = proxiedBean.getClass();
        return isBenchmarkPresented(clazz) ? Proxy.newProxyInstance(clazz.getClassLoader(),
                wrapInterfaces(getAllInterfaces(clazz)), new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        try {
                            Method targetMethod = getTargetBeanMethod(clazz, method);
                            if (targetMethod.getAnnotation(Benchmark.class) != null) {
                                long start = System.nanoTime();
                                Object res = method.invoke(proxiedBean, objects);
                                System.out.println(MessageFormat.format("exec time of bean {0} of method: {1}: {2}",
                                        clazz, method.getName(), System.nanoTime() - start));
                                return res;
                            }
                        } catch (Throwable e) {
                        }
                        return method.invoke(proxiedBean, objects);
                    }
                }) : proxiedBean;

    }

    private Method getTargetBeanMethod(Class<?> type, Method method) throws NoSuchMethodException {
        return type.getDeclaredMethod(method.getName(), method.getParameterTypes());
    }

    private boolean isBenchmarkPresented(Class<?> beanType) {
        if (beanType == Object.class) {
            return false;
        }
        for (Method m : beanType.getDeclaredMethods()) {
            if (m.getAnnotation(Benchmark.class) != null) {
                System.out.println("m = " + m);
                return true;
            }
        }
        return isBenchmarkPresented(beanType.getSuperclass());
    }

    private Class<?>[] wrapInterfaces(List<?> interfaces) {
        Class<?>[] result = new Class<?>[interfaces.size()];
        interfaces.toArray(result);
        return result;
    }

    private List<Class<?>> getAllInterfaces(Class<?> beanType) {
        if (beanType == Object.class) {
            return new ArrayList<>();
        }
        List<Class<?>> currentLevelInterfaces = Arrays.asList(beanType.getInterfaces());
        currentLevelInterfaces.addAll(getAllInterfaces(beanType.getSuperclass())); //add all superclass interfaces
        return currentLevelInterfaces;
    }

}
