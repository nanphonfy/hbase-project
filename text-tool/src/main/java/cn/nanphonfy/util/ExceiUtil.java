package cn.nanphonfy.util;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * poi报表解析工具类
 *
 * @param <T>
 * @author zhengshaorong
 */
public class ExceiUtil<T> {
    private static final Logger logger = LoggerFactory.getLogger(ExceiUtil.class);
    /**
     * @param file excel文件
     * @param err  回调
     * @return 最终要的结果
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<T> getEntity(File file, ExcelRowResultHandler<T> err) throws InvalidFormatException, IOException {
        // 1.得到一个book
        XSSFWorkbook book = new XSSFWorkbook(file);

        // 2.得到一个sheet
        XSSFSheet sheet = book.getSheetAt(0);

        // 3.调用一个方法，将sheet中的内容封装成List<List<Object>>
        List<List<Object>> ls = sheetToList(sheet);

        List<T> list = new ArrayList<>();
        for (List<Object> lo : ls) {
            // 将lo转换成T对象
            T t = err.invoke(lo); // 接口回调，最终是通过具体的接口实现类来完成对象封装
            list.add(t);
        }
        return list;
    }

    private List<List<Object>> sheetToList(Sheet sheet, int beginRow) {
        List<List<Object>> list = new ArrayList<>();
        // 遍历每一行
        for (int i = beginRow; i <= sheet.getLastRowNum(); i++) {
            List<Object> lo = new ArrayList<>();
            Row row = sheet.getRow(i);
            //https://segmentfault.com/q/1010000003733011
            //怎么用POI Excel 读取合并单元格的值？

            //遍历一行中的所有单元格
            eachRowCells(row, lo);
            // 将每一行的值装入到list中
            list.add(lo);
        }
        return list;
    }

    /**
     * 遍历一行中的所有单元格
     *
     * @param row
     * @param lo
     */
    private void eachRowCells(Row row, List<Object> lo) {
        if(row == null){
            logger.info("coloumn is:{}","null");
            return;
        }
        logger.info("coloumn size:{}",row.getLastCellNum());
        // 遍历每一列，eg.有四列，其中两列为空，只能扫描到其余的两列
        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if(cell == null){
                lo.add("");
            }else {
                lo.add(cell.getStringCellValue());
            }
            // 将每一列的值装入到lo中

            /*switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    lo.add(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    lo.add(cell.getStringCellValue());
                    break;
                default:
            }*/
        }
    }

    /**
     *
     * @param file 文件
     * @param err 回调函数
     * @param beginRow 从第几行开始读取
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<T> getEntity(File file, ExcelRowResultHandler<T> err, int beginRow) throws InvalidFormatException, IOException {
        // http://blog.csdn.net/u011598529/article/details/39233393,
        // org.apache.poi.openxml4j.exceptions.InvalidOperationException: Can't open the specified file: 'G:\data\testO.xls'
        // 1.得到一个book
//        XSSFWorkbook book = new XSSFWorkbook(file);
        InputStream is = new FileInputStream(file);
        Workbook book = createWorkbook(is);

        // 2.得到一个sheet
        Sheet sheet = book.getSheetAt(0);

        // 3.调用一个方法，将sheet中的内容封装成List<List<Object>>
        List<List<Object>> ls = sheetToList(sheet, beginRow);

        List<T> list = new ArrayList<>();
        for (List<Object> lo : ls) {
            // 将lo转换成T对象
            T t = err.invoke(lo); // 接口回调，最终是通过具体的接口实现类来完成对象封装
            list.add(t);
        }
        return list;
    }

    /**
     * 静态方法  解决创建Workbook 创建产生的问题
     * @param inp
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public Workbook createWorkbook(InputStream inp) throws IOException,InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("您的excel版本目前poi解析不了");
    }

    private List<List<Object>> sheetToList(XSSFSheet sheet) {
        List<List<Object>> list = new ArrayList<>();
        // 遍历每一行
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            List<Object> lo = new ArrayList<>();

            XSSFRow row = sheet.getRow(i);
            //遍历一行中的所有单元格
            eachRowCells(row, lo);
            // 将每一行的值装入到list中
            list.add(lo);
        }
        return list;
    }
}
