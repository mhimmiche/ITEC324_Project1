import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class AdminController {
    private String username;
    private String pass;
    public boolean validateCreds(String aUsername, char[] aPass) {
        boolean isValid = false;
        readXML();
        String pw = new String(aPass);
        if (username.equals(aUsername) && BCrypt.checkpw(pw, pass)) {
            isValid = true;
        }
        return isValid;
    }

    private void readXML() {
        try {

            File fXmlFile = new File("src\\users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("admin");
            Node nNode = nList.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                username = eElement.getElementsByTagName("username").item(0).getTextContent();
                pass = eElement.getElementsByTagName("password").item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
