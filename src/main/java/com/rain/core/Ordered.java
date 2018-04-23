package com.rain.core;

/**
 *  {@code Ordered}是一个接口 可由实现orderable接口的对象implements，例如在{@code Collection}中。
 *
 *  实际的{ #getOrder（）order}可以被解释为优先级，与第一个对象（具有最低的order值）具有最高的优先。
 *
 *  请注意，此界面还有一个 <em>优先级</ em>标记：{PriorityOrdered}。 由{@code PriorityOrdered}表示的订单值
 *  对象始终应用在由<em>plain</em> 表示的相同顺序值之前 {@link Ordered}对象。
 *
 *  有关详情，请咨询Javadoc的{OrderComparator}排序非有序对象的语义。
 *
 *  参照 PriorityOrdered
 *  参照 OrderComparator
 *  参照 org.springframework.core.annotation.Order
 *  参照 org.springframework.core.annotation.AnnotationAwareOrderComparator
 */
public interface Ordered {

    /**
     * 最先优先级值常量
     */
    int HIGHEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 最小优先级值常量
     */
    int LOWER_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * 获取此对象的order值。
     * 较高的值被解释为较低的优先级。 作为结果，具有最低值的对象具有最高优先级（有些类似于Servlet {@code load-on-startup}值）。
     * 相同的order值将根据对象的位置收到影响
     *
     * @return
     */
    int getOrder();
}
