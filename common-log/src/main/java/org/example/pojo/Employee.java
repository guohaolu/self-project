package org.example.pojo;

import io.swagger.v3.oas.annotations.media.MetaFieldFeature;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bartosz walacik
 */
@TypeName("员工")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @Schema(description = "姓名111")
    private String name;

    private Position position;

    private BigDecimal salary;

    @MetaFieldFeature(ignore = true)
    private Integer age;

    private List<Employee> subordinates = new ArrayList<>();

    private List<Person> personList = new ArrayList<>();

    @Schema(description = "地址")
    private Address primaryAddress;

    private Address postalAddress;

    private Set<String> skills;

    private Map<Integer, String> performance;

    private ZonedDateTime lastPromotionDate;
}
