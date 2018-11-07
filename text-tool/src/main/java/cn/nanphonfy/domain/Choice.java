package cn.nanphonfy.domain;

/**
 * 【选项表】
 * @author nanphonfy(南风zsr)
 * @date 2018/11/6
 */
public class Choice {
    // 选项ID 问题ID 选项类型 选项内容 选项顺序 选项类别 是否必填 是否显示下层选项 父节点ID 提示术语 是否有效 版本号
    private String id;
    private String questionId;
    private String type;
    private String content;
    private String sequence;
    private String category;
    private String required;
    private String substratum;
    private String parentId;
    private String promptTerm;
    private String isValid;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getSubstratum() {
        return substratum;
    }

    public void setSubstratum(String substratum) {
        this.substratum = substratum;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPromptTerm() {
        return promptTerm;
    }

    public void setPromptTerm(String promptTerm) {
        this.promptTerm = promptTerm;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
