package com.rain.beans;

import com.rain.core.Ordered;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public class ExtendedBeanInfoFactory implements BeanInfoFactory,Ordered {

    @Override
    public BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
