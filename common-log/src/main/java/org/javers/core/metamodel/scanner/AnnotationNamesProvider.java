package org.javers.core.metamodel.scanner;

import com.ewayt.erp.common.core.diff.feature.annotation.MetaFieldFeature;
import com.fastobject.diff.DiffLog;
import io.swagger.v3.oas.annotations.media.Schema;
import org.javers.common.collections.Lists;
import org.javers.common.reflection.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author bartosz walacik
 */
class AnnotationNamesProvider {
    private final Set<String> entityAliases = new HashSet<>();
    private final Set<String> typeNameAliases = new HashSet<>();
    private final Set<String> valueObjectAliases = new HashSet<>();
    private final Set<String> valueAliases = new HashSet<>();
    private final Set<String> transientPropertyAliases = new HashSet<>();
    private final Set<String> propertyNameAliases = new HashSet<>();

    private final List<AnnotationsNameSpace> namesProviders = Lists.immutableListOf(
            new JPAAnnotationsNameSpace());


    AnnotationNamesProvider() {
        for (AnnotationsNameSpace provider : namesProviders){
            entityAliases.addAll(provider.getEntityAliases());
            valueObjectAliases.addAll(provider.getValueObjectAliases());
            valueAliases.addAll(provider.getValueAliases());
            transientPropertyAliases.addAll(provider.getTransientPropertyAliases());
            typeNameAliases.addAll(provider.getTypeNameAliases());
            propertyNameAliases.addAll(provider.getPropertyNameAliases());
        }
    }

    boolean hasEntityAnnAlias(Set<Class<? extends Annotation>> annTypes) {
        return annTypes.stream().anyMatch(annType -> entityAliases.contains(annType.getSimpleName()));
    }

    boolean hasValueObjectAnnAlias(Set<Class<? extends Annotation>> annTypes) {
        return annTypes.stream().anyMatch(annType -> valueObjectAliases.contains(annType.getSimpleName()));
    }

    boolean hasValueAnnAlias(Set<Class<? extends Annotation>> annTypes){
        return annTypes.stream().anyMatch(annType -> valueAliases.contains(annType.getSimpleName()));
    }

    boolean hasTransientPropertyAnn(Set<Class<? extends Annotation>> annTypes) {
        for (Class<? extends Annotation> annType : annTypes) {
            if (annType.equals(MetaFieldFeature.class)) {
                try {
                    // 获取注解实例
                    Annotation annotation = annType.getAnnotation(MetaFieldFeature.class);
                    if (annotation != null) {
                        // 获取 value 属性的方法
                        Method valueMethod = annType.getDeclaredMethod("ignore");
                        // 调用 value 方法获取属性值
                        boolean value = (boolean) valueMethod.invoke(annotation);
                        // 检查 value 是否为 true
                        if (value) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error occurred while accessing annotation value: " + e.getMessage());;
                }
            }
        }

        return annTypes.stream()
                .anyMatch(annType -> transientPropertyAliases.contains(annType.getSimpleName()));
    }

    boolean hasDiffIncludeAnn(Set<Class<? extends Annotation>> annTypes) {
        return annTypes.contains(JaversAnnotationsNameSpace.DIFF_INCLUDE_ANN);
    }

    boolean hasShallowReferenceAnn(Set<Class<? extends Annotation>> annTypes) {
        return annTypes.contains(JaversAnnotationsNameSpace.SHALLOW_REFERENCE_ANN);
    }

    Optional<String> findTypeNameAnnValue(Set<Annotation> annotations) {
        return getAnnotationValue(annotations, JaversAnnotationsNameSpace.TYPE_NAME_ANN, typeNameAliases);
    }

    Optional<String> findPropertyNameAnnValue(Set<Annotation> annotations) {
        // Changed!!!
        Optional<Annotation> annotation = findAnnotation(annotations, DiffLog.class, typeNameAliases);
        return annotation.map(ann -> ReflectionUtil.getAnnotationValue(ann, "name"));
    }

    private Optional<String> getAnnotationValue(Set<Annotation> annotations, Class<? extends Annotation> javersAnnType, Set<String> aliases) {
        Optional<Annotation> annotation = findAnnotation(annotations, javersAnnType, aliases);
        return annotation.map(ann -> ReflectionUtil.getAnnotationValue(ann, "value"));
    }

    private Optional<Annotation> findAnnotation(Set<Annotation> annotations, Class<? extends Annotation> javersAnnType, Set<String> aliases) {
        Optional<Annotation> jTypeName = annotations.stream()
                .filter(ann -> javersAnnType.isAssignableFrom(ann.getClass()))
                .findAny();
        if (jTypeName.isPresent()) {
            return jTypeName;
        }

        return annotations.stream()
                .filter(ann -> aliases.contains(ann.annotationType().getSimpleName()))
                .findFirst();
    }
}
