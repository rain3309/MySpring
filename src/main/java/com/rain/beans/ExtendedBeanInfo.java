package com.rain.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.beans.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

class ExtendedBeanInfo implements BeanInfo {

    private static Log log = LogFactory.getLog(ExtendedBeanInfo.class);
    
    private final BeanInfo delegate;

    private final Set<PropertyDescriptor> propertyDescriptors = new TreeSet<>(new PropertyDescriptorComparator());

    public ExtendedBeanInfo(BeanInfo delegate)throws IntrospectionException{
        this.delegate = delegate;
        for(PropertyDescriptor pd:delegate.getPropertyDescriptors()){
            try{
                this.propertyDescriptors.add(pd instanceof IndexedPropertyDescriptor ? new SimpleIndexedPropertyDecriptor((IndexedPropertyDescriptor) pd):
                new SimplePropertyDescriptor(pd));
            }catch (IntrospectionException ex){
                if(log.isDebugEnabled()){
                    log.debug("忽略无效属性'"+pd.getName()+"':"+ex.getMessage());
                }
            }
        }
        
    }

    public static boolean isCandidateWriteMethod(Method method){
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        int plen = parameterTypes.length;
        return (methodName.length()>3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) &&
                (!void.class.isAssignableFrom(method.getReturnType())||(Modifier.isStatic(method.getModifiers()))&&
                        (plen == 1||(plen ==2&& int.class == parameterTypes[0]))));
    }
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return null;
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return new EventSetDescriptor[0];
    }

    @Override
    public int getDefaultEventIndex() {
        return 0;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[0];
    }

    @Override
    public int getDefaultPropertyIndex() {
        return 0;
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return new MethodDescriptor[0];
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        return new BeanInfo[0];
    }

    @Override
    public Image getIcon(int iconKind) {
        return null;
    }

    static class SimplePropertyDescriptor extends PropertyDescriptor{

        @Nullable
        private Method readMethod;
        @Nullable
        private Method writeMethod;
        @Nullable
        private Class<?> propertyType;
        @Nullable
        private Class<?> propertyEditorClass;

        public SimplePropertyDescriptor(PropertyDescriptor original)throws IntrospectionException{
            this(original.getName(),original.getReadMethod(),original.getWriteMethod());
            PropertyDescriptorUtils.copyNonMethodProperties(original,this);
        }

        public SimplePropertyDescriptor(String name, @Nullable Method readMethod, Method writeMethod) throws IntrospectionException {
            super(name,null,null);
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
            this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod,writeMethod);
        }

        @Override
        public Method getReadMethod() {
            return readMethod;
        }

        @Override
        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
        }

        @Override
        public Method getWriteMethod() {
            return writeMethod;
        }

        @Override
        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        @Override
        public Class<?> getPropertyType() {
            if(this.propertyType == null){
                try {
                    this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod,writeMethod);
                } catch (IntrospectionException e) {
                    //e.printStackTrace();
                }
            }
            return propertyType;
        }

