package cn.nanphonfy.service;

import cn.nanphonfy.domain.*;

import java.util.List;

/**
 * Created by zhengshaorong on 2018/11/5.
 */
public interface QuestionnaireService {
    /**
     * 转换成【健康问卷分类表】
     * @param questions
     */
    List<HealthQuestionnaireClassification> parseHealthQuestionnaireClassification(List<InteractiveQuestionnaire> questions);

    /**
     * 转换成【问题表】和【选项表】
     * @param questions
     * @param healthQuestionnaireClassifications
     */
    void parseQuestionAndChoiceTable(List<InteractiveQuestionnaire> questions,List<HealthQuestionnaireClassification> healthQuestionnaireClassifications);

    /**
     * 转换成【问题表】和【选项表】
     * 整合版
     * @param questions
     * @param healthQuestionnaireClassifications
     * @param newQuestionList
     * @param newChoiceList
     */
    void parseQuestionAndChoiceTableNew(List<InteractiveQuestionnaire> questions, List<HealthQuestionnaireClassification> healthQuestionnaireClassifications, List<Question> newQuestionList, List<Choice> newChoiceList);

    /**
     * 转换成【医疗核保结果表】
     * @param questionnaires
     * @param newQuestionList
     * @param newChoiceList
     * @param newMedicalInsuranceRuleList
     * @param classficationMap
     */
    void parseMedicalInsuranceRuleTable(List<InteractiveQuestionnaire> questionnaires, List<Question> newQuestionList, List<Choice> newChoiceList, List<MedicalInsuranceRule> newMedicalInsuranceRuleList, List<HealthQuestionnaireClassification> classficationMap);

    /**
     * 增加病种为"其他"的 虚拟问题
     * @param newQuestionList
     * @param healthQuestionnaireClassifications
     */
    void addQuestionOther(List<Question> newQuestionList, List<HealthQuestionnaireClassification> healthQuestionnaireClassifications);
}
