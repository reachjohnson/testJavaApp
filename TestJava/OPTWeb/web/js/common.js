var space_comm = " \t\n\r"
function isEmpty(field) {
    var i;
    for (i = 0; i < field.length; i++) {
        var c = field.charAt(i);
        if (space_comm.indexOf(c) == -1)
            return false;
    }
    return true;
}

function compareTwoDates(FromDate, ToDate, DateFormat, condition) {
    var FromDateDay = "";
    var FromDateMonth = "";
    var FromDateYear = "";

    var ToDateDay = "";
    var ToDateMonth = "";
    var ToDateYear = "";

    DateFormat = DateFormat.toUpperCase();

    condition = condition.toUpperCase();

    if (FromDate != "" && ToDate != "") {
        var datePat;

        if (DateFormat == "YYYY/MM/DD")
            datePat = /^(\d{4})(\/|-)(\d{1,2})\2(\d{1,2})$/;
        else
            datePat = /^(\d{1,2})(\/)(\d{1,2})\2(\d{4})$/;

        var FromDateMatchArray = FromDate.match(datePat);
        var ToDateMatchArray = ToDate.match(datePat);

        if (FromDateMatchArray == null) {
            OPTDialog("Invalid Date Format");
            return false;
        }

        if (ToDateMatchArray == null) {
            OPTDialog("Invalid Date Format");
            return false;
        }

        if (DateFormat == "YYYY/MM/DD") {
            FromDateDay = FromDateMatchArray[4];
            FromDateMonth = FromDateMatchArray[3];
            FromDateYear = FromDateMatchArray[1];

            ToDateDay = ToDateMatchArray[4];
            ToDateMonth = ToDateMatchArray[3];
            ToDateYear = ToDateMatchArray[1];
        }
        else if (DateFormat == "YYYY/DD/MM") {
            FromDateDay = FromDateMatchArray[4];
            FromDateMonth = FromDateMatchArray[1];
            FromDateYear = FromDateMatchArray[3];

            ToDateDay = ToDateMatchArray[4];
            ToDateMonth = ToDateMatchArray[1];
            ToDateYear = ToDateMatchArray[3];
        }
        else if (DateFormat == "MM/DD/YYYY") {
            FromDateDay = FromDateMatchArray[3];
            FromDateMonth = FromDateMatchArray[1];
            FromDateYear = FromDateMatchArray[4];

            ToDateDay = ToDateMatchArray[3];
            ToDateMonth = ToDateMatchArray[1];
            ToDateYear = ToDateMatchArray[4];
        }
        else if (DateFormat == "DD/MM/YYYY") {
            FromDateDay = FromDateMatchArray[1];
            FromDateMonth = FromDateMatchArray[3];
            FromDateYear = FromDateMatchArray[4];

            ToDateDay = ToDateMatchArray[1];
            ToDateMonth = ToDateMatchArray[3];
            ToDateYear = ToDateMatchArray[4];
        }
        else if (DateFormat == "MM-DD-YYYY") {
            FromDateDay = FromDateMatchArray[3];
            FromDateMonth = FromDateMatchArray[1];
            FromDateYear = FromDateMatchArray[4];

            ToDateDay = ToDateMatchArray[3];
            ToDateMonth = ToDateMatchArray[1];
            ToDateYear = ToDateMatchArray[4];
        }
        else //if(dateFormat == "DD-MM-YYYY")
        {
            FromDateDay = FromDateMatchArray[1];
            FromDateMonth = FromDateMatchArray[3];
            FromDateYear = FromDateMatchArray[4];

            ToDateDay = ToDateMatchArray[1];
            ToDateMonth = ToDateMatchArray[3];
            ToDateYear = ToDateMatchArray[4];
        }
    }

    FromDate = FromDateMonth + "/" + FromDateDay + "/" + FromDateYear;
    ToDate = ToDateMonth + "/" + ToDateDay + "/" + ToDateYear;

    FromDate = Date.parse(FromDate);
    ToDate = Date.parse(ToDate);

    if (condition == "GREATER" && FromDate > ToDate) {
        return false;
    }
    else if (condition == "GREATEROREQUAL" && FromDate >= ToDate) {
        return false;
    }
    else if (condition == "LESSER" && FromDate < ToDate) {
        return false;
    }
    else if (condition == "LESSEROREQUAL" && FromDate <= ToDate) {
        return false;
    }

    return true;
}

