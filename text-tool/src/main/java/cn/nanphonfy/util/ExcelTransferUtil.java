package cn.nanphonfy.util;

import cn.nanphonfy.domain.InteractiveQuestionnaire;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhengshaorong on 2018/11/5.
 */
public class ExcelTransferUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelTransferUtil.class);

    /**
     * 从excel文件中读取信息，并实例化为对象
     * 适用于181102问卷Excel
     * @param newFile     拿到excel文件
     * @return
     */
    public static List<InteractiveQuestionnaire> parseInteractiveQuestionnaireFromExcel(File newFile) {
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
