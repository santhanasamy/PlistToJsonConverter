package com.santhana.utils;
//

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.longevitysoft.android.util.Stringer;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;

public class PListXMLHandler extends DefaultHandler2 {
	public static final String TAG = "PListXMLHandler";
	private Stringer stringer = new Stringer();
	private PListParserListener parseListener;
	private Stringer tempVal;
	private PList pList;
	protected String key;

	public PListXMLHandler() {
	}

	public PList getPlist() {
		return this.pList;
	}

	public void setPlist(PList plist) {
		this.pList = plist;
	}

	public PListParserListener getParseListener() {
		return this.parseListener;
	}

	public void setParseListener(PListParserListener parseListener) {
		this.parseListener = parseListener;
	}

	public Stringer getTempVal() {
		return this.tempVal;
	}

	public void setTempVal(Stringer tempVal) {
		this.tempVal = tempVal;
	}

	public void startDocument() throws SAXException {
		super.startDocument();
		this.tempVal = new Stringer();
		this.pList = null;
		this.key = null;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		this.tempVal.newBuilder();
		if (qName.equalsIgnoreCase("plist")) {
			if (this.pList != null) {
				throw new SAXException("there should only be one PList element in PList XML");
			}

			this.pList = new PList();
		} else {
			if (this.pList == null) {
				throw new SAXException("invalid PList - please see http://www.apple.com/DTDs/PropertyList-1.0.dtd");
			}

			if (qName.equalsIgnoreCase("dict") || qName.equalsIgnoreCase("array")) {
				try {
					PListObject e = this.pList.buildObject(qName, this.tempVal.getBuilder().toString());
					this.pList.stackObject(e, this.key);
				} catch (Exception var6) {
					throw new SAXException(var6);
				}
			}
		}

	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		this.tempVal.getBuilder().append(new String(ch, start, length));
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("key")) {
			this.key = this.tempVal.getBuilder().toString().trim();
		} else if (!qName.equalsIgnoreCase("dict") && !qName.equalsIgnoreCase("array")) {
			if (!qName.equalsIgnoreCase("plist")) {
				try {
					PListObject e = this.pList.buildObject(qName, this.tempVal.getBuilder().toString());
					this.pList.stackObject(e, this.key);
				} catch (Exception var5) {
					throw new SAXException(var5);
				}

				this.key = null;
			} else if (qName.equalsIgnoreCase("plist") && this.parseListener != null) {
				this.parseListener.onPListParseDone(this.pList, PListXMLHandler.ParseMode.END_TAG);
			}
		} else {
			this.pList.popStack();
		}

		this.tempVal.newBuilder();
	}

	public static enum ParseMode {
		START_TAG, END_TAG;

		private ParseMode() {
		}
	}
}
