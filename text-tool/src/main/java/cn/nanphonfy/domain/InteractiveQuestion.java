package cn.nanphonfy.domain;

/**
 * 用于存放一级，二级，三级问题
 * @author nanphonfy(南风zsr)
 * @date 2018/11/4
 */
public class InteractiveQuestion {
    private String id;
    private String parentId;
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
    //    几级问题
    private String nLevelQuestion;
    //    几级答案
    private String nLevelAnswer;
    //    医疗核保
    private String medicalUnderwriting;
    //    重疾评点
    private String seriousHealthAdvice;
    //    寿险评点
    private String lifeInsuranceAdvice;
    //    补充意见
    private String additionalComment;

    public InteractiveQuestion(InteractiveQuestionnaire obj) {
        this.classification = obj.getClassification();
        this.healthEnquirie = obj.getHealthEnquirie();
        this.level3Name = obj.getLevel3Name();
        this.ICD10Code = obj.getICD10Code();
        this.commonAppellation = obj.getCommonAppellation();
        this.riskDepiction = obj.getRiskDepiction();
        this.medicalUnderwriting = obj.getMedicalUnderwriting();
        this.seriousHealthAdvice = obj.getSeriousHealthAdvice();
        this.lifeInsuranceAdvice = obj.getLifeInsuranceAdvice();
        this.additionalComment = obj.getAdditionalComment();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getnLevelQuestion() {
        return nLevelQuestion;
    }

    public void setnLevelQuestion(String nLevelQuestion) {
        this.nLevelQuestion = nLevelQuestion;
    }

    public String getnLevelAnswer() {
        return nLevelAnswer;
    }

    public void setnLevelAnswer(String nLevelAnswer) {
        this.nLevelAnswer = nLevelAnswer;
    }

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
