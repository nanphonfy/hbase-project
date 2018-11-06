package cn.nanphonfy;

import cn.nanphonfy.domain.InteractiveQuestion;
import cn.nanphonfy.domain.InteractiveQuestionnaire;
import cn.nanphonfy.service.DealWithService;
import cn.nanphonfy.service.QuestionnaireService;
import cn.nanphonfy.service.impl.DealWithServiceImpl;
import cn.nanphonfy.service.impl.QuestionnaireServiceImpl;
import cn.nanphonfy.util.ExcelTransferUtil;
import cn.nanphonfy.util.FastJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author nanphonfy(南风zsr)
 * @date 2018/11/3
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static final String PREFIX = "00";
    private static final String OTHER = "其他";

    public static void main(String[] args) {
        QuestionnaireService questionnaireService = new QuestionnaireServiceImpl();
        DealWithService dealWithService = new DealWithServiceImpl();
        //parent pathname,child pathname string，打开新文件
        File newFile = new File("C:\\Users\\NAN\\Desktop\\data\\重新整理-问卷-v1.0.1.xlsx");
//                File newFile = new File("D:\\code\\text-code\\重新整理-问卷-v1.0.1.xlsx");

        List<InteractiveQuestionnaire> list = ExcelTransferUtil.parseInteractiveQuestionnaireFromExcel(newFile);

        //按系统分类
        Map<String, List<InteractiveQuestionnaire>> classficationMap = dealWithService.getClassficationMap(list);
        questionnaireService.parseHealthQuestionnaireClassification(list);

        List<InteractiveQuestion> questions = new ArrayList<>();

        //把一条记录分成多行，父子节点的形式
        dealWithService.getQuestionByClassficationMap(questions,classficationMap);

        System.out.println(FastJsonUtil.toJson(questions));
        //System.out.println(JSONArray.parse(JSONObject.toJSONString(list, SerializerFeature.SortField), Feature.OrderedField));
    }
}
