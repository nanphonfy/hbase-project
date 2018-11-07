package cn.nanphonfy.domain;

/**
 * 【医疗核保规则表】
 * Created by zhengshaorong on 2018/11/7.
 */
public class MedicalInsuranceRule {
    //ID	分类ID	层级	问题一ID	选项一ID	答案一	问题二ID			选项二ID	答案二	问题三ID	选项三ID	答案三	问题四ID	选项四ID	答案四	医疗核保结果	结果code	风险描述	重疾评点	寿险评点	补充意见
    private Integer id;
    private String classificationId;
    private Integer levelCount;
    private String questionId1;
    private String choiceId1;
    private String questionId2;
    private String choiceId2;
    private String questionId3;
    private String choiceId3;
    private String questionId4;
    private String choiceId4;
    //    结果code
    private String resultCode;
    //    风险描述
    private String riskDepiction;
    //    医疗核保
    private String medicalUnderwriting;
    //    重疾评点
    private String seriousHealthAdvice;
    //    寿险评点
    private String lifeInsuranceAdvice;
    //    补充意见
    private String additionalComment;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public Integer getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(Integer levelCount) {
        this.levelCount = levelCount;
    }

    public String getQuestionId1() {
        return questionId1;
    }

    public void setQuestionId1(String questionId1) {
        this.questionId1 = questionId1;
    }

    public String getChoiceId1() {
        return choiceId1;
    }

    public void setChoiceId1(String choiceId1) {
        this.choiceId1 = choiceId1;
    }

    public String getQuestionId2() {
        return questionId2;
    }

    public void setQuestionId2(String questionId2) {
        this.questionId2 = questionId2;
    }

    public String getChoiceId2() {
        return choiceId2;
    }

    public void setChoiceId2(String choiceId2) {
        this.choiceId2 = choiceId2;
    }

    public String getQuestionId3() {
        return questionId3;
    }

    public void setQuestionId3(String questionId3) {
        this.questionId3 = questionId3;
    }

    public String getChoiceId3() {
        return choiceId3;
    }

    public void setChoiceId3(String choiceId3) {
        this.choiceId3 = choiceId3;
    }

    public String getQuestionId4() {
        return questionId4;
    }

    public void setQuestionId4(String questionId4) {
        this.questionId4 = questionId4;
    }

    public String getChoiceId4() {
        return choiceId4;
    }

    public void setChoiceId4(String choiceId4) {
        this.choiceId4 = choiceId4;
    }

    public String getRiskDepiction() {
        return riskDepiction;
    }

    public void setRiskDepiction(String riskDepiction) {
        this.riskDepiction = riskDepiction;
    }

    public String getMedicalUnderwriting() {
        return medicalUnderwriting;
    }

    public void setMedicalUnderwriting(String medicalUnderwriting) {
        this.medicalUnderwriting = medicalUnderwriting;
    }

    public String getSeriousHealthAdvice() {
        return seriousHealthAdvice;
    }

    public void setSeriousHealthAdvice(String seriousHealthAdvice) {
        this.seriousHealthAdvice = seriousHealthAdvice;
    }

    public String getLifeInsuranceAdvice() {
        return lifeInsuranceAdvice;
    }

    public void setLifeInsuranceAdvice(String lifeInsuranceAdvice) {
        this.lifeInsuranceAdvice = lifeInsuranceAdvice;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public void setAdditionalComment(String additionalComment) {
        this.additionalComment = additionalComment;
    }
}
