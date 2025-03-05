package org.example.demo.controller;

import com.google.common.collect.ImmutableMap;
import org.example.common.FreemarkerTemplateBO;
import org.example.common.FreemarkerTemplateEnum;
import org.example.common.WordManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/api/v1/report")
public class WordController {
    private static final DateTimeFormatter DOT_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Resource
    private WordManager wordManager;

    @PostMapping("/word/export")
    public void exportWord(HttpServletResponse response) {
        Map<String, Object> dataModel = ImmutableMap.<String, Object>builder()
                .put("schoolName", "上海市普陀区曹杨实验小学")
                .put("meetingName", "党员大会")
                .put("meetingDate", LocalDate.now().format(DOT_FORMATTER))
                .put("meetingPlace", "会议室")
                .put("meetingOwner", "严泳旻")
                .put("recorder", "徐淑蕾")
                .put("users", Arrays.asList("张三", "李四", "王五", "赵七"))
                .put("topic", "推选黄雯静、李元基同志为入党积极分子推优会议")
                .put("start", "今天是学校党支部召开推选入党积极分子推优会议，大会现在开始。")
                .put("mid", "在前期党小组酝酿的基础上，我们推荐了入党积极分子，请个党小组发表意见。" +
                        "朱艳婷：我代表第一党小组发言，我们组10位党员经商议，共同推荐黄雯静、李元基为入党积极分子。" +
                        "吴佳：我代表第二党小组发言，我们组9位党员经商议，共同推荐黄雯静、李元基为入党积极分子。" +
                        "俞洁霞：我代表第三党小组发言，我们组10位党员经商议，共同推荐黄雯静、李元基为入党积极分子。")
                .put("end", "请黄雯静同志的入党推荐人代表沈琳、顾艳琼，李元基同志的入党推荐人代表姜薇、徐淑蕾分别" +
                        "介绍王瑾同志近年来的思想政治、学习、教育教学工作情况及主要优缺点，视其是否具备入党积极分子条件。")
                .build();
        FreemarkerTemplateBO templateBO = FreemarkerTemplateBO.builder()
                .title("党员大会")
                .dataModel(dataModel)
                .type(FreemarkerTemplateEnum.PARTY_BRANCH_GENERAL_MEETING)
                .build();
        wordManager.export(templateBO, response);
    }

}
