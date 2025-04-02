package org.example.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * SpEL表达式评估器，用于解析和执行Spring Expression Language (SpEL)表达式。
 * 该类通过AOP切面的连接点信息动态解析方法参数并评估表达式。
 *
 * @author guohao.lu
 */
@Component
public class SpelExpressionEvaluator {
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 评估给定的SpEL表达式并返回结果。
     *
     * @param joinPoint      AOP切面的连接点，包含目标方法的签名、参数和目标对象信息。
     * @param spelExpression 需要评估的SpEL表达式字符串。
     * @param clazz          表达式评估后的结果类型。
     * @return 表达式评估后的结果，类型取决于表达式的定义。
     * @throws IllegalArgumentException 如果表达式解析或评估失败，则抛出此异常。
     */
    public <T> T evaluate(ProceedingJoinPoint joinPoint, String spelExpression, Class<T> clazz) {
        try {
            // 获取方法签名和目标方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();
            Object target = joinPoint.getTarget();

            // 创建评估上下文，用于绑定方法参数和目标对象
            EvaluationContext context = new MethodBasedEvaluationContext(
                    target, method, args, PARAMETER_NAME_DISCOVERER);

            // 解析SpEL表达式并执行评估
            Expression expression = PARSER.parseExpression(spelExpression);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            // 捕获异常并抛出自定义异常，包含表达式内容和原始异常信息
            throw new IllegalArgumentException("SpEL表达式评估失败: " + spelExpression, e);
        }
    }
}
