package org.example.common;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * <p>
 * 该注解用于标记需要记录操作日志的方法。通过该注解，可以灵活配置是否启用日志记录、操作类型以及业务ID和额外参数的SpEL表达式。
 *
 * @author guohao.lu
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 是否启用操作日志记录。
     *
     * @return true 表示启用操作日志记录，false 表示禁用。默认值为 true。
     */
    boolean enable() default true;

    /**
     * 操作类型，用于标识当前操作的类别。
     *
     * @return 操作类型的枚举值，默认值为 OperationLogEnum.OTHER。
     */
    OperationLogEnum operationType() default OperationLogEnum.OTHER;

    /**
     * 业务ID的SpEL表达式。
     * <p>
     * 该表达式用于动态获取业务ID，通常与方法参数或上下文相关联。
     *
     * @return SpEL表达式字符串，默认值为空字符串。
     */
    String busId() default "";

    /**
     * 额外参数的SpEL表达式。
     * <p>
     * 该表达式用于动态获取额外的参数信息，通常用于补充日志记录的内容。
     *
     * @return SpEL表达式字符串，默认值为空字符串。
     */
    String extraParams() default "";

    /**
     * 是否是单条操作
     *
     * @return true 表示是单条操作，false 表示不是单条操作。默认值为 false。
     */
    boolean isSingle() default false;
}
