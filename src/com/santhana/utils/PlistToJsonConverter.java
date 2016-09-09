package com.santhana.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.google.gson.JsonObject;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.Integer;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;
import com.longevitysoft.android.xml.plist.domain.String;
import com.santhana.utils.PListXMLHandler.ParseMode;

public class PlistToJsonConverter implements PListParserListener {

	private Writer mJsonWriter = null;
	private JsonObject mRootJson = null;
	private static final boolean DEBUG = false;

	public static void main(java.lang.String[] args) {

		if (null == args || args.length == 0 || args.length < 2) {

			System.out.println("[Input parameters are missing]");
			if (DEBUG) {
				args = new java.lang.String[2];
				args[0] = ".\\local\\production.plist";
				args[1] = ".\\local\\production.json";
			} else {
				return;
			}
		}
		new PlistToJsonConverter().convert(args[0], args[1]);
	}

	public void convert(java.lang.String aInput, java.lang.String aOutput) {

		System.out.println("[Input,Output][" + aInput + "," + aOutput + "]");

		try {
			PListXMLParser lParser = new PListXMLParser();
			PListXMLHandler lHandler = new PListXMLHandler();
			lHandler.setParseListener(this);
			lParser.setHandler(lHandler);

			File file = new File(aInput);

			FileInputStream fis = null;
			fis = new FileInputStream(file.getCanonicalPath());
			mJsonWriter = new FileWriter(aOutput);
			lParser.parse(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onPListParseDone(PList lList, ParseMode var2) {

		JsonObject lHeadJson = new JsonObject();
		mRootJson = new JsonObject();
		lHeadJson.add("", mRootJson);
		Dict rootDict = (Dict) lList.getRootElement();
		parse(rootDict, "");
		try {
			mJsonWriter.write(lHeadJson.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				mJsonWriter.flush();
				mJsonWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void parse(Dict rootDict, java.lang.String aRootKey) {

		Map<java.lang.String, PListObject> lMap = rootDict.getConfigMap();

		for (java.lang.String lKeyStr : lMap.keySet()) {

			Object lPlistObj = lMap.get(lKeyStr);
			String lPlistStr = null;
			Integer lPlistInt = null;

			java.lang.String lTempStr = "";

			if (lPlistObj instanceof Dict) {

				lTempStr = (aRootKey == null || 0 == aRootKey.length()) ? lKeyStr.toString()
						: aRootKey + "." + lKeyStr.toString();

				parse((Dict) lPlistObj, lTempStr);
			} else if (lPlistObj instanceof String) {
				lPlistStr = (String) lPlistObj;
				if (null != lPlistStr) {
					mRootJson.addProperty((aRootKey + "." + lKeyStr.toString()), lPlistStr.getValue());
				}
			} else if (lPlistObj instanceof Integer) {

				lPlistInt = (Integer) lPlistObj;
				if (null != lPlistInt) {
					mRootJson.addProperty((aRootKey + "." + lKeyStr.toString()), lPlistInt.getValue());
				}
			}
		}
	}
}
