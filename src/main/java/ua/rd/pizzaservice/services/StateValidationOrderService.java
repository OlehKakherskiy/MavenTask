package ua.rd.pizzaservice.services;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.OrderState;
import ua.rd.pizzaservice.domain.StateTransition;
import ua.rd.pizzaservice.domain.StateTransitions;
import ua.rd.pizzaservice.repository.OrderRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Oleg on 17.10.2016.
 */
public class StateValidationOrderService implements InvocationHandler {

    private OrderService orderService;

    private OrderRepository orderRepository;

    private StateValidationOrderService(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    public static OrderService newInstance(OrderService serviceToBeProxied, OrderRepository orderRepository) {
        Class<? extends OrderService> clazz = serviceToBeProxied.getClass();
        return (OrderService) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                new StateValidationOrderService(serviceToBeProxied, orderRepository));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method originalMethod = orderService.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        if (originalMethod.getAnnotation(StateTransitions.class) != null) {
            Order order = getOrder((Long) args[0]);
            OrderState changeTo = findEndTransitionPoint(originalMethod.getAnnotation(StateTransitions.class).value(), getCurrentOrderState(order));
            if (changeTo == null) {
                throw new IllegalStateException(); //не найдено состояние, в которое можно перейти с данного состояния при вызове данного метода, хотя правила перехода есть
            }
            order.changeState(changeTo);
        }
        return method.invoke(orderService, args);
    }

    private Order getOrder(Long id) {
        return orderRepository.readOrder(id);
    }

    private OrderState getCurrentOrderState(Order order) {
        return order.getCurrentState();
    }

    private OrderState findEndTransitionPoint(StateTransition[] transitions, OrderState currentState) {
        for (StateTransition stateTransition : transitions) {
            if (stateTransition.from() == currentState) {
                return stateTransition.to();
            }
        }
        return null;
    }

}