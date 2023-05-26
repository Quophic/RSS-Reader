package com.ncusoft.rssreader.RSS;

import android.os.AsyncTask;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

public class RSSUtils {

    static class RSSHandler extends DefaultHandler {
        private static final String NAME_TITLE = "title";
        private static final String NAME_LINK = "link";
        private static final String NAME_DESCRIPTION = "description";
        private static final String NAME_ITEM = "item";
        private RSSInfo rssInfo;
        private RSSItem rssItem;
        private boolean isItem;
        private StringBuilder builder;
        public RSSInfo getRssInfo(){
            return rssInfo;
        }

        @Override
        public void startDocument(){
            rssInfo = new RSSInfo();
            isItem = false;
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes){
            switch (qName) {
                case NAME_ITEM:
                    rssItem = new RSSItem();
                    isItem = true;
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length){
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName){
            String data = builder.toString().trim();
            switch (qName){
                case NAME_TITLE:
                    if(isItem){
                        rssItem.setTitle(data);
                    }else{
                        rssInfo.setTitle(data);
                    }
                    break;
                case NAME_LINK:
                    if(isItem){
                        rssItem.setLink(data);
                    }else{
                        rssInfo.setLink(data);
                    }
                    break;
                case NAME_DESCRIPTION:
                    if(isItem){
                        rssItem.setDescription(data);
                    }else{
                        rssInfo.setDescription(data);
                    }
                    break;
                case NAME_ITEM:
                    rssInfo.getItems().add(rssItem);
            }
            builder.setLength(0);
        }
    }

    public static RSSInfo getRSSInfoFromUrl(String urlString) throws Exception{
        XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        RSSHandler handler = new RSSHandler();
        xmlReader.setContentHandler(handler);
        URL url = new URL(urlString);
        xmlReader.parse(new InputSource(url.openStream()));
        return handler.getRssInfo();
    }



}
