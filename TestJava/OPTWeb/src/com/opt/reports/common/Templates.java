package com.opt.reports.common;
/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2013 Ricardo Mariaca
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Templates
{
   public static final StyleBuilder rootStyle;
   public static final StyleBuilder boldStyle;
   public static final StyleBuilder italicStyle;
   public static final StyleBuilder boldCenteredStyle;
   public static final StyleBuilder boldLeftStyle;
   public static final StyleBuilder bold12CenteredStyle;
   public static final StyleBuilder bold12LeftStyle;
   public static final StyleBuilder bold10LeftStyle;
   public static final StyleBuilder bold14CenteredStyle;
   public static final StyleBuilder bold18CenteredStyle;
   public static final StyleBuilder bold22CenteredStyle;
   public static final StyleBuilder columnStyle;
   public static final StyleBuilder columnStyle10;
   public static final StyleBuilder columnStyle10String;
   public static final StyleBuilder columnStyle8String;
   public static final StyleBuilder columnStyle10Number;
   public static final StyleBuilder columnStyle8Number;
   public static final StyleBuilder columnTitleStyle;
   public static final StyleBuilder groupStyle;
   public static final StyleBuilder subtotalStyle;

   public static final ReportTemplateBuilder reportTemplate;
   public static final CurrencyType currencyType;
   public static final ComponentBuilder<?, ?> dynamicReportsComponent;
   public static final ComponentBuilder<?, ?> footerComponent;

   static
   {
      rootStyle = stl.style().setPadding(2);
      boldStyle = stl.style(rootStyle).bold();
      italicStyle = stl.style(rootStyle).italic();
      boldCenteredStyle = stl.style(boldStyle)
              .setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
      boldLeftStyle = stl.style(boldStyle)
              .setAlignment(HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
      bold12CenteredStyle = stl.style(boldCenteredStyle)
              .setFontSize(12);
      bold10LeftStyle = stl.style(boldLeftStyle)
              .setFontSize(10);
      bold12LeftStyle = stl.style(boldLeftStyle)
              .setFontSize(12);
      bold14CenteredStyle = stl.style(boldCenteredStyle)
              .setFontSize(14);
      bold18CenteredStyle = stl.style(boldCenteredStyle)
              .setFontSize(18);
      bold22CenteredStyle = stl.style(boldCenteredStyle)
              .setFontSize(22);
      columnStyle = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE);
      columnStyle10 = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(10);
      columnStyle10String = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(10);
      columnStyle10Number = stl.style(rootStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(10);
      columnStyle8String = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(8);
      columnStyle8Number = stl.style(rootStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(8);
      columnTitleStyle = stl.style(columnStyle)
              .setBorder(stl.pen1Point())
              .setHorizontalAlignment(HorizontalAlignment.CENTER)
              .setBackgroundColor(Color.LIGHT_GRAY)
              .bold();
      groupStyle = stl.style(boldStyle)
              .setHorizontalAlignment(HorizontalAlignment.LEFT);
      subtotalStyle = stl.style(boldStyle)
              .setTopBorder(stl.pen1Point());

      StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
      StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
              .setBackgroundColor(new Color(170, 170, 170));
      StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
              .setBackgroundColor(new Color(140, 140, 140));
      StyleBuilder crosstabCellStyle = stl.style(columnStyle)
              .setBorder(stl.pen1Point());

      TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
              .setHeadingStyle(0, stl.style(rootStyle).bold());

      reportTemplate = template()
              .setLocale(Locale.ENGLISH)
              .setColumnStyle(columnStyle)
              .setColumnTitleStyle(columnTitleStyle)
              .setGroupStyle(groupStyle)
              .setGroupTitleStyle(groupStyle)
              .setSubtotalStyle(subtotalStyle)
              .highlightDetailEvenRows()
              .crosstabHighlightEvenRows()
              .setCrosstabGroupStyle(crosstabGroupStyle)
              .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
              .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
              .setCrosstabCellStyle(crosstabCellStyle)
              .setTableOfContentsCustomizer(tableOfContentsCustomizer);

      currencyType = new CurrencyType();

      HyperLinkBuilder link = hyperLink("http://www.dynamicreports.org");
      /*dynamicReportsComponent =
              cmp.horizontalList(
                      cmp.image(Templates.class.getResource("../images/dynamicreports.png")).setFixedDimension(60, 60),
                      cmp.verticalList(
                              cmp.text("Adhoc Report").setStyle(bold22CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
                              cmp.text("http://www.dynamicreports.org").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);
      */
      dynamicReportsComponent =
              cmp.horizontalList(cmp.verticalList(cmp.text("").setStyle(bold18CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT))).setFixedWidth(300);
      footerComponent = cmp.pageXofY()
              .setStyle(
                      stl.style(boldCenteredStyle)
                              .setTopBorder(stl.pen1Point()));
   }

   /**
    * Creates custom component which is possible to add to any report band component
    */
   public static ComponentBuilder<?, ?> createTitleComponent(String label)
   {
      return cmp.horizontalList()
              .add(
                      dynamicReportsComponent,
                      cmp.text(label).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
              .newRow()
              .add(cmp.line())
              .newRow()
              .add(cmp.verticalGap(10));
   }

   public static CurrencyValueFormatter createCurrencyValueFormatter(String label)
   {
      return new CurrencyValueFormatter(label);
   }

   public static class CurrencyType extends BigDecimalType
   {
      private static final long serialVersionUID = 1L;

      @Override
      public String getPattern()
      {
         return "$ #,###.00";
      }
   }

   private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number>
   {
      private static final long serialVersionUID = 1L;

      private String label;

      public CurrencyValueFormatter(String label)
      {
         this.label = label;
      }

      @Override
      public String format(Number value, ReportParameters reportParameters)
      {
         return label + currencyType.valueToString(value, reportParameters.getLocale());
      }
   }
}