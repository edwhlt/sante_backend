package fr.hedwin;

import fr.hedwin.excel.CellProperties;
import fr.hedwin.excel.CellProperty;
import fr.hedwin.objects.Profil;
import fr.hedwin.objects.aliment.Nutriment;
import fr.hedwin.objects.elements.*;
import fr.hedwin.objects.manage.RecetteMap;
import fr.hedwin.objects.manage.RepasMap;
import fr.hedwin.objects.modele.NotAssociateException;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.exceptions.DaoException;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws DaoException, IOException {
        /*Aliment aliment = DATA.getAliment(args[0]);
        aliment.getNutriments().forEach((k, v)-> {
            System.out.println(k.getKey()+" - "+v.getValue_100g());
        });*/

        /*char c;
        do{
            printDatas();
            System.out.println("Renvoyer les données ? [y/n]");
            c = scanner.nextLine().charAt(0);
        }while (c == 'y');*/

        Week week =  DATA.getWeek(1, "38");
        System.out.println(week.getElements().size());

    }

    public static void printDatas() throws DaoException, IOException {
        //Map<Integer, Profil> profils = DATA.getUserProfils(1);

        /*System.out.println("--------------- TOTAL RECETTES -------------");
        getTableRecette(recetteMap.values()).print();

        System.out.println("--------------- TOTAL REPAS -------------");
        getTableRepas(repasMap.values().stream()
                .sorted((e1, e2) -> Comparator.comparing(r -> ((Repas)r).getDay().getRang()).compare(e1, e2))
                .collect(Collectors.toList())
        ).print();

        System.out.println("--------------- TOTAL JOURNALIER -------------");
        getTableDay(week).print();
        getTableProfil(profils.values()).print();*/

        /*Week week = DATA.getWeekWithNutriments(1, 'A');

        System.out.println("Enregistrer la liste de course ?");
        char c = scanner.nextLine().charAt(0);
        if(c == 'y') {
            Map<String, Ingredient<FoodElement>> is = saveList(week.getElements());
            try (Writer writer = new FileWriter("list.txt")) {
                StringJoiner sj = new StringJoiner("\n");
                is.forEach((k, v) -> {
                    FoodElement foodElement = v.getElement();
                    String unity = foodElement.weightUnity() > 0 ? " (Unité: "+Math.round(v.getQuantity()/foodElement.weightUnity()*100)/100.+")" : "";
                    sj.add(new Formatter().format("%-20s%-50s%-10s", k, v.name(), Math.round(v.getQuantity()*100)/100.+""+foodElement.getUnit().getUnit()).toString()+unity);
                });
                writer.write(sj.toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Enregistrer le planning au format excel ?");
        char c2 = scanner.nextLine().charAt(0);
        if(c2 == 'y') {

            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream("planning.xlsx");
                createPlanningExcel(profils.get(1), week.getElements()).write(fileOut);
                fileOut.close();
            } catch (IOException | NotAssociateException e) {
                e.printStackTrace();
            }
        }*/
    }

    public static XSSFWorkbook createPlanningExcel(Profil profil, Map<String, Day> week) throws IOException, NotAssociateException, DaoException {
        XSSFWorkbook wb = new XSSFWorkbook();
        //2. Créer une Feuille de calcul vide
        XSSFSheet feuille = wb.createSheet("Planning "+wb.getNumberOfSheets());
        CellProperties cellProperties = new CellProperties(feuille);

        //HEADER
        cellProperties.setValuesRow(0, 0,
                "Nom", "Genre", "Age", "Poids", "Taille", "Facteur", "Activité physique", "Energie (kcal)", "Glucides (g)", "Proteines (g)", "Lipides (g)"
        );
        cellProperties.setValuesRow(1, 0,
                profil.getName(), profil.getSexe() == 0 ? "Homme" : "Femme",  profil.getAge(), profil.getWeight(), profil.getSize(), profil.getFactor(), profil.getPhysical_activity(),
                profil.getEnergyPerDay(), profil.getStringCarbohydratePerDay(), profil.getStringProteinPerDay(), profil.getStringLipidPerDay()
        );
        cellProperties.updateStyle(0, 0, 1, 10, style -> {
            style.setFillForegroundColor(new XSSFColor(new Color(0, 176, 80)));
            style.setFillPattern(SOLID_FOREGROUND);
        });

        cellProperties.addValue(3, 0, "Semaine A", cellProperties::updateToGrayBold);

        int lastRow = 4;
        int sizeCol = 0;
        Map<Integer, Integer> sizeRows = new HashMap<>();
        for (Day dayResult : week.values()) {
            int c = 1;

            for(Map.Entry<String, Repas> entry : dayResult.getElements().entrySet()){
                String name = entry.getKey();
                Repas rep = entry.getValue();

                feuille.addMergedRegion(new CellRangeAddress(lastRow-1, lastRow-1, c, c+2));
                cellProperties.addValue(lastRow-1, c, name, style -> {
                    cellProperties.updateToGrayBold(style);
                    style.setAlignment(HorizontalAlignment.CENTER);
                });
                cellProperties.setValuesRow(lastRow, c, "Nom", "Ingrédients" , "Quantité");

                int r = lastRow + 1;

                if (rep == null) continue;
                for (Ingredient<FoodElement> i : rep.getElements().values()) {
                    feuille.addMergedRegion(new CellRangeAddress(r, r, c, c+1));
                    cellProperties.addValue(r, c, i.name());
                    cellProperties.addValue(r, c + 2, i.getRoundQuantity() + i.getElement().getUnit().getUnit(), style -> {
                        Font font = wb.createFont();
                        font.setItalic(true);
                        style.setFont(font);
                    });
                    r++;
                }

                for (Ingredient<Recette> i : rep.getRecettes().values()){
                    Recette rec = i.getElement();
                    int r2 = r;
                    for (Ingredient<FoodElement> i2 : rec.getElements().values()) {
                        cellProperties.addValue(r2, c + 1, i2.name());
                        cellProperties.addValue(r2, c + 2, Math.round(i.proportion() * i2.getQuantity() * 100) / 100. + i2.getElement().getUnit().getUnit(), style -> {
                            Font font = wb.createFont();
                            font.setItalic(true);
                            style.setFont(font);
                        });
                        r2++;
                    }

                    if (r2 - 1 > r) feuille.addMergedRegion(new CellRangeAddress(r, r2 - 1, c, c));
                    cellProperties.addValue(r, c, rec.name() + "\n" + rec.getRecette(), style -> {
                        style.setWrapText(true);
                    });
                    r = r2;
                }

                int dayRang = dayResult.getDayType().getRang();
                if(sizeRows.containsKey(dayRang)) {
                    int sr = sizeRows.get(dayRang);
                    if(sr < r) sizeRows.replace(dayRang, sr);
                }else{
                    sizeRows.put(dayRang, r);
                }
                c += 3;
            }

            int sizeRow = sizeRows.get(dayResult.getDayType().getRang());

            // JOUR
            feuille.addMergedRegion(new CellRangeAddress(lastRow+1, sizeRow-1, 0, 0));
            cellProperties.addValue(lastRow+1, 0, dayResult.getDayType().getValue(), cellProperties::updateToGrayBold);

            // TOTAUX MACRO
            cellProperties.setValuesColumn(sizeRow, 0, "Energie (kcal)", "Glucides (g)", "Proteines (g)", "Lipides (g)");

            int c_r = 3;
            for(Map.Entry<String, Repas> entry : dayResult.getElements().entrySet()) {
                Repas rep = entry.getValue();
                cellProperties.setValuesColumn(sizeRow, c_r,
                        String.valueOf(Math.round(rep.getApport(Nutriment.ENERGY)*100/4.184)/100.),
                        rep.getStringApport(Nutriment.CARBOHYDRATES),
                        rep.getStringApport(Nutriment.PROTEINS),
                        rep.getStringApport(Nutriment.FAT)
                );
                c_r += 3;
            }

            if (sizeCol < c) sizeCol = c;
            lastRow = 5+sizeRow;
        }


        // TOTAUX MACRO JOURNA
        cellProperties.addValue(3, sizeCol, "Totaux Journaliers", cellProperties::updateToGrayBold);
        for (Day dayResult : week.values()) {
            int sizeRow = sizeRows.get(dayResult.getDayType().getRang());

            cellProperties.setValuesColumn(sizeRow, sizeCol,
                    String.valueOf(Math.round(dayResult.getApport(Nutriment.ENERGY)*100/4.184)/100.),
                    dayResult.getStringApport(Nutriment.CARBOHYDRATES),
                    dayResult.getStringApport(Nutriment.PROTEINS),
                    dayResult.getStringApport(Nutriment.FAT)
            );

            cellProperties.updateStyle(sizeRow, 0, sizeRow+3, sizeCol, style -> {
                style.setFillForegroundColor(new XSSFColor(new Color(244, 176, 132)));
                style.setFillPattern(SOLID_FOREGROUND);
            });


            int sizeRowBefore = dayResult.getDayType().getRang() != 0 ? sizeRows.get(dayResult.getDayType().getRang()-1) : -1;
            cellProperties.updateStyle(sizeRowBefore+4, 0, sizeRowBefore+4, sizeCol, cellProperties::updateToGrayBold);
            cellProperties.updateStyle(sizeRowBefore+5, 0, sizeRowBefore+5, sizeCol, style -> {
                style.setFillForegroundColor(new XSSFColor(new Color(255, 217, 102)));
                style.setFillPattern(SOLID_FOREGROUND);
                style.setBorderTop(BorderStyle.THIN);
            });
        }


        for(Map.Entry<Integer, List<CellProperty>> entry : cellProperties.getRows().entrySet()){
            Row row = feuille.createRow(entry.getKey());
            for(CellProperty cellProperty : entry.getValue()){
                Cell cell = row.createCell(cellProperty.getColumn());
                cell.setCellValue(cellProperty.getValue().toString());
                cell.setCellStyle(cellProperty.getCellStyle());
            }
        }

        for(int i = 0; i <= sizeCol; i++) feuille.autoSizeColumn(i);

        return wb;
    }

    public static Map<String, Ingredient<FoodElement>> saveList(Map<String, Day> week){
        Map<String, Ingredient<FoodElement>> foods = new HashMap<>();
        week.forEach((k, d) -> {
            d.getFoodIngredients().forEach((key, ing) -> {
                String code = ing.getElement().getCode();
                if(foods.containsKey(code)) {
                    Ingredient<FoodElement> lastIng = foods.get(code);
                    lastIng.setQuantity(lastIng.getQuantity() + ing.getQuantity());
                }else foods.put(code, ing);
            });
        });
        return foods;
    }

    public static TableList getTableProfil(Collection<? extends Profil> profils){
        TableList tl = new TableList("Nom", "Energie (kcal)", "Glucides (g)", "Proteine (g)", "Lipide (g)");
        profils.forEach(p -> tl.addRow(p.getName(), Math.round(p.getEnergyPerDay())+"",
                p.getStringCarbohydratePerDay(),
                p.getStringProteinPerDay(),
                p.getStringLipidPerDay()));
        return tl.withUnicode(true);
    }

}
