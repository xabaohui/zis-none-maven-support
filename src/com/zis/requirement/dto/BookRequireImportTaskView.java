package com.zis.requirement.dto;

import com.zis.requirement.bean.BookRequireImportTask;

public class BookRequireImportTaskView extends BookRequireImportTask {

	private String statusDisplay;

	public String getStatusDisplay() {
		return statusDisplay;
	}

	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
}
