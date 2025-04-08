package org.example;

import org.example.common.JaversObjectComparator;
import org.example.pojo.Address;
import org.example.pojo.Employee;
import org.example.pojo.EmployeeBuilder;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class JaversTest {
    /**
     * Javers实例，用于比较对象差异
     */
    private static final Javers JAVERS = JaversBuilder.javers()
            .withPrettyPrint(true)
            .registerValue(BigDecimal.class,
                    (a, b) -> a.compareTo(b) == 0,
                    a -> a.stripTrailingZeros().toString())
            .withListCompareAlgorithm(ListCompareAlgorithm.AS_SET)
            .build();

    @Test
    void testCompare() {
        Employee frodoOld = EmployeeBuilder.Employee("Frodo")
                .withAge(40)
                .withPosition("Townsman")
                .withSalary(10_000)
                .withPrimaryAddress(new Address("Shire"))
                .withSkills("management")
                .withSubordinates(new Employee("Sam"))
                .build();

        Employee frodoNew = EmployeeBuilder.Employee("Frodo")
                .withAge(41)
                .withPosition("Hero")
                .withBoss(new Employee("Gandalf"))
                .withPrimaryAddress(new Address("Mordor"))
                .withSalary(12_000)
                .withSkills("management", "agile coaching")
                .withSubordinates(new Employee("Sméagol"), new Employee("Sam"))
                .build();

        Diff compare = JAVERS.compare(frodoOld, frodoNew);
        String prettyPrint = compare.prettyPrint();
    }
}
