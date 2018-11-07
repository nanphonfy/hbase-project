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

    /***
     * https://blog.csdn.net/Nanphonfy/article/details/52433586
     * @param healthQuestionnaireClassifications
     */
    @Override
    public void transformSqlofHealthQuestionnaireClassification(List<HealthQuestionnaireClassification> healthQuestionnaireClassifications,String filePath) {
        String tableName = "";
        String columns = "";
        String values = String.format("");

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for(HealthQuestionnaireClassification obj:healthQuestionnaireClassifications){
            String sqlScript = getSqlScript(tableName,columns,values);
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }

        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    @Override
    public void transformSqlofQuestion(List<Question> questions,String filePath) {
        String tableName="";
        String columns="";
        String values = String.format("");

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for(Question obj:questions){
            String sqlScript = getSqlScript(tableName,columns,values);
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }
        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    @Override
    public void transformSqlofChoice(List<Choice> choices,String filePath) {
        String tableName="";
        String columns="";
        String values = String.format("");

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for(Choice obj:choices){
            String sqlScript = getSqlScript(tableName,columns,values);
            textFileUtil.writeLine(sqlScript,textFileUtil.getBufferedWriter());
        }
        textFileUtil.releaseBufferedWriter(textFileUtil.getBufferedWriter());
    }

    @Override
    public void transformSqlofMedicalInsuranceRule(List<MedicalInsuranceRule> medicalInsuranceRules,String filePath) {
        String tableName = "";
        String columns = "";
        String values = String.format("");

        TextFileUtil textFileUtil = new TextFileUtil(filePath);
        for (MedicalInsuranceRule obj : medicalInsuranceRules) {
            String sqlScript = getSqlScript(tableName, columns, values);
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
        String title = String.format("insert %s into (%s)", tableName, columns);
        String body = String.format("values (%s);", values);
        String sql = String.format("%s\n%s\n", title, body);
        return sql;
    }
}
