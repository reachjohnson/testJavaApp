package com.opt.reports.ExcelReports;

import com.opt.util.AppConstants;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class TicketsCurrentStatusExcel
{
   Logger objLogger = Logger.getLogger(TicketsCurrentStatusExcel.class.getName());
   HSSFWorkbook hwb = null;

   public void exportDataToExcel(String strFilePath, ArrayList<String> arlNotStartedTicketsCurrentStatus,
                                 ArrayList<String> arlInProgressTicketsCurrentStatus, ArrayList<String> arlOnHoldTicketsCurrentStatus) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try (FileOutputStream fileOut = new FileOutputStream(strFilePath))
      {
         hwb = new HSSFWorkbook();
         HSSFFont headerFont = hwb.createFont();
         headerFont.setColor(HSSFColor.BLACK.index);
         headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

         HSSFCellStyle cellHeaderStyle = hwb.createCellStyle();
         cellHeaderStyle.setWrapText(true);
         cellHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         cellHeaderStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
         cellHeaderStyle.setFont(headerFont);


         if (arlNotStartedTicketsCurrentStatus != null && !arlNotStartedTicketsCurrentStatus.isEmpty())
         {
            HSSFSheet sheet = hwb.createSheet("Not Started");
            CreateNotStartedTicketsCurrentStatus(arlNotStartedTicketsCurrentStatus, sheet, cellHeaderStyle);
         }
         if (arlInProgressTicketsCurrentStatus != null && !arlInProgressTicketsCurrentStatus.isEmpty())
         {
            HSSFSheet sheet = hwb.createSheet("In Progress");
            CreateInProgressTicketsCurrentStatus(arlInProgressTicketsCurrentStatus, sheet, cellHeaderStyle);
         }
         if (arlOnHoldTicketsCurrentStatus != null && !arlOnHoldTicketsCurrentStatus.isEmpty())
         {
            HSSFSheet sheet = hwb.createSheet("On Hold");
            CreateInProgressTicketsCurrentStatus(arlOnHoldTicketsCurrentStatus, sheet, cellHeaderStyle);
         }

         hwb.write(fileOut);
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw objException;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   private void CreateNotStartedTicketsCurrentStatus(ArrayList<String> arlNotStartedTicketsCurrentStatus, HSSFSheet sheet, HSSFCellStyle cellHeaderStyle) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         HSSFRow rowhead = sheet.createRow(0);

         HSSFCell Cell0 = rowhead.createCell(0);
         Cell0.setCellValue("Ticket ID");
         Cell0.setCellStyle(cellHeaderStyle);

         HSSFCell Cell1 = rowhead.createCell(1);
         Cell1.setCellValue("Ticket Description");
         Cell1.setCellStyle(cellHeaderStyle);

         HSSFCell Cell2 = rowhead.createCell(2);
         Cell2.setCellValue("Category");
         Cell2.setCellStyle(cellHeaderStyle);

         HSSFCell Cell3 = rowhead.createCell(3);
         Cell3.setCellValue("Assignee");
         Cell3.setCellStyle(cellHeaderStyle);

         HSSFCell Cell4 = rowhead.createCell(4);
         Cell4.setCellValue("Comments By");
         Cell4.setCellStyle(cellHeaderStyle);

         HSSFCell Cell5 = rowhead.createCell(5);
         Cell5.setCellValue("Comments Date");
         Cell5.setCellStyle(cellHeaderStyle);

         HSSFCell Cell6 = rowhead.createCell(6);
         Cell6.setCellValue("Comments");
         Cell6.setCellStyle(cellHeaderStyle);

         int intRowCounter = 1;

         for (int intCount = 0; intCount < arlNotStartedTicketsCurrentStatus.size(); intCount += 7)
         {
            HSSFRow row = sheet.createRow(intRowCounter);
            row.createCell(0).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount));
            row.createCell(1).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount + 2));
            row.createCell(2).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount + 3));
            row.createCell(3).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount + 1));
            row.createCell(4).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount + 4));
            row.createCell(5).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount + 5));
            row.createCell(6).setCellValue(arlNotStartedTicketsCurrentStatus.get(intCount + 6));
            intRowCounter++;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw objException;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   private void CreateInProgressTicketsCurrentStatus(ArrayList<String> arlInProgressTicketsCurrentStatus, HSSFSheet sheet, HSSFCellStyle cellHeaderStyle) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         HSSFRow rowhead = sheet.createRow(0);

         HSSFCell Cell0 = rowhead.createCell(0);
         Cell0.setCellValue("Ticket ID");
         Cell0.setCellStyle(cellHeaderStyle);

         HSSFCell Cell1 = rowhead.createCell(1);
         Cell1.setCellValue("Ticket Description");
         Cell1.setCellStyle(cellHeaderStyle);

         HSSFCell Cell2 = rowhead.createCell(2);
         Cell2.setCellValue("Category");
         Cell2.setCellStyle(cellHeaderStyle);

         HSSFCell Cell3 = rowhead.createCell(3);
         Cell3.setCellValue("Assignee");
         Cell3.setCellStyle(cellHeaderStyle);

         HSSFCell Cell4 = rowhead.createCell(4);
         Cell4.setCellValue("Comments By");
         Cell4.setCellStyle(cellHeaderStyle);

         HSSFCell Cell5 = rowhead.createCell(5);
         Cell5.setCellValue("Comments Date");
         Cell5.setCellStyle(cellHeaderStyle);

         HSSFCell Cell6 = rowhead.createCell(6);
         Cell6.setCellValue("Comments");
         Cell6.setCellStyle(cellHeaderStyle);

         int intRowCounter = 1;

         for (int intCount = 0; intCount < arlInProgressTicketsCurrentStatus.size(); intCount += 7)
         {
            HSSFRow row = sheet.createRow(intRowCounter);
            row.createCell(0).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount));
            row.createCell(1).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount + 2));
            row.createCell(2).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount + 3));
            row.createCell(3).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount + 1));
            row.createCell(4).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount + 4));
            row.createCell(5).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount + 5));
            row.createCell(6).setCellValue(arlInProgressTicketsCurrentStatus.get(intCount + 6));
            intRowCounter++;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw objException;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

   private void CreateOnHoldTicketsCurrentStatus(ArrayList<String> arlOnHoldTicketsCurrentStatus, HSSFSheet sheet, HSSFCellStyle cellHeaderStyle) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      try
      {
         HSSFRow rowhead = sheet.createRow(0);

         HSSFCell Cell0 = rowhead.createCell(0);
         Cell0.setCellValue("Ticket ID");
         Cell0.setCellStyle(cellHeaderStyle);

         HSSFCell Cell1 = rowhead.createCell(1);
         Cell1.setCellValue("Ticket Description");
         Cell1.setCellStyle(cellHeaderStyle);

         HSSFCell Cell2 = rowhead.createCell(2);
         Cell2.setCellValue("Category");
         Cell2.setCellStyle(cellHeaderStyle);

         HSSFCell Cell3 = rowhead.createCell(3);
         Cell3.setCellValue("Assignee");
         Cell3.setCellStyle(cellHeaderStyle);

         HSSFCell Cell4 = rowhead.createCell(4);
         Cell4.setCellValue("Comments By");
         Cell4.setCellStyle(cellHeaderStyle);

         HSSFCell Cell5 = rowhead.createCell(5);
         Cell5.setCellValue("Comments Date");
         Cell5.setCellStyle(cellHeaderStyle);

         HSSFCell Cell6 = rowhead.createCell(6);
         Cell6.setCellValue("Comments");
         Cell6.setCellStyle(cellHeaderStyle);

         int intRowCounter = 1;

         for (int intCount = 0; intCount < arlOnHoldTicketsCurrentStatus.size(); intCount += 7)
         {
            HSSFRow row = sheet.createRow(intRowCounter);
            row.createCell(0).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount));
            row.createCell(1).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount + 2));
            row.createCell(2).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount + 3));
            row.createCell(3).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount + 1));
            row.createCell(4).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount + 4));
            row.createCell(5).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount + 5));
            row.createCell(6).setCellValue(arlOnHoldTicketsCurrentStatus.get(intCount + 6));
            intRowCounter++;
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw objException;
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
   }

}
