package org.example.common;

import org.apache.commons.collections4.ListUtils;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

/**
 * 使用Javers比较对象差异的工具类
 * 提供对象比较功能，并对比较结果进行分类处理
 *
 * @author guohao.lu
 */
public class JaversObjectComparator {
    /**
     * Javers实例，用于比较对象差异
     */
    private static final Javers JAVERS = JaversBuilder.javers().build();


    /**
     * 比较两个对象并提取它们之间的差异变化。
     *
     * @param oldObj 旧对象，用于比较的基准对象。
     * @param newObj 新对象，与旧对象进行比较以提取变化。
     * @return 包含所有变化的列表，每个变化以 Change 对象表示。
     *         如果两个对象完全相同，则返回一个空列表。
     */
    public static List<Change> extractChanges(Object oldObj, Object newObj) {
        // 使用 JAVERS 框架比较两个对象，生成差异结果
        Diff diff = JAVERS.compare(oldObj, newObj);

        // 提取并返回差异结果中的所有变化
        return ListUtils.emptyIfNull(diff.getChanges());
    }


    /**
     * 提取两个对象之间的差异变化。
     * <p>
     * 该方法通过指定的属性提取器函数，从源对象和目标对象中提取指定属性值，
     * 使用 JAVERS 框架比较这两个属性值，并返回它们之间的差异变化列表。
     *
     * @param <T>        源对象和目标对象的类型
     * @param <K>        属性提取器返回的属性值类型
     * @param source     源对象，必须不为 null
     * @param target     目标对象，必须不为 null
     * @param propExtract 属性提取器函数，用于从源对象和目标对象中提取需要比较的属性值
     * @return           包含所有差异变化的列表，每个变化由 JAVERS 框架生成
     */
    public static <T, K> List<Change> extractChanges(T source, T target, Function<T, K> propExtract) {
        // 确保源对象和目标对象不为 null
        Assert.notNull(source, "source must not be null");
        Assert.notNull(target, "target must not be null");

        // 使用属性提取器函数分别从源对象和目标对象中提取属性值
        K oldObj = propExtract.apply(source);
        K newObj = propExtract.apply(target);

        return extractChanges(oldObj, newObj);
    }
}
