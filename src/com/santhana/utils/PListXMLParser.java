package com.santhana.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.longevitysoft.android.util.Stringer;
import com.longevitysoft.android.xml.plist.BaseXMLParser;
import com.longevitysoft.android.xml.plist.domain.PList;

import java.io.IOException;
import java.io.InputStream;

public class PListXMLParser extends BaseXMLParser {
	public static final String TAG = "PListXMLParser";

	public PListXMLParser() {
	}

	public void parse(String xml) throws IllegalStateException {
		PListXMLHandler pListHandler = (PListXMLHandler) this.getHandler();
		if (pListHandler == null) {
			throw new IllegalStateException("handler is null, must set a document handler before calling parse");
		} else if (xml == null) {
			pListHandler.setPlist((PList) null);
		} else {
			this.initParser();
			super.parse(xml);
		}
	}

	public void parse(InputStream is) throws IllegalStateException, IOException {
		PListXMLHandler pListHandler = (PListXMLHandler) this.getHandler();
		if (pListHandler == null) {
			throw new IllegalStateException("handler is null, must set a document handler before calling parse");
		} else if (is == null) {
			pListHandler.setPlist((PList) null);
		} else {
			Stringer xml = null;

			try {
				xml = Stringer.convert(is);
			} catch (IOException var5) {
				throw new IOException("error reading from input string - is it encoded as UTF-8?");
			}

			this.initParser();
			super.parse(xml.getBuilder().toString());
		}
	}

}
