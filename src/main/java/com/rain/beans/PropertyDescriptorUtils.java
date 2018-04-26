package com.rain.beans;

import org.springframework.lang.Nullable;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;

class PropertyDescriptorUtils {

    /**
     * Spring内部{ PropertyDescriptor}实现的通用委托方法。
     */
    public static void copyNonMethodProperties(PropertyDescriptor source,PropertyDescriptor target)throws IntrospectionException {

        target.setExpert(source.isExpert());
        target.setHidden(source.isHidden());
        target.setPreferred(source.isPreferred());
        target.setName(source.getName());
        target.setShortDescription(source.getShortDescription());
        target.setDisplayName(source.getShortDescription());

        //复制所有属性
        Enumeration<String> keys = source.attributeNames();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            target.setValue(key,source.getValue(key));
        }

        target.setPropertyEditorClass(source.getPropertyEditorClass());
        target.setBound(source.isBound());
        target.setConstrained(source.isConstrained());

    }

    /**
     * 参照 See { java.beans.PropertyDescriptor#findPropertyType}.
     * @param readMethod
     * @param writeMethod
     * @return
     * @throws InstantiationError
     */
    public static Class<?> findPropertyType(@Nullable Method readMethod,@Nullable Method writeMethod)throws IntrospectionException{
        Class<?> propertyType = null;
        if(readMethod != null){
            Class<?>[] types = readMethod.getParameterTypes();
            if(types.length != 0){
                throw new IntrospectionException("错误的传参总数："+readMethod);
            }
            propertyType = readMethod.getReturnType();
            if(propertyType == Void.TYPE){
                throw new IntrospectionException("错误的返回值"+readMethod);
            }

        }
        if(writeMethod != null){
            Class<?>[] types = writeMethod.getParameterTypes();
            if(types.length != 1){
                throw new IntrospectionException("错误的传参总数："+writeMethod);
            }
            if(propertyType != null){
                if(propertyType.isAssignableFrom(types[0])){
                    propertyType = types[0];
                }else if(propertyType.isAssignableFrom(types[0])){

                }else{
                    throw new IntrospectionException("传入参数和返回参数不匹配： "+readMethod+" - "+ writeMethod);
                }
            }else{
                propertyType = types[0];
            }

        }
        return propertyType;
    }

    /**
     * 参照 { java.beans.IndexedPropertyDescriptor#findIndexedPropertyType}.
     * @param name
     * @param
     * @param indexedReadMethod
     * @param indexedWriteMethod
     * @return
     * @throws IntrospectionException
     */
    public static Class<?> findIndexedPropertyType(String name,@Nullable Class<?> propertyType,@Nullable Method indexedReadMethod,@Nullable Method indexedWriteMethod)throws IntrospectionException{

        Class<?> indexedPropertyType = null;
        if(indexedReadMethod != null){
            Class<?>[] parameterTypes = indexedReadMethod.getParameterTypes();
            if(parameterTypes.length != 1){
                throw new IntrospectionException("传递参数个数有误： "+indexedReadMethod);
            }
            if(parameterTypes[0] != Integer.TYPE){
                throw new IntrospectionException("传参类型有误： "+indexedReadMethod);
            }
            indexedPropertyType = indexedReadMethod.getReturnType();
            if(indexedPropertyType == Void.TYPE){
                throw new IntrospectionException("返回参数有误： "+indexedReadMethod);
            }
        }

        if(indexedWriteMethod != null){
            Class<?>[] parameterTypes = indexedWriteMethod.getParameterTypes();
            if(parameterTypes.length != 2){
                throw new IntrospectionException("传递参数个数有误："+indexedWriteMethod);
            }
            if(parameterTypes[0] != Integer.TYPE){
                throw new IntrospectionException("传递参数类型有误："+indexedWriteMethod);
            }
            if(indexedPropertyType != null){
                if(indexedPropertyType.isAssignableFrom(parameterTypes[1])){
                    indexedPropertyType = parameterTypes[1];
                }else if(parameterTypes[0].isAssignableFrom(parameterTypes[1])){

                }else{
                    throw new IntrospectionException("传入参数和返回参数不匹配: "+indexedReadMethod+" - "+indexedWriteMethod);
                }
            }else{
                indexedPropertyType = parameterTypes[1];
            }
        }
        return indexedPropertyType;
    }

}
