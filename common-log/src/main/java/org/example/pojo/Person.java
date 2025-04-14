package org.example.pojo;

import io.swagger.v3.oas.annotations.media.MetaFieldFeature;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@TypeName("人类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @Schema(description = "登录名")
    @DiffIgnore
    private String login;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "薪资")
    private BigDecimal salary;

    @Schema(description = "职位")
    @MetaFieldFeature(ignore = true)
    @DiffIgnore
    private Position position;

    @Override
    @SneakyThrows
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", Person.class.getSimpleName() + "[", "]");
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 检查字段是否有 DiffIgnore 注解
            if (field.isAnnotationPresent(DiffIgnore.class)) {
                continue; // 如果有 DiffIgnore 注解，跳过该字段
            }

            field.setAccessible(true);
            Schema schemaAnnotation = field.getAnnotation(Schema.class);
            if (schemaAnnotation != null && schemaAnnotation.description() != null) {
                joiner.add(schemaAnnotation.description() + "=" + field.get(this));
            }

        }
        return joiner.toString();
    }
}
