package cn.nanphonfy.service.impl;

import cn.nanphonfy.domain.HealthQuestionnaireClassification;
import cn.nanphonfy.domain.InteractiveQuestion;
import cn.nanphonfy.domain.InteractiveQuestionnaire;
import cn.nanphonfy.service.DealWithService;
import cn.nanphonfy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengshaorong on 2018/11/6.
 */
public class DealWithServiceImpl implements DealWithService {
    private static final Logger logger = LoggerFactory.getLogger(DealWithServiceImpl.class);
    /**
     * 根据系统分类，划分map
     *
     * @param list
     * @return
     */
    @Override
    public Map<String, List<InteractiveQuestionnaire>> getClassficationMap(
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

    /**
     * 把一条记录分成多行，父子节点
     *
     * @param questions
     * @param classficationMap
     */
    @Override
    public void getQuestionByClassficationMap(List<InteractiveQuestion> questions,Map<String, List<InteractiveQuestionnaire>> classficationMap) {
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

    @Override
    public void dealTxtForHealthQuestionnaireClassification(List<HealthQuestionnaireClassification> healthQuestionnaireClassifications,String filePath) {
        File newFile = null;
        try {
            newFile = new File(filePath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(newFile)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] arr = line.split("、");
                for(int i=1;i<arr.length;i++){
                    HealthQuestionnaireClassification  obj= new HealthQuestionnaireClassification();
                    obj.setParentId(arr[0]);
                    obj.setId(StringUtil.getSequenceId(arr[0],i,-1));
                    obj.setClassification(StringUtil.removeDigital(arr[i]));
                    //顺序
                    obj.setSequence(String.valueOf(i));

                    healthQuestionnaireClassifications.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