//        @Override
//        public void setPropertyType(Class<?> propertyType) {
//            this.propertyType = propertyType;
//        }

        @Override
        public Class<?> getPropertyEditorClass() {
            return propertyEditorClass;
        }

        @Override
        public void setPropertyEditorClass(Class<?> propertyEditorClass) {
            this.propertyEditorClass = propertyEditorClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            SimplePropertyDescriptor that = (SimplePropertyDescriptor) o;
            return Objects.equals(readMethod, that.readMethod) &&
                    Objects.equals(writeMethod, that.writeMethod) &&
                    Objects.equals(propertyType, that.propertyType) &&
                    Objects.equals(propertyEditorClass, that.propertyEditorClass);
        }

        @Override
        public int hashCode() {

            return Objects.hash(super.hashCode(), readMethod, writeMethod, propertyType, propertyEditorClass);
        }

        @Override
        public String toString() {
            return "SimplePropertyDescriptor{" +
                    "readMethod=" + readMethod +
                    ", writeMethod=" + writeMethod +
                    ", propertyType=" + propertyType +
                    ", propertyEditorClass=" + propertyEditorClass +
                    '}';
        }
    }

    static class SimpleIndexedPropertyDecriptor extends IndexedPropertyDescriptor{

        @Nullable
        private Method readMethod;
        @Nullable
        private Method writeMethod;
        @Nullable
        private Class<?> propertyType;
        @Nullable
        private Method indexedReadMethod;
        @Nullable
        private  Method indexedWriteMethod;
        @Nullable
        private Class<?> indexedPropertyType;
        @Nullable
        private Class<?> propertyEditorCLass;

        public SimpleIndexedPropertyDecriptor(IndexedPropertyDescriptor original)throws IntrospectionException{
            this(original.getName(),original.getReadMethod(),original.getWriteMethod(),
                  original.getIndexedReadMethod(),original.getIndexedWriteMethod());
            PropertyDescriptorUtils.copyNonMethodProperties(original,this);

        }

        public SimpleIndexedPropertyDecriptor(String name, @Nullable Method readMethod, @Nullable Method writeMethod, @Nullable Method indexedReadMethod, @Nullable Method indexedWriteMethod)throws IntrospectionException {
            super(name,null,null,null,null);
            this.readMethod = readMethod;
            this.writeMethod = writeMethod;
            this.indexedReadMethod = indexedReadMethod;
            this.indexedWriteMethod = indexedWriteMethod;
            this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod,this.writeMethod);
            this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(name,propertyType,indexedReadMethod,indexedWriteMethod);
        }


        @Override
        public Method getReadMethod() {
            return readMethod;
        }

        @Override
        public void setReadMethod(@Nullable Method readMethod) {
            this.readMethod = readMethod;
        }

        @Override
        public Method getWriteMethod() {
            return writeMethod;
        }

        @Override
        public void setWriteMethod(@Nullable Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        @Override
        public Class<?> getPropertyType() {
            if(this.propertyType != null){
                try {
                    this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod,this.writeMethod);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                }
            }
            return this.propertyType;
        }

//        @Override
//        public void setPropertyType(Class<?> propertyType) {
//            this.propertyType = propertyType;
//        }

        @Override
        public Method getIndexedReadMethod() {
            return indexedReadMethod;
        }

        @Override
        public void setIndexedReadMethod(Method indexedReadMethod) {
            this.indexedReadMethod = indexedReadMethod;
        }

        @Override
        public Method getIndexedWriteMethod() {
            return indexedWriteMethod;
        }

        @Override
        public void setIndexedWriteMethod(Method indexedWriteMethod) {
            this.indexedWriteMethod = indexedWriteMethod;
        }

        @Override
        public Class<?> getIndexedPropertyType() {
            if(indexedPropertyType != null){
                try {
                    this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(getName(),getPropertyType(),this.indexedReadMethod,this.indexedWriteMethod);
                } catch (IntrospectionException e) {
                    //e.printStackTrace();
                }
            }
            return indexedPropertyType;
        }

//        @Override
//        public void setIndexedPropertyType(Class<?> indexedPropertyType) {
//            this.indexedPropertyType = indexedPropertyType;
//        }

        public Class<?> getPropertyEditorCLass() {
            return propertyEditorCLass;
        }

        public void setPropertyEditorCLass(Class<?> propertyEditorCLass) {
            this.propertyEditorCLass = propertyEditorCLass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            SimpleIndexedPropertyDecriptor that = (SimpleIndexedPropertyDecriptor) o;
            return Objects.equals(readMethod, that.readMethod) &&
                    Objects.equals(writeMethod, that.writeMethod) &&
                    Objects.equals(propertyType, that.propertyType) &&
                    Objects.equals(indexedReadMethod, that.indexedReadMethod) &&
                    Objects.equals(indexedWriteMethod, that.indexedWriteMethod) &&
                    Objects.equals(indexedPropertyType, that.indexedPropertyType) &&
                    Objects.equals(propertyEditorCLass, that.propertyEditorCLass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), readMethod, writeMethod, propertyType, indexedReadMethod, indexedWriteMethod, indexedPropertyType, propertyEditorCLass);
        }
    }

    /**
     * 以字母数字排序PropertyDescriptor实例以模拟行为
     * {@link java.beans.BeanInfo＃getPropertyDescriptors（）}。
     * @see ExtendedBeanInfo＃propertyDescriptors
     */
    static class PropertyDescriptorComparator implements Comparator<PropertyDescriptor> {
        
        @Override
        public int compare(PropertyDescriptor pd1, PropertyDescriptor pd2) {
            String left = pd1.getName();
            String right = pd2.getName();
            for(int i=0;i<left.length();i++){
                if(right.length() == i){
                    return 1;
                }
                int result = left.getBytes()[i] - right.getBytes()[i];
                if(result != 0){
                    return result;
                }
            }
            return left.length() - right.length();
        }
    }

}
