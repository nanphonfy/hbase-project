package cn.nanphonfy;

import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.domain.InteractiveQuestion;
import cn.nanphonfy.domain.InteractiveQuestionnaire;
import cn.nanphonfy.util.ExcelTransferUtil;
import cn.nanphonfy.util.FastJsonUtil;
import cn.nanphonfy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nanphonfy(南风zsr)
 * @date 2018/11/3
 */
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    private static final String PREFIX = "00";
    private static final String OTHER = "其他";

    public static void main(String[] args) {
        //parent pathname,child pathname string，打开新文件
        File newFile = new File("C:\\Users\\NAN\\Desktop\\data\\重新整理-问卷-v1.0.1.xlsx");
        //        File newFile = new File("D:\\【项目需求】\\11月份契约电子化\\REQ-3415（健康告知交互式）\\取数\\重新整理-问卷-v1.0.0.xlsx");

        List<InteractiveQuestionnaire> list = ExcelTransferUtil.parseInteractiveQuestionnaireFromExcel(newFile);

        //按系统分类
        Map<String, List<InteractiveQuestionnaire>> classficationMap = getClassficationMap(list);
        parseHealthQuestionnaireClassification(list);

        List<InteractiveQuestion> questions = new ArrayList<>();

        //把一条记录分成多行，父子节点的形式
        //getQuestionByClassficationMap(questions,classficationMap);

        System.out.println(FastJsonUtil.toJson(questions));
        //System.out.println(JSONArray.parse(JSONObject.toJSONString(list, SerializerFeature.SortField), Feature.OrderedField));
    }

    /**
     * 把一条记录分成多行，父子节点
     *
     * @param questions
     * @param classficationMap
     */
    private static void getQuestionByClassficationMap(List<InteractiveQuestion> questions,
            Map<String, List<InteractiveQuestionnaire>> classficationMap) {
        int i = 1;

        for (Map.Entry<String, List<InteractiveQuestionnaire>> entry : classficationMap.entrySet()) {
            for (InteractiveQuestionnaire obj : entry.getValue()) {
                if (StringUtil.isEmpty(obj.getFirstLevelQuestion())) {
                    continue;
                }
                InteractiveQuestion q1 = new InteractiveQuestion(obj);
                q1.setId(i + "");
                q1.setnLevelQuestion(obj.getFirstLevelQuestion());
                q1.setnLevelAnswer(obj.getFirstLevelAnswer());
                i++;
                questions.add(q1);

                if (StringUtil.isEmpty(obj.getSecondLevelQuestion())) {
                    continue;
                }
                InteractiveQuestion q2 = new InteractiveQuestion(obj);
                q2.setId(i + "");
                q2.setParentId(q1.getId());
                q2.setnLevelQuestion(obj.getSecondLevelQuestion());
                q2.setnLevelAnswer(obj.getSecondLevelAnswer());
                i++;
                questions.add(q2);

                if (StringUtil.isEmpty(obj.getThirdLevelQuestion())) {
                    continue;
                }
                InteractiveQuestion q3 = new InteractiveQuestion(obj);
                q3.setId(i + "");
                q3.setParentId(q2.getId());
                q3.setnLevelQuestion(obj.getThirdLevelQuestion());
                q3.setnLevelAnswer(obj.getThirdLevelAnswer());
                i++;
                questions.add(q3);
            }
        }
    }

    /**
     * 健康问卷分类
     *
     * @param questions
     */
    private static void parseHealthQuestionnaireClassification(List<InteractiveQuestionnaire> questions) {
        String prefix = "00";

        Map<String, String> firstMap = new HashMap<>();
        Map<String, String> secondMap = new HashMap<>();
        Map<String, String> thirdMap = new HashMap<>();

        List<String> firstList = questions.stream().map(s -> s.getClassification()).distinct()
                .collect(Collectors.toList());
        List<String> secondList = questions.stream().map(s -> s.getICD10Code()).distinct().collect(Collectors.toList());
        List<String> thirdList = questions.stream().map(s -> s.getLevel3Name()).distinct().collect(Collectors.toList());

        List<HealthQuestionnaireClassification> firstNewList = new ArrayList();
        List<HealthQuestionnaireClassification> seconndNewList = new ArrayList();

        int firstrow = 1;
        //遍历第一层父节点
        for (String classification : firstList) {
            //空值或null值
            if (StringUtil.isEmpty(classification)) {
                continue;
            }
            for (InteractiveQuestionnaire obj : questions) {
                int i = 1;
                //匹配到分类
                if (!StringUtil.isEmpty(obj.getClassification()) && obj.getClassification().equals(classification)) {
                    HealthQuestionnaireClassification classification1 = new HealthQuestionnaireClassification();
                    classification1.setId(StringUtil.getSequenceId(prefix, firstrow, i, -1));
                    classification1.setClassification(classification);
                    firstNewList.add(classification1);

                    i++;
                    HealthQuestionnaireClassification classification2 = new HealthQuestionnaireClassification();
                    classification2.setId(StringUtil.getSequenceId(prefix, firstrow, i, -1));
                    classification2.setClassification(OTHER);
                    firstNewList.add(classification2);
                    break;
                }
            }
            firstrow++;
        }

        //系统类型计数器，用于生成主键策略
        Map<String, Integer> classificationCountMap = new HashMap<>();
        /*A | AA  -> A,B
        A | AB   ->AA,AB,BA,BB->AA A +1,AB A +2....
        A | AB
        B | BA
        B | BB
        */
        //遍历病种
        for (String level3Name : thirdList) {
            //遍历第一层 系统分类
            for (InteractiveQuestionnaire obj : questions) {
                //匹配到病种，取对应的parentId并对ICD10分类
                if (!StringUtil.isEmpty(obj.getLevel3Name()) && obj.getLevel3Name().equals(level3Name)) {
                    //遍历系统类型
                    for (HealthQuestionnaireClassification firstObj : firstNewList) {
                        //获取相同的系统类型，抓出父ID
                        if (firstObj.getClassification().equals(obj.getClassification())) {
                            //三级病种层有多层，则累加
                            String key = obj.getClassification();
                            if (classificationCountMap.containsKey(key)) {
                                classificationCountMap.put(key, classificationCountMap.get(key) + 1);
                            } else {
                                classificationCountMap.put(key, 1);
                            }

                            //封装
                            HealthQuestionnaireClassification classification1 = new HealthQuestionnaireClassification();
                            classification1.setId(StringUtil
                                    .getSequenceId(firstObj.getId(), classificationCountMap.get(key), -1, -1));
                            classification1.setClassification(level3Name);
                            classification1.setParentId(firstObj.getId());

                            String icd10 = obj.getICD10Code();
                            //转换成我们想要的ICD10
                            classification1
                                    .setICD10(icd10.substring(icd10.indexOf("[")).replace("[", "").replace("]", ""));
                            seconndNewList.add(classification1);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        List<HealthQuestionnaireClassification> healthQuestionnaireClassifications = new ArrayList<>();
        healthQuestionnaireClassifications.addAll(firstNewList);
        healthQuestionnaireClassifications.addAll(seconndNewList);

        System.out.println(FastJsonUtil.toJson(healthQuestionnaireClassifications));
    }

    /**
     * 根据系统分类，划分map
     *
     * @param list
     * @return
     */
    private static Map<String, List<InteractiveQuestionnaire>> getClassficationMap(
            List<InteractiveQuestionnaire> list) {
        //按系统分类
        Map<String, List<InteractiveQuestionnaire>> map = new HashMap<>();
        for (InteractiveQuestionnaire obj : list) {
            //系统分类
            String key = obj.getClassification();
            if (StringUtil.isEmpty(key)) {
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
}
