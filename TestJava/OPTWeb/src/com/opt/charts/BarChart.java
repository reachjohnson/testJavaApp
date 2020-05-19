package com.opt.charts;

import com.opt.exception.TaskException;
import com.opt.util.AppConstants;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BarChart
{
   Logger objLogger = Logger.getLogger(BarChart.class.getName());
   int intWidth = 600;
   int intHeight = 400;

   public String drawBarChart(String chartName, String userId, String imageRealPath, String chartTitle, String categoryAxisLabel, String valueAxisLabel, ArrayList<Object> arlData) throws Exception
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

         JFreeChart chart = ChartFactory.createBarChart(chartTitle,
                 categoryAxisLabel, valueAxisLabel, createDataSet(arlData), PlotOrientation.VERTICAL, false, true, false);
         chart.setBackgroundPaint(Color.white);

         // get a reference to the plot for further customisation...
         final CategoryPlot plot = chart.getCategoryPlot();
         plot.setBackgroundPaint(Color.lightGray);
         plot.setDomainGridlinePaint(Color.white);
         plot.setRangeGridlinePaint(Color.white);

         // set the range axis to display integers only...
         final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
         rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

         // disable bar outlines...
         final BarRenderer renderer = (BarRenderer) plot.getRenderer();
         renderer.setDrawBarOutline(false);
         plot.setOutlinePaint(Color.BLACK);
         plot.setOutlineStroke(new BasicStroke(2.0f));
         plot.setRangeGridlinesVisible(true);
         plot.setRangeGridlinePaint(Color.BLACK);
         plot.setDomainGridlinesVisible(true);
         plot.setDomainGridlinePaint(Color.BLACK);

         Color lineColor1 = new Color(255, 0, 0);
         Color lineColor2 = new Color(0, 128, 0);
         Color lineColor3 = new Color(0, 255, 255);
         Color lineColor4 = new Color(0, 0, 255);
         Color lineColor5 = new Color(165, 42, 42);
         Color lineColor6 = new Color(128, 0, 128);
         renderer.setSeriesPaint(0, lineColor1);
         renderer.setSeriesPaint(1, lineColor2);
         renderer.setSeriesPaint(2, lineColor3);
         renderer.setSeriesPaint(3, lineColor4);
         renderer.setSeriesPaint(4, lineColor5);
         renderer.setSeriesPaint(5, lineColor6);


         Font font0 = new Font("Verdana", Font.BOLD, 11);
         DecimalFormat decimalformat1 = new DecimalFormat("####");
         renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
         renderer.setBaseItemLabelsVisible(true);
         renderer.setBaseItemLabelFont(font0);
         plot.setRenderer(renderer);
         Font font1 = new Font("Verdana", Font.BOLD, 14);
         Font font2 = new Font("Verdana", Font.BOLD, 12);

         CategoryAxis domainAxis = plot.getDomainAxis();
         ValueAxis valueAxis = plot.getRangeAxis();

         domainAxis.setLabelFont(font1);
         domainAxis.setTickLabelFont(font2);
         valueAxis.setLabelFont(font1);
         valueAxis.setTickLabelFont(font2);
         LegendTitle legend = new LegendTitle(plot.getRenderer());
         Font font3 = new Font("Verdana", Font.BOLD, 13);
         legend.setItemFont(font3);
         legend.setPosition(RectangleEdge.BOTTOM);
         chart.addLegend(legend);
         Font font4 = new Font("Verdana", Font.BOLD, 16);
         chart.getTitle().setFont(font4);
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

   private CategoryDataset createDataSet(ArrayList<Object> arlData) throws Exception
   {
      objLogger.info(AppConstants.PROCESS_STARTED);
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      try
      {
         for (int intCount1 = 0; intCount1 < arlData.size(); intCount1 += 3)
         {
            dataset.addValue((Double) arlData.get(intCount1), (String) arlData.get(intCount1 + 1), (String) arlData.get(intCount1 + 2));
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
