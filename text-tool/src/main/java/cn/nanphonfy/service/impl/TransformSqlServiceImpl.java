package cn.nanphonfy.service.impl;

import cn.nanphonfy.domain.Choice;
import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.domain.MedicalInsuranceRule;
import cn.nanphonfy.domain.Question;
import cn.nanphonfy.service.TransformSqlService;
import cn.nanphonfy.util.TextFileUtil;

import java.util.List;

/**
 * Created by zhengshaorong on 2018/11/7.
 */
public class TransformSqlServiceImpl implements TransformSqlService {
    private static final String SYSDATE = "sysdate";
    private static final String SYSUSER = "SYSTEM@CMRH.COM";
    private static final String IS_VALID = "Y";
    private static final String VERSION = "1";

    /***
     * https://blog.csdn.net/Nanphonfy/article/details/52433586
     * @param healthQuestionnaireClassifications
     */
    @Override
    public void transformSqlofHealthQuestionnaireClassification(List<HealthQuestionnaireClassification> healthQuestionnaireClassifications,String filePath) {
        String tableName = "nburule.uw_questionaire_cat";
        String columns = "id,category,option_id,parent_id,cat_order,icd10_code,generic_title,is_valid,version,created_date,created_user,updated_date,updated_user";

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for(HealthQuestionnaireClassification obj:healthQuestionnaireClassifications){
            String values = String.format("'%s','%s',%s,'%s',%s,'%s','%s','%s','%s',%s,'%s',%s,'%s'", obj.getId(),
                    obj.getClassification(), obj.getChoiceId(), obj.getParentId(), obj.getSequence(), obj.getICD10(),
                    obj.getCommonAppellation(), IS_VALID, VERSION, SYSDATE, SYSUSER, SYSDATE, SYSUSER);
            //如果字符串为null，则去掉''
            String sqlScript = getSqlScript(tableName,columns,values).replace("'null'","null");
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }

        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    @Override
    public void transformSqlofQuestion(List<Question> questions,String filePath) {
        String tableName="nburule.uw_health_questions";
        String columns="id,question_content,question_order,question_type,parent_id,questionnaire_cat,version,created_date,updated_date,created_user,updated_user";

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for(Question obj:questions){
            String values = String.format("%s,'%s',%s,%s,%s,'%s','%s',%s,%s,'%s','%s'", obj.getId(), obj.getContent(),
                    obj.getSequence(), obj.getType(), obj.getParentId(), obj.getClassificationId(), VERSION, SYSDATE,
                     SYSDATE,SYSUSER, SYSUSER);
            //如果字符串为null，则去掉''
            String sqlScript = getSqlScript(tableName,columns,values).replace("'null'","null");
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }
        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    @Override
    public void transformSqlofChoice(List<Choice> choices,String filePath) {
        String tableName="nburule.uw_health_options";
        String columns="id,question_id,option_type,option_content,option_order,option_cat,mandatory,parent_id,display_sentence,version,created_date,created_user,updated_date,updated_user";

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for(Choice obj:choices){
            String values = String
                    .format("%s,%s,%s,'%s',%s,%s,'%s',%s,'%s','%s',%s,'%s',%s,'%s'", obj.getId(), obj.getQuestionId(),
                            obj.getType(), obj.getContent(), obj.getSequence(), obj.getCategory(), obj.getRequired(),
                            obj.getParentId(), obj.getPromptTerm(), VERSION, SYSDATE,SYSUSER, SYSDATE, SYSUSER);
            //如果字符串为null，则去掉''
            String sqlScript = getSqlScript(tableName, columns, values).replace("'null'", "null");
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }
        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    @Override
    public void transformSqlofMedicalInsuranceRule(List<MedicalInsuranceRule> medicalInsuranceRules,String filePath) {
        String tableName = "nburule.uw_medical_uw_rules";
        String columns = "id,cat_id,rule_level,question_id_1,option_id_1,ans_1,question_id_2,option_id_2,ans_2,question_id_3,option_id_3,ans_3,question_id_4,option_id_4,ans_4,medical_uw_result,result_code,risk_desc,serious_illness,lifeins_comment,addtion_content,version,created_date,created_user,updated_date,updated_user";

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for (MedicalInsuranceRule obj : medicalInsuranceRules) {
            String values = String
                    .format("%s,'%s',%s,%s,%s,'%s',%s,%s,'%s',%s,%s,'%s',%s,%s,'%s','%s','%s','%s','%s','%s','%s','%s',%s,'%s'，%s,'%s'",
                            obj.getId(), obj.getClassificationId(), obj.getLevelCount(), obj.getQuestionId1(),
                            obj.getChoiceId1(), obj.getAnswer1(), obj.getQuestionId2(), obj.getChoiceId2(),
                            obj.getAnswer2(), obj.getQuestionId3(), obj.getChoiceId3(), obj.getAnswer3(),
                            obj.getQuestionId4(), obj.getChoiceId4(), obj.getAnswer4(), obj.getMedicalUnderwriting(),
                            obj.getResultCode(), obj.getRiskDepiction(), obj.getSeriousHealthAdvice(),
                            obj.getLifeInsuranceAdvice(), obj.getAdditionalComment(),obj.getVersion(), SYSDATE, SYSUSER, SYSDATE,
                            SYSUSER);
            //如果字符串为null，则去掉''
            String sqlScript = getSqlScript(tableName, columns, values).replace("'null'", "null");
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }
        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    /**
     * 动态生成sql脚本
     * @param tableName
     * @param columns
     * @param values
     * @return
     */
    private String getSqlScript(String tableName, String columns, String values) {
        String title = String.format("insert into %s (%s)", tableName, columns);
        String body = String.format("values (%s);", values);
        String sql = String.format("%s\n%s\n", title, body);
        return sql;
    }
}
