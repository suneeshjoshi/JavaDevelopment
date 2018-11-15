import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
 
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
 
/**
 *
 * @author Suneesh Joshi
 */
public class XMLDom {
 
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;
    Document document;
 
    public void createXML(ArrayList<HashMap<String, String>> ar) {
        try {
 
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
 
            Element orderElement = document.createElement("Restatement");
            document.appendChild(orderElement);
 
            Element orderDetailElement = document.createElement("Products");
            orderElement.appendChild(orderDetailElement);
 
            for(HashMap<String,String> ele:ar){
                Element productElement = document.createElement("Product");
                orderElement.appendChild(productElement);
            	for(String s:ele.keySet()){
		            String key = s;
		            Element detailElement = document.createElement(key);
		            detailElement.appendChild(document.createTextNode(ele.get(s)));
		            productElement.appendChild(detailElement);
            	}
 
            }
 
            File xmlFile = new File("G:\\SJ_Test.xml");
            xmlFile.createNewFile();
            FileOutputStream isod = new FileOutputStream(xmlFile);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
 
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(isod);
            transformer.transform(source, result);
 
            isod.flush();
            isod.close();
            
            System.out.println("result = "+source);
            System.out.println("result = "+source.toString());
            System.out.println("result = "+result);
 
        } catch (TransformerException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
    }
 
    public static void main(String[] args) {
 
    	ArrayList<HashMap<String,String>> ar = new ArrayList<HashMap<String,String>>();
    	
        XMLDom cXML = new XMLDom();

        HashMap<String,String> m = new HashMap<String,String>();
        m.put("file_prefix", "emtxc11");
        m.put("date", "11 Jan 2016");
        m.put("result", "SUCCESS");
        m.put("link", "ftp://test.mail.com");
        
        ar.add(m);
        m.clear();
        
        m.put("file_prefix", "emtxc17");
        m.put("date", "31 Jan 2016");
        m.put("result", "FAIL");
        m.put("link", "some error");

        ar.add(m);
        System.out.println(ar);
        
        cXML.createXML(ar);
    }
}
