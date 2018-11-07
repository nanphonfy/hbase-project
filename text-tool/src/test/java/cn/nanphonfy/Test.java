package cn.nanphonfy;

import cn.nanphonfy.domain.*;
import cn.nanphonfy.service.DealWithService;
import cn.nanphonfy.service.QuestionnaireService;
import cn.nanphonfy.service.TransformSqlService;
import cn.nanphonfy.service.impl.DealWithServiceImpl;
import cn.nanphonfy.service.impl.QuestionnaireServiceImpl;
import cn.nanphonfy.service.impl.TransformSqlServiceImpl;
import cn.nanphonfy.util.ExcelTransferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于解析复杂的Excel文件
 * @author nanphonfy(南风zsr)
 * @date 2018/11/3
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static final String PREFIX = "00";
    private static final String OTHER = "其他";
    private static final String TXT_FILE_PATH = "D:\\code\\text-code\\其他.txt";
    private static final String EXCEL_FILE_PATH = "C:\\Users\\NAN\\Desktop\\data\\重新整理-问卷-v1.0.1.xlsx";
    private static final String SQL_FILE_PATH_1 = "C:\\Users\\NAN\\Desktop\\data\\1.sql";
//    private static final String EXCEL_FILE_PATH = "D:\\code\\text-code\\重新整理-问卷-v1.0.1.xlsx";

    public static void main(String[] args) {
        QuestionnaireService questionnaireService = new QuestionnaireServiceImpl();
        DealWithService dealWithService = new DealWithServiceImpl();
        //parent pathname,child pathname string，打开新文件
        File newFile = new File(EXCEL_FILE_PATH);

        List<InteractiveQuestionnaire> questionnaires = ExcelTransferUtil.parseInteractiveQuestionnaireFromExcel(newFile);

        List<InteractiveQuestion> questions = new ArrayList<>();
        // 按系统分类
        Map<String, List<InteractiveQuestionnaire>> classficationMap = dealWithService.getClassficationMap(questionnaires);
        //把一条记录分成多行，父子节点的形式
        dealWithService.getQuestionByClassficationMap(questions,classficationMap);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // 【健康问卷分类表】
        List<HealthQuestionnaireClassification> healthQuestionnaireClassifications = questionnaireService.parseHealthQuestionnaireClassification(questionnaires);
        // 【问题表】
        List<Question> newQuestionList = new ArrayList<>();
        // 【选项表】
        List<Choice> newChoiceList = new ArrayList<>();
        // 转换成【问题表】和【选项表】 整合版
        questionnaireService.parseQuestionAndChoiceTableNew(questionnaires, healthQuestionnaireClassifications, newQuestionList,newChoiceList);
        // add【问题表】的"其他"
        questionnaireService.addQuestionOther(newQuestionList,healthQuestionnaireClassifications);

        // 【选项表】
        List<MedicalInsuranceRule> newMedicalInsuranceRuleList = new ArrayList<>();
        // 转换成【医疗核保规则表】
        questionnaireService.parseMedicalInsuranceRuleTable(questionnaires,newQuestionList,newChoiceList,newMedicalInsuranceRuleList,healthQuestionnaireClassifications);

        // add【健康问卷分类表】非交互式病种
        //dealWithService.dealTxtForHealthQuestionnaireClassification(healthQuestionnaireClassifications,TXT_FILE_PATH);

        System.out.println(String.format("问卷分类表:%s,问题表:%s,选项表:%s,核保规则表:%s",healthQuestionnaireClassifications.size(),newQuestionList.size(),newChoiceList.size(),newMedicalInsuranceRuleList.size()));

        TransformSqlService transformSqlService = new TransformSqlServiceImpl();
        transformSqlService.transformSqlofHealthQuestionnaireClassification(healthQuestionnaireClassifications,SQL_FILE_PATH_1);
        //System.out.println(FastJsonUtil.toJson(newQuestionList));
        //System.out.println(FastJsonUtil.toJson(newChoiceList));

        //System.out.println(FastJsonUtil.toJson(questions));
        //System.out.println(JSONArray.parse(JSONObject.toJSONString(list, SerializerFeature.SortField), Feature.OrderedField));
    }
}
