package com.anjoyo.musicplayer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.anjoyo.musicplayer.bean.MusicBean;
import com.anjoyo.musicplayer.define.Constant;

public class XmlDom4jUtil {

	/**
	 * 获得“我的最爱”音乐id
	 * @return
	 */
	public static List<Integer> getFavoriteMusic() {
		List<Integer> list = new ArrayList<Integer>();
		File path = new File(Constant.FILE_PATH);
		if (! path.exists()) {
			path.mkdirs();
		}
		
		File file = new File(Constant.FILE_PATH + File.separator + Constant.FILE_XML_FAVORITE_MUSIC);
		if (file.exists()) {
			try {
				SAXReader saxReader = new SAXReader();
				Document document = saxReader.read(file);
				Element rootElement = document.getRootElement();
				for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext();) {
					Element element = (Element) iterator.next();
					list.add(Integer.parseInt(element.getText()));
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/** 
	 * 保存“我的最爱”音乐id
	 * @param list
	 */
	public static void saveFavoriteMusic(List<MusicBean> musicBeans) {
		List<Integer> list = new ArrayList<Integer>();
		for (MusicBean musicBean : musicBeans) {
			if (musicBean.isFavorite()) {
				list.add(musicBean.getId());
			}
		}
		File file = new File(Constant.FILE_PATH + File.separator + Constant.FILE_XML_FAVORITE_MUSIC);
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement(Constant.FILE_XML_ROOT);
		for (Integer integer : list) {
			Element element = rootElement.addElement(Constant.FILE_XML_ELEMENT);
			element.addText(integer+"");
		}
		
		try {
			Writer fileWriter = new FileWriter(file);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
			xmlWriter.write(document);
			xmlWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
