package cn.sibat.hbase.take.util;

import cn.sibat.hbase.take.AbstractTest;
import cn.sibat.hbase.take.entity.BusRoad;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

/**
 * Created by nanphonfy on 2017/8/16.
 */
public class BusRoadsTest extends AbstractTest {
    @Test
    public void josnTest() throws ParseException {
        logger.info("{}", JacksonUtil.jsonToList(BusRoads.jsonString, BusRoad.class));
        List<BusRoad> list = JacksonUtil.jsonToList(BusRoads.jsonString, BusRoad.class);
        Map<String, List<String>> map = new HashMap<>();
        for (BusRoad br : list) {
            logger.info("{}", separate(br.getNs()));
            logger.info("{}", separate(br.getWe()));
            List<List<String>> nses = separate(br.getNs());
            List<List<String>> wes = separate(br.getWe());
            if (!CollectionUtils.isEmpty(nses)) {
                map.put(br.getName() + DirectionEnum.NS.getDesc(), nses.get(0));
                map.put(br.getName() + DirectionEnum.SN.getDesc(), nses.get(1));
            }
            if (!CollectionUtils.isEmpty(wes)) {
                map.put(br.getName() + DirectionEnum.WE.getDesc(), wes.get(0));
                map.put(br.getName() + DirectionEnum.EW.getDesc(), wes.get(1));
            }
        }
        logger.info("{}", map);

        for (String key : map.keySet()) {
            logger.info("{}", map.get(key));
            for (String roadId : map.get(key)) {
                //填充道路ID到10位数
                String preffix = "";
                for (int i = 0; i < 10 - roadId.length(); i++) {
                    preffix += "0";
                }
                String filled_roads = preffix + roadId;
                ResultScanner rs = HBaseDAO.scanByColumnRangeFilter("GdRoadNew", filled_roads + ":" + "17-08-01 05:00:00", filled_roads + ":" + "17-08-01 23:00:00", "001", "288");

                for (Result r : rs) {
                    for (KeyValue kv : r.raw()) {
                        logger.info("{},{}", Bytes.toString(kv.getQualifier()), Bytes.toString(kv.getValue()));
                    }
                }
            }
        }
    }

    private List<List<String>> separate(String direction) {
        List<List<String>> list = new ArrayList<>(2);
        if (StringUtils.isEmpty(direction) || direction.split("-").length < 2) {
        } else {
            List<String> one = Arrays.asList(direction.split("-")[0].split("/"));
            List<String> second = Arrays.asList(direction.split("-")[1].split("/"));
            list.add(one);
            list.add(second);
        }
        return list;
    }

    enum DirectionEnum {
        WE("西东", "we"), EW("东西", "ew"), NS("北南", "ns"), SN("南北", "sn");

        /**
         * 枚举值
         */
        private String value;

        /**
         * 描述
         */
        private String desc;

        private DirectionEnum(String desc, String value) {
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