function TextAreaMaxSize(objTextArea, TextAreaSize, ErrorMsg) {
    var objTextAreaValue = trim(objTextArea.value);
    TextAreaSize = parseFloat(TextAreaSize);
    if (objTextAreaValue.length > TextAreaSize) {
        OPTDialog(ErrorMsg);
        objTextArea.focus();
        objTextArea.select();
    }
}

function isNumber(data) {

    data = trim(data);
    var valid = "0123456789";
    var ok = "yes";
    var temp;
    if (data != "") {
        for (var i = 0; i < data.length; i++) {
            temp = "" + data.substring(i, i + 1);
            if (valid.indexOf(temp) == "-1")
                ok = "no";
        }
        if (ok == "no") {
            return false;
        }
    }
    return true;
}

function trim(strText) {
    while (strText.substring(0, 1) == ' ')
        strText = strText.substring(1, strText.length);
    while (strText.substring(strText.length - 1, strText.length) == ' ')
        strText = strText.substring(0, strText.length - 1);

    return strText;
}

function checkValidDateForValue(obj) {
    var dateStr = obj;
    dateFormat = "DD/MM/YYYY";

    if (!isEmpty(dateStr)) {
        if (dateStr.length == 8) {
            if (isNumeric(dateStr)) {
                var dayValue = dateStr.substring(0, 2);
                var monthValue = dateStr.substring(2, 4);
                var yearValue = dateStr.substring(4, 8);
                dateStr = dayValue + "/" + monthValue + "/" + yearValue;
            }
        }
        var datePat = /^(\d{1,2})(\/)(\d{1,2})\2(\d{4})$/;

        var matchArray = dateStr.match(datePat);

        if (matchArray == null) {
            OPTDialog("Please enter valid date format (DD/MM/YYYY)", obj);
            return;
        }

        var day = matchArray[1];
        var month = matchArray[3];
        var year = matchArray[4];

        if (month < 1 || month > 12) {
            OPTDialog("Invalid Date. Valid months are 1 to 12", obj);
            return;
        }
        if (day < 1 || day > 31) {
            OPTDialog("Invalid Date. Valid days are 1 to 31", obj);
            return;
        }
        if (year < 1900 || year > 2099) {
            OPTDialog("Invalid Date. Valid years are between 1900 and 2099 only");
            return;
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
            OPTDialog("Invalid Date. Valid days are 1 to 30", obj);
            return;
        }
        if (month == 2) {
            var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
            if (isleap && day > 29) {
                OPTDialog("Invalid Date. The year entered is leap year. So, valid days are 1 to 29", obj);
                return;
            }
            else if (day == 29 && !isleap) {
                OPTDialog("Invalid Date. The year entered is not a leap year. So, valid days are 1 to 28", obj);
                return;
            }
            else if (day > 29 && !isleap) {
                OPTDialog("Invalid Date. Valid days are 1 to 28", obj);
                return;
            }

        }
        return true;
    }
}

function getDefaultDateFormat(dateValue, DateFormat) {
    var day = "";
    var month = "";
    var year = "";

    DateFormat = DateFormat.toUpperCase();

    if (dateValue != "") {
        var datePat;

        if (DateFormat == "YYYY/MM/DD")
            datePat = /^(\d{4})(\/|-)(\d{1,2})\2(\d{1,2})$/;
        else
            datePat = /^(\d{1,2})(\/)(\d{1,2})\2(\d{4})$/;

        var matchArray = dateValue.match(datePat);
        // is the format ok?

        if (matchArray == null) {
            OPTDialog("Invalid Date Format");
            return false;
        }

        if (DateFormat == "YYYY/MM/DD") {
            day = matchArray[4];
            // parse date into variables
            month = matchArray[3];
            year = matchArray[1];
        }
        else if (DateFormat == "YYYY/DD/MM") {
            day = matchArray[4];
            // parse date into variables
            month = matchArray[1];
            year = matchArray[3];
        }
        else if (DateFormat == "MM/DD/YYYY") {
            day = matchArray[3];
            // parse date into variables
            month = matchArray[1];
            year = matchArray[4];
        }
        else if (DateFormat == "DD/MM/YYYY") {
            day = matchArray[1];
            // parse date into variables
            month = matchArray[3];
            year = matchArray[4];
        }
        else if (DateFormat == "MM-DD-YYYY") {
            day = matchArray[3];
            // parse date into variables
            month = matchArray[1];
            year = matchArray[4];
        }
        else //if(dateFormat == "DD-MM-YYYY")
        {
            day = matchArray[1];
            // parse date into variables
            month = matchArray[3];
            year = matchArray[4];
        }
    }

    var EnteredDate = month + "/" + day + "/" + year;
    return EnteredDate;
}

