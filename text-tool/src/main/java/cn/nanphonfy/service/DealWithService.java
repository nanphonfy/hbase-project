package cn.nanphonfy.service;

import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.domain.InteractiveQuestion;
import cn.nanphonfy.domain.InteractiveQuestionnaire;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengshaorong on 2018/11/6.
 */
public interface DealWithService {
    /**
     * 根据系统分类，划分map
     * 
     * @param list
     * @return
     */
    Map<String, List<InteractiveQuestionnaire>> getClassficationMap(List<InteractiveQuestionnaire> list);

    /**
     * 根据系统分类，划分ID->obj
     * @param list
     * @return
     */
    Map<String, HealthQuestionnaireClassification> getHealthQuestionnaireClassificationMap(
            List<HealthQuestionnaireClassification> list);

    /**
     * 把一条记录分成多行，父子节点
     * @param questions
     * @param classficationMap
     */
    void getQuestionByClassficationMap(List<InteractiveQuestion> questions,Map<String, List<InteractiveQuestionnaire>> classficationMap);

    /**
     * 处理非交互式病种（读取TXT）
     * @param healthQuestionnaireClassifications
     */
    void dealTxtForHealthQuestionnaireClassification(List<HealthQuestionnaireClassification> healthQuestionnaireClassifications,String filePath);
}
