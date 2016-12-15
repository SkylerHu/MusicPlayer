package com.anjoyo.musicplayer.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingYinUtil {
	
	public static String getPingYin(String inputString) {
		String outputString = "";
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置大小写
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置声调———无声调
		format.setVCharType(HanyuPinyinVCharType.WITH_V);//特殊拼音ü的显示格式——用v显示
		
		char[] input = inputString.trim().toCharArray();
		try {
			for (int i = 0; i < input.length; i++) {
                if (java.lang.Character.toString(input[i]).
                matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    outputString += temp[0];
                } else {
                	if (input[i] >= 'a') {
						input[i] = (char) (input[i] -32);
					}
                	outputString += java.lang.Character.toString(input[i]);
                }
            }
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		
		return outputString;
	}
	
	
}
