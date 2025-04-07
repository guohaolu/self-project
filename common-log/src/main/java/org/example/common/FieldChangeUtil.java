package org.example.common;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 字段变更工具类，基于Java Bean的自省机制
 *
 * @author guohao.lu
 */
public final class FieldChangeUtil {
    // 私有构造方法，防止实例化
    private FieldChangeUtil() {
    }

    /**
     * 将单个对象转换为字段变更列表
     * 此方法遍历对象的所有属性，并为每个属性创建一个FieldChange对象，表示字段的变更
     * 如果对象为null，则返回空列表
     *
     * @param target 待转换的对象
     * @param <T>    对象的类型
     * @return 字段变更列表
     */
    public static <T> List<FieldChange> extract(T target) {
        if (target == null) {
            return new ArrayList<>();
        }

        List<FieldChange> fieldChanges = new ArrayList<>();
        BeanWrapper beanWrapper = new BeanWrapperImpl(target);

        for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            FieldChange fieldChange = new FieldChange(propertyName, null, propertyValue == null ? "空" : Objects.toString(propertyValue));
            fieldChanges.add(fieldChange);
        }

        return fieldChanges;
    }

    /**
     * 将对象列表转换为字段变更列表的列表
     * 此方法利用流（Stream）对每个对象调用convert方法，将每个对象转换为字段变更列表，
     * 并将这些列表收集到一个更大的列表中
     * 如果输入列表为空或null，则返回空列表
     *
     * @param targetList 待转换的对象列表
     * @param <T>        对象的类型
     * @return 字段变更列表的列表
     */
    public static <T> List<List<FieldChange>> extract(List<T> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return new ArrayList<>();
        }

        return targetList.stream().map(FieldChangeUtil::extract).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * 比较两个对象的字段值，并生成字段变更列表
     * 此方法会遍历 source 和 target 的所有属性，比较它们的值，
     * 并为每个发生变更的字段创建一个 FieldChange 对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T>    对象的类型
     * @return 字段变更列表
     */
    public static <T> List<FieldChange> convert(T source, T target) {
        // 如果 source 和 target 都为 null，返回空列表
        if (source == null && target == null) {
            return new ArrayList<>();
        }

        List<FieldChange> fieldChanges = new ArrayList<>();

        // 如果 source 或 target 为 null，则认为所有字段都发生了变更
        if (source == null || target == null) {
            BeanWrapper nonNullWrapper = new BeanWrapperImpl(source != null ? source : target);
            for (PropertyDescriptor propertyDescriptor : nonNullWrapper.getPropertyDescriptors()) {
                String propertyName = propertyDescriptor.getName();
                Object propertyValue = nonNullWrapper.getPropertyValue(propertyName);
                FieldChange fieldChange = new FieldChange(
                        propertyName,
                        source == null ? "空" : Objects.toString(propertyValue),
                        target == null ? "空" : Objects.toString(propertyValue)
                );
                fieldChanges.add(fieldChange);
            }
            return fieldChanges;
        }

        // 使用 BeanWrapper 获取 source 和 target 的属性
        BeanWrapper sourceWrapper = new BeanWrapperImpl(source);
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        // 遍历 source 的所有属性
        for (PropertyDescriptor propertyDescriptor : sourceWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            Object sourceValue = sourceWrapper.getPropertyValue(propertyName);
            Object targetValue = targetWrapper.getPropertyValue(propertyName);

            // 比较字段值，如果不同则记录变更
            if (!Objects.equals(sourceValue, targetValue)) {
                FieldChange fieldChange = new FieldChange(
                        propertyName,
                        sourceValue == null ? "空" : Objects.toString(sourceValue),
                        targetValue == null ? "空" : Objects.toString(targetValue)
                );
                fieldChanges.add(fieldChange);
            }
        }

        return fieldChanges;
    }

}