function checkValidDateForObject(obj) {
    var dateStr = obj.value;
    dateFormat = "DD/MM/YYYY";

    if (!isEmpty(dateStr)) {
        if (dateStr.length == 8) {
            if (isNumeric(dateStr)) {
                var dayValue = dateStr.substring(0, 2);
                var monthValue = dateStr.substring(2, 4);
                var yearValue = dateStr.substring(4, 8);
                dateStr = dayValue + "/" + monthValue + "/" + yearValue;
            }
        }
        var datePat = /^(\d{1,2})(\/)(\d{1,2})\2(\d{4})$/;

        var matchArray = dateStr.match(datePat);

        if (matchArray == null) {
            OPTDialog("Please enter valid date format (DD/MM/YYYY)", obj);
            return;
        }

        var day = matchArray[1];
        var month = matchArray[3];
        var year = matchArray[4];

        if (month < 1 || month > 12) {
            OPTDialog("Invalid Date. Valid months are 1 to 12", obj);
            return;
        }
        if (day < 1 || day > 31) {
            OPTDialog("Invalid Date. Valid days are 1 to 31", obj);
            return;
        }
        if (year < 1900 || year > 2099) {
            OPTDialog("Invalid Date. Valid years are between 1900 and 2099 only", obj);
            return;
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
            OPTDialog("Invalid Date. Valid days are 1 to 30", obj);
            return;
        }
        if (month == 2) {
            var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
            if (isleap && day > 29) {
                OPTDialog("Invalid Date. The year entered is leap year. So, valid days are 1 to 29", obj);
                return;
            }
            else if (day == 29 && !isleap) {
                OPTDialog("Invalid Date. The year entered is not a leap year. So, valid days are 1 to 28", obj);
                return;
            }
            else if (day > 29 && !isleap) {
                OPTDialog("Invalid Date. Valid days are 1 to 28", obj);
                return;
            }

        }
        return true;
    }
}


function isNumeric(data) {
    var valid = "0123456789";
    var ok = "yes";
    var temp;
    if (!isEmpty(data)) {
        for (var i = 0; i < data.length; i++) {
            temp = "" + data.substring(i, i + 1);
            if (valid.indexOf(temp) == "-1")
                ok = "no";
        }
        if (ok == "no") {
            return false;
        }
    }
    return true;
}

//disable right mouse click Script
document.onmousedown = "if (event.button==2) return false";
document.oncontextmenu = new Function("return false");

document.onkeydown = showDown;

function showDown(evt) {
    evt = (evt) ? evt : ((event) ? event : null);
    if (evt) {
        if (event.keyCode == 8 && (event.srcElement.type != "text" && event.srcElement.type != "textarea" && event.srcElement.type != "password")) {
// When backspace is pressed but not in form element
            cancelKey(evt);
        }
        else if (event.keyCode == 116) {
// When F5 is pressed
            cancelKey(evt);
        }
        else if (event.keyCode == 122) {
// When F11 is pressed
            cancelKey(evt);
        }
        else if (event.ctrlKey && (event.keyCode == 78 || event.keyCode == 82)) {
// When ctrl is pressed with R or N
            cancelKey(evt);
        }
        else if (event.altKey && event.keyCode == 37) {
// stop Alt left cursor
            return false;
        }
    }
}

function cancelKey(evt) {
    if (evt.preventDefault) {
        evt.preventDefault();
        return false;
    }
    else {
        evt.keyCode = 0;
        evt.returnValue = false;
    }
}

function setFocus(obj) {
    obj.focus();
}

document.oncontextmenu = RightMouseDown;
document.onmousedown = mouseDown;

function mouseDown(e) {
    if (e.which == 3) {//righClick
    }
}
function RightMouseDown() {
    return false;
}

function menuSubmitForm(actionName) {
    document.OPTForm.action = actionName;
    frmReadSubmit();
}

