package cn.nanphonfy.service;

import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.domain.InteractiveQuestionnaire;

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
     */
    void parseQuestionAndChoiceTableNew(List<InteractiveQuestionnaire> questions,List<HealthQuestionnaireClassification> healthQuestionnaireClassifications);
}
