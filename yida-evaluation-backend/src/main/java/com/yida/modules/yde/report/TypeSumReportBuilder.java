package com.yida.modules.yde.report;

import com.jeesite.common.lang.StringUtils;
import com.yida.modules.yde.api.v1.model.YdeAnswer;
import com.yida.modules.yde.entity.*;
import com.yida.modules.yde.service.YdeTypeGroupAnalysisService;
import com.yida.modules.yde.service.YdeTypeRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TypeSumReportBuilder extends AbstractReportBuilder {
    private final YdeTypeGroupAnalysisService ydeTypeGroupAnalysisService;
    private final YdeTypeRuleService ydeTypeRuleService;
    private final Map<String, YdeTypeRule> quesIdOptionToRuleMap;
    private final Map<String, YdeTypeGroupAnalysis> typeToAnalysisMap;
    private final MultiValueMap groupToTypesMap;

    public TypeSumReportBuilder(YdeTypeRuleService ydeTypeRuleService,
                                YdeTypeGroupAnalysisService ydeTypeGroupAnalysisService) {
        this.ydeTypeRuleService = ydeTypeRuleService;
        this.ydeTypeGroupAnalysisService = ydeTypeGroupAnalysisService;
        quesIdOptionToRuleMap = new HashMap<>();
        typeToAnalysisMap = new LinkedHashMap<>();
        groupToTypesMap = MultiValueMap.decorate(new HashMap(), LinkedHashSet.class);
    }

    @Override
    protected void preBuild() {
        YdeTypeRule where = new YdeTypeRule();
        where.setRuleId(Long.valueOf(rule.getId()));
        List<YdeTypeRule> rules = ydeTypeRuleService.findList(where);
        for (YdeTypeRule rule : rules) {
            String[] questionIds = rule.getQuestionIds() == null ? new String[0] : rule.getQuestionIds().split(",");
            for (String questionId : questionIds) {
                quesIdOptionToRuleMap.put(questionId + "_" + rule.getOptionName(), rule);
                groupToTypesMap.put(rule.getGroupNo(), rule.getType());
            }
        }

        YdeTypeGroupAnalysis analysisWhere = new YdeTypeGroupAnalysis();
        analysisWhere.setRuleId(Long.valueOf(rule.getId()));
        ydeTypeGroupAnalysisService.findList(analysisWhere).forEach(tga -> {
            Set<String> types = new TreeSet<>();
            types.add(tga.getType1());
            if (StringUtils.isNotBlank(tga.getType2())) {
                types.add(tga.getType2());
            }
            if (StringUtils.isNotBlank(tga.getType3())) {
                types.add(tga.getType3());
            }
            if (StringUtils.isNotBlank(tga.getType4())) {
                types.add(tga.getType4());
            }
            typeToAnalysisMap.put(getType(types), tga);
        });
    }

    @Override
    protected void buildReport() {
        report = new YdeReport();
        report.setTitle(evaluation.getName());  //标题
        report.setTotalScore(totalScore);
        report.setReportType(rule.getReportType());

        Map<String, Integer> typePointsSum = new HashMap();
        for (YdeAnswer answer : answers) {
            YdeQuestion question = questionIdMap.get(String.valueOf(answer.getQuestionId()));

            //获取分数
            Integer points = rule.getPointsSource() == YdeRule.RULE_SOURCE_USER ?
                    answer.getPoints() : question.getPoints(answer.getAnswer());
            if(points == null){
                throw new RuntimeException("分数配置不全或错误，请检查..");
            }

            //分组统计分值
            String answerOption = YdeQuestion.OPTION_TYPE_YESNO.equals(question.getOptionType()) ? answer.toYesNoAnswer() : answer.getAnswer();
            YdeTypeRule rule = quesIdOptionToRuleMap.get(answer.getQuestionId() + "_" + answerOption);
            if(rule == null){
                continue;
            }
            String type = rule.getType();   //类型
            Integer counter = typePointsSum.get(type);  //分数累加器
            if (counter == null) {
                typePointsSum.put(type, points);
            } else {
                typePointsSum.put(type, counter + points);
            }

            if(YdeQuestion.OPTION_TYPE_ALLOCATION.equals(question.getOptionType())){
                Integer totalPoints = question.getPoints();
                if(question.getOptions().size() == 2){
                    Integer optionAPoints = points;
                    Integer optionBPoints = totalPoints - optionAPoints;
                    for(Object key : groupToTypesMap.keySet()){
                        Collection twoTypes = groupToTypesMap.getCollection(key);
                        if(twoTypes.contains(type)){
                            Optional<String> optional = twoTypes.stream().filter(i-> !i.equals(type)).findFirst();
                            if(optional.isPresent()){
                                String typeB = optional.get();
                                Integer counterB = typePointsSum.get(typeB);  //B分数累加器
                                if (counterB == null) {
                                    typePointsSum.put(typeB, optionBPoints);
                                } else {
                                    typePointsSum.put(typeB, counterB + optionBPoints);
                                }
                            }
                        }
                    }
                }
            }

        }

        //如果是组内统计方法，则移除组内低分值的结果，只保留最高分值结果
        if(YdeRule.STATISTICS_METHOD_INTRA_GROUP.equals(rule.getStatisticsMethod())){
            Integer maxPoints = 0;
            String maxType = null;
            for(Object key : groupToTypesMap.keySet()){
                Set<String> types = (Set) groupToTypesMap.getCollection(key);
                for(String type: types){
                    Integer points = typePointsSum.get(type);
                    if(points != null && maxPoints <= points){  //因为types是按顺序加入，即使同分，最后的类型是结果
                        maxPoints = points;
                        maxType = type;
                    }
                }
                //移除组内不是结果的类型
                for(String type: types){
                    if(maxType != null && !maxType.equals(type)){
                        typePointsSum.remove(type);
                    }
                }
                maxPoints = 0;
                maxType = null;
            }

        }


        //计算用户得分
        LinkedHashMap<String, Integer> sortedSum = sortByValue(typePointsSum);
        if(sortedSum.entrySet().isEmpty()){
            log.error("Fail to get corresponding score from type list, score={}", typePointsSum);
            throw new RuntimeException("未找到分析结果!");
        }

        Map.Entry<String, Integer> entry = sortedSum.entrySet().iterator().next();
        Integer score = entry.getValue();

        YdeTypeGroupAnalysis analysis = matchAnalysis(sortedSum);

        if(analysis == null){
            log.error("Fail to get analysis, score={}", sortedSum);
            throw new RuntimeException("分析结果还没有定义");
        }
        report.setScore(score); //累计得分
        List<String> types = new ArrayList<>();
        if(StringUtils.isNotBlank(analysis.getType1())) types.add(analysis.getType1());
        if(StringUtils.isNotBlank(analysis.getType2())) types.add(analysis.getType2());
        if(StringUtils.isNotBlank(analysis.getType3())) types.add(analysis.getType3());
        if(StringUtils.isNotBlank(analysis.getType4())) types.add(analysis.getType4());
        report.setTypeContent(types.stream().collect(Collectors.joining("、")));

        report.setReportContent(analysis.getAnalysis());
        report.setBoxContent("<p style='margin-bottom: 5px;'><strong>匹配分析</strong></p><p>" + analysis.getBelongTo() + "</p>");
    }

    private YdeTypeGroupAnalysis matchAnalysis(Map<String, Integer> sortedResult) {
        int counter = 0;
        List<String> types = new ArrayList<>();
        List<String> keys = new ArrayList<>(sortedResult.keySet());
        Iterator<String> itr = keys.iterator();
        while (itr.hasNext() && counter < 4) {
            types.add(itr.next()); //最高分到四个类型
            counter++;
        }

        //先找 4个类型都符合
        String type = getType(types);
        log.info("Matching type:{}", type);
        YdeTypeGroupAnalysis analysis = typeToAnalysisMap.get(type);
        if (analysis != null) {
            return analysis;
        }

        //再找 3个类型都符合
        analysis = typeToAnalysisMap.get(getType(types.subList(0, 3)));
        if (analysis != null) {
            return analysis;
        }

        //再找 2个类型符合
        analysis = typeToAnalysisMap.get(getType(types.subList(0, 2)));
        if (analysis != null) {
            return analysis;
        }

        //最后找 1个类型符合
        analysis = typeToAnalysisMap.get(getType(types.subList(0, 1)));
        if (analysis != null) {
            return analysis;
        }

        return null;
    }

    private String getType(Collection<String> types) {
        return types.stream().sorted().collect(Collectors.joining("#"));
    }

    public static LinkedHashMap<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