function disableButtonsAndSubmit()
{
    var obj1 = document.getElementById("buttonId");
    if (obj1 != null) {
        obj1.removeAttribute('href');
        obj1.style.opacity = 0.2;
        obj1.style.textDecoration = 'none';
        obj1.style.cursor = 'default';
        obj1.disabled = 'disabled';
    }

    var obj2 = document.getElementById("buttonIdReopenSearch");
    if (obj2 != null) {
        obj2.removeAttribute('href');
        obj2.style.opacity = 0.2;
        obj2.style.textDecoration = 'none';
        obj2.style.cursor = 'default';
        obj2.disabled = 'disabled';
    }

    var obj3;
    for (var iCount = 1; iCount < 8; iCount++) {
        obj3 = document.getElementById("SendButton" + iCount);
        if (obj3 != null) {
            obj3.removeAttribute('href');
            obj3.style.opacity = 0.2;
            obj3.style.textDecoration = 'none';
            obj3.style.cursor = 'default';
            obj3.disabled = 'disabled';
        }
    }

    var obj4 = document.getElementById("buttonIdHome");
    if (obj4 != null) {
        obj4.removeAttribute('href');
        obj4.style.opacity = 0.2;
        obj4.style.textDecoration = 'none';
        obj4.style.cursor = 'default';
        obj4.disabled = 'disabled';
    }

    var obj5 = document.getElementById("buttonIdLogOut");
    if (obj5 != null) {
        obj5.removeAttribute('href');
        obj5.style.opacity = 0.2;
        obj5.style.textDecoration = 'none';
        obj5.style.cursor = 'default';
        obj5.disabled = 'disabled';
    }

    var obj6;
    for (var iCount = 0; iCount < 5; iCount++) {
        obj6 = document.getElementById("buttonId" + iCount);
        if (obj6 != null) {
            obj6.removeAttribute('href');
            obj6.style.opacity = 0.2;
            obj6.style.textDecoration = 'none';
            obj6.style.cursor = 'default';
            obj6.disabled = 'disabled';
        }
    }


    document.OPTForm.formsubmit.value = "submitted";
    var target = document.getElementById('spinnerContainer');
    var spinner = new Spinner(opts).spin(target);
    document.OPTForm.submit();
}

function frmReadSubmit() {

    disableButtonsAndSubmit();
}

function frmWriteSubmit() {
    if (writeAccess) {
        disableButtonsAndSubmit();
    }
    else {
        OPTDialog("You Do Not Have Write Access");
        return;
    }
}

function frmLoginSubmit() {
    disableButtonsAndSubmit();
}


function noAccessMessage() {
    OPTDialog("You Do Not Have Access");
    return;
}

function openJIRAWindow(JIRAURL, TicketId) {
    window.open(JIRAURL, TicketId);
}

function showFullPageMask(flag) {
    var pageMask = document.getElementById('pageMask');
    if (flag) {
        var n, r, i, s, o, u, a = document.documentElement ? document.documentElement.clientWidth : window.innerWidth;
        window.innerHeight && window.scrollMaxY ? (i = a + window.scrollMaxX, s = window.innerHeight + window.scrollMaxY) : document.body.scrollHeight > document.body.offsetHeight ? (i = document.body.scrollWidth, s = document.body.scrollHeight) : (i = document.body.offsetWidth, s = document.body.offsetHeight), window.innerHeight ? (n = a, r = window.innerHeight) : document.documentElement && document.documentElement.clientHeight ? (n = document.documentElement.clientWidth, r = document.documentElement.clientHeight) : document.body && (n = document.body.clientWidth, r = document.body.clientHeight), o = n > i ? n : i, u = r > s ? r : s;
        pageMask.style.height = u + 'px';
        pageMask.style.width = o + 'px';
        document.body.className += " fullPageMask";
        pageMask.className += " fullMask";
        document.getElementById("fullPageMask").className = "";
    } else {
        document.body.className = document.body.className.replace(/\bfullPageMask\b/, '');
        pageMask.className = pageMask.className.replace(/\bfullMask\b/, '');
        document.getElementById("fullPageMask").className = " accessAid";
    }
}

function OPTDialog(Message, Obj) {
    showFullPageMask(true);
    jAlert(Message, JavaScriptAlert, function (r) {
        showFullPageMask(false);
        if (Obj != null) {
            Obj.focus();
        }
    });
}

function frmCancel() {
    showFullPageMask(true);
    jConfirm("Are You Sure to Reset Data", JavaScriptConf, function (retval) {
        showFullPageMask(false);
        if (retval) {
            document.OPTForm.reset();
        }
    });
}

function frmClose() {
    parent.opener.showFullPageMask(false);
    window.close();
}
