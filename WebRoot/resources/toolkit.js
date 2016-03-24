function showAddResult(result) {
	if(result.lastRecordSuccess === true) {
		var selectedContent = '已扫描记录:<p/><ol>';
		var scannedBookList = result.scannedBookList;
		for(var i=0; i<scannedBookList.length; i++) {
			var book = scannedBookList[i];
			var bookIntro = book.isbn + ' / ' + book.bookName;
			selectedContent =  selectedContent + '<li>'+ bookIntro +'</li>'
		}
		selectedContent = selectedContent + '</ol>';
		selectedContent = selectedContent + '<p/>匹配的库位有：<p/><ul>';
		var comparablePos = result.comparablePos;
		for(var i=0; i<comparablePos.length; i++) {
			var pos = comparablePos[i];
			selectedContent = selectedContent + '<li>' + pos + '</li>';
		}
		selectedContent = selectedContent + '</ul>';
		$('lastScanResult').innerHTML = selectedContent;
	} else {
		alert('操作失败，' + result.failReason);
	}
}

function clearSession() {
	
	stockPosCheckBO.clearSession();
	$('lastScanResult').innerHTML = '';
}