package com.rain.beans;

import org.springframework.aop.IntroductionAdvisor;
import org.springframework.beans.CachedIntrospectionResults;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/**
 * 用于为Spring bean创建实例的策略接口。
 * 可以用来插入自定义bean属性解析策略（例如，用于其他JVM上的语言）或更高效的检索算法。
 *
 * BeanInfoFactories由{ CachedIntrospectionResults}实例化，
 * 通过使用{ org.springframework.core.io.support.SpringFactoriesLoader}实用程序类。
 *
 * 当要创建{ BeanInfo}时，{CachedIntrospectionResults} 将遍历发现的工厂，调用{ #getBeanInfo（Class）}每一个。
 * 如果返回{@code null}，则会查询下一个工厂。
 * 如果没有一个工厂支持这个类，那么将会有一个标准的{ BeanInfo} 创建为默认值。
 *
 * 参照 CachedIntrospectionResults
 * 参照 org.springframework.core.io.support.SpringFactoriesLoader
 */
public interface BeanInfoFactory {

    BeanInfo getBeanInfo(Class<?> beanClass )throws IntrospectionException;
}
