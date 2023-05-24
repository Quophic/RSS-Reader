package RSS;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URL;
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
        private String currentName;
        public RSSInfo getRssInfo(){
            return rssInfo;
        }

        @Override
        public void startDocument(){
            rssInfo = new RSSInfo();
            isItem = false;
            currentName = "";
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes){
            if(qName.equals(NAME_TITLE)){
                currentName = NAME_TITLE;
            }else if(qName.equals(NAME_LINK)) {
                currentName = NAME_LINK;
            }else if(qName.equals(NAME_DESCRIPTION)){
                currentName = NAME_DESCRIPTION;
            }else if(qName.equals(NAME_ITEM)){
                rssItem = new RSSItem();
                isItem = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length){
            if(currentName.equals(NAME_TITLE)){
                if(isItem){
                    rssItem.setTitle(new String(ch, start, length));
                }else {
                    rssInfo.setTitle(new String(ch, start, length));
                }
            }else if(currentName.equals(NAME_LINK)) {
                if(isItem){
                    rssItem.setLink(new String(ch, start, length));
                }else {
                    rssInfo.setLink(new String(ch, start, length));
                }
            }else if(currentName.equals(NAME_DESCRIPTION)){
                if(isItem){
                    rssItem.setDescription(new String(ch, start, length));
                }else {
                    rssInfo.setDescription(new String(ch, start, length));
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName){
            currentName = "";
            if(qName.equals(NAME_ITEM)){
                rssInfo.getItems().add(rssItem);
            }
        }
    }

    public static RSSInfo getRSSInfoFromUrl(String urlString) throws Exception{
        URL url = new URL(urlString);
        XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        RSSHandler handler = new RSSHandler();
        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(url.openStream()));
        return handler.getRssInfo();
    }

}
