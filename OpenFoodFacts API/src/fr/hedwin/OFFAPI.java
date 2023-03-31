package fr.hedwin;

import com.google.gson.Gson;
import fr.hedwin.object.json.Element;
import fr.hedwin.object.json.exceptions.NotFoundElementOFFAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class OFFAPI {

    public static Element getProduct(String code_barre) throws NotFoundElementOFFAPI {
        try{
            String json = readUrl("https://fr.openfoodfacts.org/api/v0/product/"+code_barre+".json");
            Gson gson = new Gson();
            Element element = gson.fromJson(json, Element.class);
            if(element.getStatus() == 0) throw new NotFoundElementOFFAPI("Aucun produit n'est associé au code barre : "+code_barre);
            else return element;
        }catch (Exception e){
            throw new NotFoundElementOFFAPI(e.getMessage());
        }
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

}