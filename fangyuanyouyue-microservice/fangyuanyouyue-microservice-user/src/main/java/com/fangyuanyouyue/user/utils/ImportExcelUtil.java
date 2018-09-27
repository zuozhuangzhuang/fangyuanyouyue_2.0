//package com.fangyuanyouyue.user.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.fangyuanyouyue.base.enums.ReCode;
//import com.fangyuanyouyue.base.exception.ServiceException;
//import com.fangyuanyouyue.user.model.UserInfo;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class ImportExcelUtil {
//
//    private final static String XLS = "xls";
//    public static final String XLSX = "xlsx";
//
//    private final static Logger logger = LoggerFactory.getLogger(ImportExcelUtil.class);
//
//    public Integer importExcel(MultipartFile myFile) throws ServiceException {
//        //1.  使用HSSFWorkbook 打开或者创建 “Excel对象”
//        //2.  用HSSFWorkbook返回对象或者创建sheet对象
//        //3.  用sheet返回行对象，用行对象得到Cell对象
//        //4.  对Cell对象进行读写
//        List<UserInfo> users = new ArrayList<>();
//        Workbook workbook = null;
//        String fileName = myFile.getOriginalFilename();//  获取文件名
//        logger.info("【fileName】{}",fileName);
//        if (fileName.endsWith(XLS))
//        {
//            try {
//                workbook = new HSSFWorkbook(myFile.getInputStream());//  2003版本
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else if(fileName.endsWith(XLSX)){
//            try {
//                workbook = new XSSFWorkbook(myFile.getInputStream());//  2007版本
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else{
//            throw new ServiceException(ReCode.FILE_IS_NOT_EXCEL.getValue(),ReCode.FILE_IS_NOT_EXCEL.getMessage()); // 文件不是Excel文件
//        }
//        Sheet sheet = workbook.getSheet("sheet1");
//        int rows = sheet.getLastRowNum();
//        logger.info("【rows】{}",rows);
//        if(rows == 0){
//            throw  new ServiceException(ReCode.DATA_IS_NULL.getValue(),ReCode.DATA_IS_NULL.getMessage());// 数据为空 请填写数据
//        }
//        long startTime = System.currentTimeMillis();
//        for(int i = 1;i<= rows+1;i++){
//            Row row = sheet.getRow(i);
//
////            if(row !=null){
////
////                UserInfo user = new UserInfo();
////                //  部门
////                String department = getCellValue(row.getCell(0));
////                tbagent.setDepartment(department);
////                //  用户姓名
////                String username = getCellValue(row.getCell(1));
////                tbagent.setUsername(username);
////                //  工号
////                String  jobNumer = getCellValue(row.getCell(2));
////                if(!StringUtils.isEmpty(jobNumer)){
////                    Integer job_number = Integer.parseInt(jobNumer);
////                    tbagent.setJob_number(Integer.valueOf(jobNumer));
////                }
////                // 身份证后六位
////                String idcard = getCellValue(row.getCell(3));
////                tbagent.setIdcard(idcard);
////                // 公司排名
////                String companyRankings = getCellValue(row.getCell(4));
////                if(!StringUtils.isEmpty(companyRankings)){
////                    Integer new_companyRankings = Integer.parseInt(companyRankings);
////                    tbagent.setCompany_rankings(new_companyRankings);
////                }
////                // 事业部排名
////                String departmenRanks = getCellValue(row.getCell(5));
////                if(!StringUtils.isEmpty(departmenRanks)){
////                    Integer new_departmentRanks = Integer.parseInt(departmenRanks);
////                    tbagent.setDepartment_rankings(new_departmentRanks);
////                }
////                // 大区排名
////                String region_Ranks = getCellValue(row.getCell(6));
////                if(!StringUtils.isEmpty(region_Ranks)){
////                    Integer new_region_Rankings = Integer.parseInt(region_Ranks);
////                    //logger.info("【大区排名】{}",new_region_Rankings);
////                    tbagent.setRegion_rankings(new_region_Rankings);
////                }//  距离公司第一名差距
////                String distance_first_company = getCellValue(row.getCell(7));
////                if(!StringUtils.isEmpty(distance_first_company)){
////                    Long new_distance_first_company = Long.parseLong(distance_first_company);
////                    tbagent.setDistance_first_company(new_distance_first_company);
////                }
////                // 距离事业部第一名差距
////                String distance_first_deparment = getCellValue(row.getCell(8));
////                if(!StringUtils.isEmpty(distance_first_deparment)){
////                    Long new_distance_first_deparment = Long.parseLong(distance_first_deparment);
////                    tbagent.setDistance_first_department(new_distance_first_deparment);
////                }
////                // 距离大区第一名差距
////                String distance_first_region = getCellValue(row.getCell(9));
////                if(!StringUtils.isEmpty(distance_first_region)){
////                    Long new_distance_first_region = Long.parseLong(distance_first_region);
////                    tbagent.setDistance_first_region(new_distance_first_region);
////                }
////                //System.out.println(JSON.toJSON(tbagent));
////                //tbagentMapper.insert(tbagent);
////                tbagents.add(tbagent);
////                //logger.info("插入数据完成");
////            }
//
//        }
////        excelMapper.batchInsert(tbagents);  //  批量插入 五秒完成
//        long endTime = System.currentTimeMillis();
//        long totaltime = endTime - startTime;
//        logger.info("【消耗时间为】{}",totaltime);  //  将近两万条数据 3秒解析完成
//        logger.info("【第一条数据为】{}", JSON.toJSON(users.get(0)));
//        return rows;
//    }
//
//    public String getCellValue(Cell cell) {
//        String value = "";
//        if (cell != null) {
//            switch(cell.getCellType()){
//                case HSSFCell.CELL_TYPE_NUMERIC:// 数字
//                    value = cell.getNumericCellValue()+ " ";
//                    if(HSSFDateUtil.isCellDateFormatted(cell)){
//                        Date date = cell.getDateCellValue();
//                        if(date != null){
//                            value = new SimpleDateFormat("yyyy-MM-dd").format(date); //  日期格式化
//                        }else{
//                            value = "";
//                        }
//                    }else {
//                        //  解析cell时候 数字类型默认是double类型的 但是想要获取整数类型 需要格式化
//                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
//                    }
//                    break;
//                case HSSFCell.CELL_TYPE_STRING: //  字符串
//                    value = cell.getStringCellValue();
//                    break;
//                case HSSFCell.CELL_TYPE_BOOLEAN:   //  Boolean类型
//                    value = cell.getBooleanCellValue()+"";
//                    break;
//                case HSSFCell.CELL_TYPE_BLANK:   // 空值
//                    value = "";
//                    break;
//                case HSSFCell.CELL_TYPE_ERROR: // 错误类型
//                    value ="非法字符";
//                    break;
//                default:
//                    value = "未知类型";
//                    break;
//            }
//
//        }
//        return value.trim();
//    }
//}
