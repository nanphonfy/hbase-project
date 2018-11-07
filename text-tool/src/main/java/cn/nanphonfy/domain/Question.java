package cn.nanphonfy.domain;

/**
 * 【问题表】
 * @author nanphonfy(南风zsr)
 * @date 2018/11/6
 */
public class Question {
    //问题ID	问题内容	问题顺序	问题类型（普通/特殊）	父节点ID	健康问卷问题分类	版本号
    private String id;
    private String content;
    private String sequence;
    private String type;
    private String parentId;
    private String classificationId;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
