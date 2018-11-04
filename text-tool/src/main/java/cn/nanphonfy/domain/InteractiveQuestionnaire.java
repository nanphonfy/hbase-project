package cn.nanphonfy.domain;

/**
 * @author nanphonfy(南风zsr)
 * @date 2018/11/3
 */
public class InteractiveQuestionnaire {
    //    系统分类
    private String classification;
    //    健康询问事项
    private String healthEnquirie;
    //    三级名称
    private String level3Name;
    //    ICD10代码
    private String ICD10Code;
    //    通用称谓
    private String commonAppellation;
    //    风险描述
    private String riskDepiction;
    //    一级问题
    private String firstLevelQuestion;
    //    一级答案
    private String firstLevelAnswer;
    //    二级问题
    private String secondLevelQuestion;
    //    二级答案
    private String secondLevelAnswer;
    //    三级问题
    private String ThirdLevelQuestion;
    //    三级答案
    private String ThirdLevelAnswer;
    //    医疗核保
    private String medicalUnderwriting;
    //    重疾评点
    private String seriousHealthAdvice;
    //    寿险评点
    private String lifeInsuranceAdvice;
    //    补充意见
    private String additionalComment;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getHealthEnquirie() {
        return healthEnquirie;
    }

    public void setHealthEnquirie(String healthEnquirie) {
        this.healthEnquirie = healthEnquirie;
    }

    public String getLevel3Name() {
        return level3Name;
    }

    public void setLevel3Name(String level3Name) {
        this.level3Name = level3Name;
    }

    public String getICD10Code() {
        return ICD10Code;
    }

    public void setICD10Code(String ICD10Code) {
        this.ICD10Code = ICD10Code;
    }

    public String getCommonAppellation() {
        return commonAppellation;
    }

    public void setCommonAppellation(String commonAppellation) {
        this.commonAppellation = commonAppellation;
    }

    public String getRiskDepiction() {
        return riskDepiction;
    }

    public void setRiskDepiction(String riskDepiction) {
        this.riskDepiction = riskDepiction;
    }

    public String getFirstLevelQuestion() {
        return firstLevelQuestion;
    }

    public void setFirstLevelQuestion(String firstLevelQuestion) {
        this.firstLevelQuestion = firstLevelQuestion;
    }

    public String getFirstLevelAnswer() {
        return firstLevelAnswer;
    }

    public void setFirstLevelAnswer(String firstLevelAnswer) {
        this.firstLevelAnswer = firstLevelAnswer;
    }

    public String getSecondLevelQuestion() {
        return secondLevelQuestion;
    }

    public void setSecondLevelQuestion(String secondLevelQuestion) {
        this.secondLevelQuestion = secondLevelQuestion;
    }

    public String getSecondLevelAnswer() {
        return secondLevelAnswer;
    }

    public void setSecondLevelAnswer(String secondLevelAnswer) {
        this.secondLevelAnswer = secondLevelAnswer;
    }

    public String getThirdLevelQuestion() {
        return ThirdLevelQuestion;
    }

    public void setThirdLevelQuestion(String thirdLevelQuestion) {
        ThirdLevelQuestion = thirdLevelQuestion;
    }

    public String getThirdLevelAnswer() {
        return ThirdLevelAnswer;
    }

    public void setThirdLevelAnswer(String thirdLevelAnswer) {
        ThirdLevelAnswer = thirdLevelAnswer;
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
