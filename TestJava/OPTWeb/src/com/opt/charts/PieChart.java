package com.opt.charts;

import com.opt.exception.TaskException;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PieChart
{
   Logger objLogger = Logger.getLogger(PieChart.class.getName());
   int intWidth = 600;
   int intHeight = 400;

   public String drawPieChart(String chartName, String userId, String imageRealPath, String chartTitle, String categoryAxisLabel, String valueAxisLabel, ArrayList<Object> arlData) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      String strChartImagePath;
      try
      {
         strChartImagePath = imageRealPath + "\\" + AppConstants.CHART_IMAGE_FOLDER + "\\" + userId + "_" + chartName + ".png";
         String strChartImageDirectory = imageRealPath + "\\" + AppConstants.CHART_IMAGE_FOLDER;
         File chartImageDirFile = new File(strChartImageDirectory);
         if (!chartImageDirFile.exists() || !chartImageDirFile.isDirectory())
         {
            chartImageDirFile.mkdir();
         }
         File objChartImageFile = new File(strChartImagePath);
         if (objChartImageFile.exists())
         {
            objChartImageFile.delete();
         }

         JFreeChart chart = ChartFactory.createPieChart(chartTitle, createDataSet(arlData), false, true, false);

         PiePlot plot = (PiePlot) chart.getPlot();
         plot.setSectionOutlinesVisible(false);
         plot.setCircular(false);
         plot.setLabelGap(0.02);
         Font font0 = new Font("Verdana", Font.BOLD, 11);
         plot.setLabelFont(font0);
         Font font2 = new Font("Verdana", Font.BOLD, 16);
         chart.getTitle().setFont(font2);

         plot.setOutlinePaint(Color.BLACK);
         plot.setOutlineStroke(new BasicStroke(2.0f));

         PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                 "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
         plot.setLabelGenerator(gen);
         ChartUtilities.saveChartAsPNG(objChartImageFile, chart, intWidth, intHeight);
         strChartImagePath = AppConstants.CHART_IMAGE_FOLDER + "/" + userId + "_" + chartName + ".png";
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return strChartImagePath;
   }

   private DefaultPieDataset createDataSet(ArrayList<Object> arlData) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      DefaultPieDataset dataset = new DefaultPieDataset ();
      try
      {
         for (int intCount1 = 0; intCount1 < arlData.size(); intCount1 += 2)
         {
            if((Double) arlData.get(intCount1 + 1) > 0)
            {
               dataset.setValue((String) arlData.get(intCount1), (Double) arlData.get(intCount1 + 1));
            }
         }
      }
      catch (Exception objException)
      {
         objLogger.error(objException, objException.fillInStackTrace());
         throw new TaskException(objException.getMessage(), objException);
      }
      objLogger.info(AppConstants.PROCESS_FINISHED);
      return dataset;
   }

}
