package cn.nanphonfy;

import cn.nanphonfy.domain.InteractiveQuestion;
import cn.nanphonfy.domain.InteractiveQuestionnaire;
import cn.nanphonfy.util.ExceiUtil;
import cn.nanphonfy.util.ExcelRowResultHandler;
import cn.nanphonfy.util.FastJsonUtil;
import cn.nanphonfy.util.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nanphonfy(南风zsr)
 * @date 2018/11/3
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        //parent pathname,child pathname string，打开新文件
        File newFile = new File("C:\\Users\\NAN\\Desktop\\data\\重新整理-问卷-v1.0.0.xlsx");

        List<InteractiveQuestionnaire> list = parseInteractiveQuestionnaireFromExcel(newFile);

        //按系统分类
        Map<String, List<InteractiveQuestionnaire>> classficationMap = getClassficationMap(list);

        List<InteractiveQuestion> questions=new ArrayList<>();

        //把一条记录分成多行，父子节点的形式
        getQuestionByClassficationMap(questions,classficationMap);

        System.out.println(FastJsonUtil.toJson(questions));
        //System.out.println(JSONArray.parse(JSONObject.toJSONString(list, SerializerFeature.SortField), Feature.OrderedField));
    }

    /**
     * 把一条记录分成多行，父子节点
     * @param questions
     * @param classficationMap
     */
    private static void getQuestionByClassficationMap(List<InteractiveQuestion> questions,Map<String, List<InteractiveQuestionnaire>> classficationMap) {
        int i =1;

        for (Map.Entry<String, List<InteractiveQuestionnaire>> entry : classficationMap.entrySet()) {
            for(InteractiveQuestionnaire obj:entry.getValue()){
                if(StringUtil.isEmpty(obj.getFirstLevelQuestion())){
                    continue;
                }
                InteractiveQuestion q1 = new InteractiveQuestion(obj);
                q1.setId(i+"");
                q1.setnLevelQuestion(obj.getFirstLevelQuestion());
                q1.setnLevelAnswer(obj.getFirstLevelAnswer());
                i++;
                questions.add(q1);

                if(StringUtil.isEmpty(obj.getSecondLevelQuestion())){
                    continue;
                }
                InteractiveQuestion q2 = new InteractiveQuestion(obj);
                q2.setId(i+"");
                q2.setParentId(q1.getId());
                q2.setnLevelQuestion(obj.getSecondLevelQuestion());
                q2.setnLevelAnswer(obj.getSecondLevelAnswer());
                i++;
                questions.add(q2);

                if(StringUtil.isEmpty(obj.getThirdLevelQuestion())){
                    continue;
                }
                InteractiveQuestion q3 = new InteractiveQuestion(obj);
                q3.setId(i+"");
                q3.setParentId(q2.getId());
                q3.setnLevelQuestion(obj.getThirdLevelQuestion());
                q3.setnLevelAnswer(obj.getThirdLevelAnswer());
                i++;
                questions.add(q3);
            }
        }
    }

    /**
     * 根据系统分类，划分map
     * @param list
     * @return
     */
    private static Map<String,List<InteractiveQuestionnaire>> getClassficationMap(List<InteractiveQuestionnaire> list) {
        //按系统分类
        Map<String, List<InteractiveQuestionnaire>> map = new HashMap<>();
        for (InteractiveQuestionnaire obj : list) {
            //系统分类
            String key = obj.getClassification();
            if(StringUtil.isEmpty(key)){
                continue;
            }
            if (map.containsKey(key)) {
                map.get(key).add(obj);
                map.put(key, map.get(key));
            } else {
                List<InteractiveQuestionnaire> tempList = new ArrayList<>();
                tempList.add(obj);
                map.put(key, tempList);
            }
        }
        return map;
    }

    /**
     * 从excel文件中读取信息，并实例化为对象
     *
     * @param newFile     拿到excel文件
     * @return
     */
    private static List<InteractiveQuestionnaire> parseInteractiveQuestionnaireFromExcel(File newFile) {
        ExceiUtil<InteractiveQuestionnaire> eu = new ExceiUtil<>();
        try {
            List<InteractiveQuestionnaire> list = null;
            try {
                list = eu.getEntity(newFile, new ExcelRowResultHandler<InteractiveQuestionnaire>() {
                    @Override public InteractiveQuestionnaire invoke(List<Object> data) {
                        InteractiveQuestionnaire line = new InteractiveQuestionnaire();

                        if (data.size() < 14) {
                            logger.error("size : {}",data.size());
                            return line;
                        }

                        int i;

                        i = 0;
                        if (data.get(i) != null) {
                            line.setClassification(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 1;
                        if (data.get(i) != null) {
                            line.setHealthEnquirie(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 2;
                        if (data.get(i) != null) {
                            line.setLevel3Name(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 3;
                        if (data.get(i) != null) {
                            line.setICD10Code(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 4;
                        if (data.get(i) != null) {
                            line.setCommonAppellation(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 5;
                        if (data.get(i) != null) {
                            line.setRiskDepiction(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 6;
                        if (data.get(i) != null) {
                            line.setFirstLevelQuestion(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 7;
                        if (data.get(i) != null) {
                            line.setFirstLevelAnswer(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 8;
                        if (data.get(i) != null) {
                            line.setSecondLevelQuestion(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 9;
                        if (data.get(i) != null) {
                            line.setSecondLevelAnswer(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 10;
                        if (data.get(i) != null) {
                            line.setThirdLevelQuestion(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 11;
                        if (data.get(i) != null) {
                            line.setThirdLevelAnswer(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 12;
                        if (data.get(i) != null) {
                            line.setMedicalUnderwriting(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 13;
                        if (data.get(i) != null) {
                            line.setSeriousHealthAdvice(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 14;
                        if (data.get(i) != null) {
                            line.setLifeInsuranceAdvice(StringUtil.getStr(data.get(i), ""));
                        }
                        i = 15;
                        if(data.size() < 16){
                            line.setAdditionalComment("");
                        }else {
                            if (data.get(i) != null) {
                                line.setAdditionalComment(StringUtil.getStr(data.get(i), ""));
                            }
                        }
                        return line;
                    }
                });
            } catch (InvalidFormatException e) {
                logger.error(e.getMessage());
            }
            return list;
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
        }
        return null;
    }
}
