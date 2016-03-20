package com.zis.bookinfo.util;

/**
 * 
 */


/**
 * ��·��ͼ������ҳ��Ϣץȡ����
 * 
 * @author lvbin 2015-5-21
 */
public class YouLuNetDetailCapture extends AbstractNetCapture {

	/**
	 * ��ȡͼ����Ϣ
	 * 
	 * @param id
	 *            ��·��ͼ��ID
	 * @return BookInfo�������û�У�����null
	 */
	public BookMetadata getBookInfo(String id) {
		String url = "http://www.youlu.net/" + id;
		String buf = getUrlContent(url, DEFAULT_CHARSET);
		String failMsg = "ͼ����Ϣ������";
		if (buf.contains(failMsg)) {
			return null;
		}
		// �����κ�����
		String youLuBookTitle = parseInfo(id, buf, "info1", "<h1>", "</h1>");
		String edition = "��һ��";
		if (youLuBookTitle.contains("��")) {
			Integer posStart = youLuBookTitle.indexOf("(");
			if (posStart == -1) {
				posStart = youLuBookTitle.indexOf("��");
			}
			Integer posEnd = youLuBookTitle.lastIndexOf("��");
			if (posStart > 0 && posEnd > 0) {
				edition = youLuBookTitle.substring(posStart + 1, posEnd + 1);
				youLuBookTitle = youLuBookTitle.replace(
						youLuBookTitle.substring(posStart + 1, posEnd + 1), "");// ������ȥ�����
			}
		}
		String bookName = clearBookName(youLuBookTitle);
		//
		String isbnCode = parseInfo(id, buf, "\"infoTit\">ISBN", "</span>", "[");
		String tagPrice = parseInfo(id, buf, "\"infoTit\">���ۣ�", "listPrice\">",
				"<");
		tagPrice = tagPrice.replace("&nbsp;", "").replace("��", "");
		String author = parseInfo(id, buf, "��/����", "_blank\">", "</a>");
		String publisher = parseInfo(id, buf, ">������", "\">", "</a>");
		String publishDate = parseInfo(id, buf, "�������ڣ�", "</span>", "&nbsp");
		// ����&Ŀ¼
		String contentUrlFmt = "http://www.youlu.net/info/bodyContent.aspx?bookId=%s&contentType=%s";
		String summary = getUrlContent(
				String.format(contentUrlFmt, id, "summary"), "gbk").replace(
				",", "��").replace("\"", "");
		String catelog = getUrlContent(
				String.format(contentUrlFmt, id, "catalog"), "gbk").replace(
				",", "��").replace("\"", "");
		// ���
		String stock = parseInfo(id, buf, "startRequestBookBuyLink", isbnCode + "', '", "',");
		String salePrice = parseInfo(id, buf, "������ͨ�û���", "��", "Ԫ");
		BookMetadata bi = new BookMetadata();
		bi.setAuthor(author);
		bi.setEdition(edition);
		bi.setIsbnCode(isbnCode);
		bi.setName(bookName);
		bi.setPrice(Double.valueOf(tagPrice));
		bi.setPublishDate(publishDate);
		bi.setPublisher(publisher);
		bi.setSummary(summary);
		bi.setCatelog(catelog);
		bi.setStock(Integer.valueOf(stock));
		bi.setSalesPrice(Double.valueOf(salePrice));
		logger.info("��ȡ��·�����ݳɹ�, youluId=" + id);
		return bi;
	}

	public Integer getBookIdByIsbn(String isbn) {
		String url = "http://www.youlu.net/search/result3/?isbn=" + isbn;
		String buf = getUrlContent(url, "gbk");
		String failMsg = "����ͼ������ 0 ��";
		if (buf.contains(failMsg)) {
			return -1;
		}
		String idStr = parseInfo(isbn, buf, "p class=\"bookname\"", "/", "\"");
		Integer bookId = Integer.parseInt(idStr);
		return bookId;
	}

	private String[] invalidTitleElements = { "�����ԭ�۲�ͬ", "����һ��", "����һ��",
			"����һ��", "����һ��", "ӡ��", "����", "ԭ�۲�ͬ", "���۲�ͬ", "�۸�ͬ", "ͳһ�ۼ�", "�������",
			"��", "��", ",", "()", "����", "(��", "��)" };

	/**
	 * @param bookName
	 * @return
	 */
	private String clearBookName(String bookName) {
		for (String elem : invalidTitleElements) {
			bookName = bookName.replace(elem, "");
		}
		bookName = bookName.replace("��", " ");
		bookName = bookName.replace("  ", " ");
		bookName = bookName.replace("  ", " ");
		return clearInvalidChar(bookName);
	}

	/**
	 * @param s
	 * @return
	 */
	private String clearInvalidChar(String s) {
		if (s == null) {
			return "";
		}
		return s.replace(",", " ").replace("\"", "").trim();
	}
}