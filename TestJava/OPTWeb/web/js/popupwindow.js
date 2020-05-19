/*  Codes for Modal Window */

var ModalWin = null;

function FocusModalWin() {
    if (!checkModalOpened()) {

    }
    else {
        if (ModalWin && !ModalWin.closed) {
            window.ModalWin.focus();
        }
    }
}

function checkModalOpened() {
    if (ModalWin && !ModalWin.closed) {
        return true;
    }
    else {
        return false;
    }
}

function CloseModalWin() {
    if (!checkModalOpened()) {

    }
    else {
        if (ModalWin && !ModalWin.closed) {
            window.ModalWin.close();
        }
    }
}

function openModalWin(url, iHeight, iWidth, sNewWindow, sModalWinName) {
    var winOpened;
    if (document.all)
        var xMax = screen.width, yMax = screen.height;
    else if (document.layers)
        var xMax = window.outerWidth, yMax = window.outerHeight;
    else
        var xMax = 640, yMax = 480;

    var xOffset = (xMax - iWidth) / 2, yOffset = (yMax - iHeight) / 2;

    if (sNewWindow == "new") {
        if (!ModalWin || ModalWin.closed) {
            ModalWin = window.open(url, sModalWinName, "screenX=" + xOffset + ",screenY=" + yOffset + ",top=" + yOffset + ",left=" + xOffset + ",height=" + iHeight + ",width=" + iWidth + ",resizable=no,status=no,toolbar=no,menubar=no,location=no, scrollbars=1");
            winOpened = true;
            var pageMask = document.getElementById('pageMask');
            if (pageMask) {
                pageMask.addEventListener('click', function (event) {
                    ModalWin.focus();
                })
            }
            ;
            intVal = setInterval(function () {
                if (ModalWin && ModalWin.closed) {
                    clearInterval(intVal);
                    winOpened = false;
                    destroyPopupWindow();
                }
            }, 200);

        }
        else {
            ModalWin.focus();
        }
    }
    else {
        location.href = url;
    }
}

function openModalWin1(url, iHeight, iWidth, xOffset, yOffset, sNewWindow, sModalWinName) {
    var returnObj;
    if (sNewWindow == "new") {
        if (!ModalWin || ModalWin.closed) {
            returnObj = ModalWin = window.open(url, sModalWinName, "screenX=" + xOffset + ",screenY=" + yOffset + ",top=" + yOffset + ",left=" + xOffset + ",height=" + iHeight + ",width=" + iWidth + ",resizable=no,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
        }
        else {
            ModalWin.focus();
        }
    }
    else {
        location.href = url;
    }
    return returnObj;
}

function MM_openBrWindow(theURL, height, width) {
    openModalWin(theURL, height, width, 'new', 'ModalWindow');
}
function destroyPopupWindow() {
    showFullPageMask(false);
}
