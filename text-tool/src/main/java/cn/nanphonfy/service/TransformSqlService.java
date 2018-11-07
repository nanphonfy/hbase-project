package cn.nanphonfy.service;

import cn.nanphonfy.domain.Choice;
import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.domain.MedicalInsuranceRule;
import cn.nanphonfy.domain.Question;

import java.util.List;

/**
 * 根据表对象，转化为SQL脚本
 * Created by zhengshaorong on 2018/11/7.
 */
public interface TransformSqlService {
    void transformSqlofHealthQuestionnaireClassification( List<HealthQuestionnaireClassification> healthQuestionnaireClassifications,String filePath);
    void transformSqlofQuestion( List<Question> questions,String filePath);
    void transformSqlofChoice( List<Choice> choices,String filePath);
    void transformSqlofMedicalInsuranceRule( List<MedicalInsuranceRule> medicalInsuranceRules,String filePath);
}
