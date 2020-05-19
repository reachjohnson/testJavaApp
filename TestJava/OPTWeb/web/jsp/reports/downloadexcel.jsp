<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.io.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.opt.util.AppConstants" %>
<%@ page import="com.opt.util.AppUtil" %>
<%

    Logger objLogger = Logger.getLogger("shoeExcelPopup");
    objLogger.info(AppConstants.PROCESS_STARTED);
    int DEFAULT_BUFFER_SIZE = 8192;
    String strFilename = AppUtil.checkNull((String) request.getAttribute("EXCELFILEPATH"));
    File file = new File(strFilename);
    response.reset();
    response.setBufferSize(DEFAULT_BUFFER_SIZE);
    response.setHeader("Content-Type", "application/vnd.ms-excel");
    response.setHeader("Content-Length", String.valueOf(file.length()));
    response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

    BufferedInputStream input = null;
    BufferedOutputStream output = null;
    try
    {
        input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
        output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int length; (length = input.read(buffer)) > -1; )
        {
            output.write(buffer, 0, length);
        }
    }
    catch (Exception objException)
    {
        objLogger.error(objException, objException.fillInStackTrace());
    }
    finally
    {
        if (output != null)
            output.close();
        if (input != null)
            input.close();
    }
    objLogger.info(AppConstants.PROCESS_FINISHED);
%>