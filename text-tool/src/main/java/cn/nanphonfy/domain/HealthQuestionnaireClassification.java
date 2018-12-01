package cn.nanphonfy.domain;

/**
 * 【健康问卷分类表】
 * Created by zhengshaorong on 2018/11/5.
 */
public class HealthQuestionnaireClassification {
    // ID
    private String id;
    // 分类
    private String classification;
    // 选项ID
    private String choiceId;
    // 父节点ID
    private String parentId;
    // 顺序
    private String sequence;
    // 询问告知
    private String inquires;
    // ICD10代码
    private String ICD10;
    // 通用称谓
    private String commonAppellation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getICD10() {
        return ICD10;
    }

    public void setICD10(String ICD10) {
        this.ICD10 = ICD10;
    }

    public String getCommonAppellation() {
        return commonAppellation;
    }

    public void setCommonAppellation(String commonAppellation) {
        this.commonAppellation = commonAppellation;
    }

    public String getInquires() {
        return inquires;
    }

    public void setInquires(String inquires) {
        this.inquires = inquires;
    }
}
