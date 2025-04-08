package org.example;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.example.pojo.*;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

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
        Person tom = new Person("u1", "tom", Position.Hero);
        Person jack = new Person("u2", "jack", Position.Townsman);
        Person jerry = new Person("u3", "jerry", Position.Developer);
        Person mike = new Person("u4", "mike", Position.Saleswoman);

        Employee frodoOld = Employee.builder().name("Frodo")
                .age(40)
                .position(Position.Assistant)
                .salary(BigDecimal.valueOf(100.0000))
                .primaryAddress(new Address("Shire"))
                .skills(Collections.singleton("management"))
                .subordinates(null)
                .personList(Lists.newArrayList(tom, jack))
                .build();

        Employee frodoNew = Employee.builder().name("Frodo")
                .age(45)
                .position(Position.Developer)
                .salary(BigDecimal.valueOf(100))
                .primaryAddress(new Address("Addre11"))
                .skills(Collections.singleton("study"))
                .subordinates(new ArrayList<>())
                .personList(Lists.newArrayList(jerry, mike, jack))
                .build();

        Diff compare = JAVERS.compare(frodoOld, frodoNew);
        String prettyPrint = compare.prettyPrint();

        String strip = StringUtils.strip(prettyPrint, "Diff:\n");
        String replace = StringUtils.replace(strip, "\n", "<p>");
        String replaceNew = StringUtils.replace(replace, " ", "&nbsp;");
        System.out.println(replaceNew);

    }
}
