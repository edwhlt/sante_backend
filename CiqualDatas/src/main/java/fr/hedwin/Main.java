package fr.hedwin;

import fr.hedwin.objects.CiqualIngredient;
import fr.hedwin.objects.elements.FoodElement;
import fr.hedwin.objects.modele.Unitable;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import me.tongfei.progressbar.ProgressBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    private static Map<String, String> files;
    private static final DaoFactory daoFactory = DaoFactory.getInstance();
    private static String VERSION = "2020_07_07";

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        try {
            VERSION = args[0];
        }catch (ArrayIndexOutOfBoundsException ignored){}

        files = openZIP();
        Map<String, CiqualIngredient> ciqualIngredientMap = loadAliment();
        Map<String, String> units = loadUnit();
        loadNutriments(ciqualIngredientMap, units);

        try {
            daoFactory.getCiqualIngredientDao().truncate();
            daoFactory.getCiqualIngredientDao().add(ciqualIngredientMap);
            Map<String, FoodElement> foodElementMap = ciqualIngredientMap.values().stream().map(c -> new FoodElement(c.getCode(), c.getCode(), c.getName(), c.getUnit().getUnit())).collect(Collectors.toMap(FoodElement::key, f -> f));
            //PENSER A MODIFIER LES UNITE MUNUELLEMENT APRES L'ENVOIE DES DONNEES
            daoFactory.getFoodElementDao().add(foodElementMap);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> loadUnit() throws ParserConfigurationException, IOException, SAXException {
        Map<String, String> units = new HashMap<>();
        readXML(files.get("const_"+VERSION+".xml"), doc -> {
            NodeList nodeList = doc.getElementsByTagName("CONST");
            for (int itr = 0; itr < nodeList.getLength(); itr++){
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                    String const_code = getElementText(eElement, "const_code");
                    String name = getElementText(eElement, "const_nom_fr");
                    Matcher unit = Pattern.compile("\\((.+?)/100 g\\)").matcher(name);
                    if(unit.find()) units.put(const_code, unit.group(1));
                }
            }
        });
        return units;
    }

    private static void loadNutriments(Map<String, CiqualIngredient> ciqualIngredientMap, Map<String, String> units) throws ParserConfigurationException, IOException, SAXException {
        readXML(files.get("compo_"+VERSION+".xml"), doc -> {
            NodeList nodeList = doc.getElementsByTagName("COMPO");
            for (int itr = 0; itr < nodeList.getLength(); itr++){
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                    String key = getElementText(eElement, "alim_code");
                    String const_key = getElementText(eElement, "const_code");
                    String value = getElementText(eElement, "teneur");
                    double v = 0;
                    try{
                        v = Double.parseDouble(value.replace(",", "."));
                    }catch (NumberFormatException ignored){}
                    if (ciqualIngredientMap.containsKey(key)) {
                        ciqualIngredientMap.get(key).addNutriment(const_key, v, units.get(const_key));
                    }
                }
            }
        });
    }

    private static Map<String, CiqualIngredient> loadAliment() throws IOException, SAXException, ParserConfigurationException {
        Map<String, CiqualIngredient> ciqualIngredientMap = new HashMap<>();
        readXML(files.get("alim_"+VERSION+".xml"), doc -> {
            NodeList nodeList = doc.getElementsByTagName("ALIM");
            for (int itr = 0; itr < nodeList.getLength(); itr++){
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                    String key = getElementText(eElement, "alim_code");
                    String name = getElementText(eElement, "alim_nom_fr");
                    String group_code = getElementText(eElement, "alim_grp_code");
                    ciqualIngredientMap.put(key, new CiqualIngredient(key, name, group_code.equals("06") ? Unitable.Unit.MILILITRE : Unitable.Unit.GRAMME));
                }
            }
        });
        return ciqualIngredientMap;
    }

    private static String getElementText(Element element, String tagName){
        String text = element.getElementsByTagName(tagName).item(0).getTextContent();
        return text.substring(1, text.length()-1);
    }

    private static void readXML(String content, Consumer<Document> consumer) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(content.replace("(<", "(&lt;").replace(" < ", " &lt; ").replace("&", "&amp;"))));
        doc.getDocumentElement().normalize();
        consumer.accept(doc);
    }

    private static Map<String, String> openZIP() throws IOException {
        //String absolute_path = URLDecoder.decode(Objects.requireNonNull(Main.class.getClassLoader().getResource(path)).getFile(), StandardCharsets.UTF_8);
        Map<String, String> content = new HashMap<>();
        URL url = new URL("https://ciqual.anses.fr/cms/sites/default/files/inline-files/XML_"+VERSION+".zip");
        ZipInputStream stream = new ZipInputStream(url.openConnection().getInputStream(), StandardCharsets.ISO_8859_1);
        byte[] buffer = new byte[2048];


        try (ProgressBar pb = new ProgressBar("Chargement des nutriments: ", 100)) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                StringBuilder sb = new StringBuilder();
                int read;
                while ((read = stream.read(buffer, 0, buffer.length)) >= 0) {
                    pb.stepBy(buffer.length);
                    sb.append(new String(buffer, 0, read, StandardCharsets.ISO_8859_1));
                }
                content.put(entry.getName(), sb.toString());
            }
            return content;
        }
    }

}
