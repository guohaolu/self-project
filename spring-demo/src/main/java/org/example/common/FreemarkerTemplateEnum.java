package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Freemarker模版
 */
@Getter
@AllArgsConstructor
public enum FreemarkerTemplateEnum {
    PARTY_BRANCH_GENERAL_MEETING("三会一课-党员大会", "test-v2.ftl", "党员大会");

    private final String name;
    private final String path;
    private final String desc;
}
