package sample;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private Label lab;

    @FXML
    private Button but1;

    @FXML
    private TextField keyIn, keyIn1, keyIn2;

    @FXML
    private ToggleButton off, on, skitala, wheatstone, caesar;

    @FXML
    private TextArea textIn, textOut;

    @FXML
    void initialize() {
        Pattern pattern = Pattern.compile("\\D+");
        Pattern pattern1 = Pattern.compile("[^а-я]+");

        textOut.setVisible(false);
//        дефолтные ключи
        keyIn1.setText("жщнюритьцбяме.свыпч :дуокзэфгшха,лъ");
        keyIn2.setText("ичгят,жьмозюрвщц:пелъан.хэксшдбфуы ");

        //группируем кнопочки
        final ToggleGroup groupOn = new ToggleGroup();
        final ToggleGroup groupShifr = new ToggleGroup();

        on.setToggleGroup(groupOn);
        off.setToggleGroup(groupOn);

        skitala.setToggleGroup(groupShifr);
        caesar.setToggleGroup(groupShifr);
        wheatstone.setToggleGroup(groupShifr);

        butttons();

        but1.setOnAction(evt -> {
            String[] otherList = new String[]{"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я"};
            ArrayList<String> spliText = new ArrayList<>(Arrays.asList(textIn.getText().split("")));
            ArrayList<String> liters = new ArrayList<>(Arrays.asList(otherList));

            int res;
            if (off.isSelected()) {
                res = -1;
            } else {
                res = 1;
            }
            if (skitala.isSelected()) {
                Matcher matcher = pattern.matcher(keyIn.getText());
                Matcher matcher1 = pattern1.matcher(textIn.getText());

//                if(matcher.find() || matcher1.find()){
//                    lab.setText("Ошибка: ключом может быть только число, текстом только буквы русского алфавита");
//                    lab.setVisible(true);
//                } else {
                    textOut.setText(Skitala(spliText, keyIn.getText(), res));
                    textOut.setVisible(true);
                    lab.setVisible(false);
//                }
            }
            if (caesar.isSelected()) {
                Matcher matcher = pattern.matcher(keyIn.getText());
                Matcher matcher1 = pattern1.matcher(textIn.getText());

//                if (matcher.find() || matcher1.find()) {
//                    lab.setText("Ошибка: ключом может быть только число, текстом только буквы русского алфавита");
//                    lab.setVisible(true);
//                } else {
                textOut.setText(Caesar(spliText, liters, keyIn.getText(), res));
                textOut.setVisible(true);
                    lab.setVisible(false);
//            }
            }
            if (wheatstone.isSelected()) {
                System.out.println(keyIn1.getText().length());
                Matcher matcher1 = pattern1.matcher(textIn.getText());
//                if(keyIn1.getText().length() != 35 || keyIn2.getText().length() != 35 || matcher1.find()){
//                    lab.setText("Ошибка: длина ключа должна состовлять 33 символа, текстом только буквы русского алфавита");
//                    lab.setVisible(true);
//                } else {
                    textOut.setText(Wheatstone(spliText, res));
                    textOut.setVisible(true);
                    lab.setVisible(false);
//                }
            }
        });

    }

    String Caesar(ArrayList<String> spliText, ArrayList<String> liters, String key, int onOff) {
        for (int i = 0; i < spliText.size(); i++) {
            for (int j = 0; j < liters.size(); j++) {
                if (spliText.get(i).equals(liters.get(j))) {
                    int k = j + (Integer.parseInt(key) * onOff);
                    if (k > 32) {
                        k = k % 33;
                    }
                    if (k < 0) {
                        k = k + 33;
                    }
                    spliText.set(i, liters.get(k));
                    break;
                }
            }
        }
        return String.join("", spliText);
    }

    String Skitala(ArrayList<String> spliText, String key, int onOff) {
        int res = Integer.parseInt(key);
        if (onOff == -1) {
            res = spliText.size() / res;
            if (spliText.size() % Integer.parseInt(key) != 0) {
                res++;
            }
        }
        System.out.println(res);

        ArrayList<String> out = new ArrayList<>();
        int line = 0;
        int columns = 0;
        for (int i = 0; i < spliText.size(); i++) {
            if (columns + (res * line) > spliText.size() - 1) {
                line = 0;
                columns++;
            }
            out.add(spliText.get(columns + (res * line)));
            line++;
        }
        return String.join("", out);
    }


    String Wheatstone(ArrayList<String> spliText, int onOff) {
        ArrayList<String> res = new ArrayList<>();
        String[][] tabl1 = new String[7][5];
        String[][] tabl2 = new String[7][5];

        ArrayList<String> key1 = new ArrayList<>(Arrays.asList(keyIn1.getText().split("")));
        ArrayList<String> key2 = new ArrayList<>(Arrays.asList(keyIn2.getText().split("")));

        tabl1 = table(key1, tabl1);
        tabl2 = table(key2, tabl2);

        int q = 0;
        int w = 1;
        if (onOff == -1) {
            q = 1;
            w = 0;
        }

        int line_l = 0, line_p = 0, cl_l = 0, cl_p = 0;
        for (int m = 0; m < spliText.size() - 1; m++) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 5; j++) {
                    if (spliText.get(m + q).equals(tabl1[i][j])) {
                        line_l = i;
                        cl_l = j;
                    }
                    if (spliText.get(m + w).equals(tabl2[i][j])) {
                        line_p = i;
                        cl_p = j;
                    }
                }
            }
            m++;
            if (line_l == line_p) {
                if (onOff == -1) {
                    res.add(tabl1[line_p][cl_p]);
                    res.add(tabl2[line_l][cl_l]);
                } else {
                    res.add(tabl2[line_l][cl_l]);
                    res.add(tabl1[line_p][cl_p]);
                }
            } else {
                if (onOff == -1) {
                    res.add(tabl1[line_p][cl_l]);
                    res.add(tabl2[line_l][cl_p]);
                } else {
                    res.add(tabl2[line_l][cl_p]);
                    res.add(tabl1[line_p][cl_l]);
                }
            }
        }

        return String.join("", res);
    }

    //        код ниже просто стилизация кнопочек :)
    void butttons() {
        on.setOnAction(event -> onoffbutton(on, off));
        off.setOnAction(event -> onoffbutton(off, on));
        skitala.setOnAction(event -> {
            shifrbutton(skitala, caesar, wheatstone);
            keys(true, false);
        });
        caesar.setOnAction(event -> {
            shifrbutton(caesar, skitala, wheatstone);
            keys(true, false);
        });
        wheatstone.setOnAction(event -> {
            shifrbutton(wheatstone, skitala, caesar);
            keys(false, true);
        });
    }

    void onBut(ToggleButton butti) {
        butti.getStyleClass().remove("toggle-button");
        butti.getStyleClass().add("toggle-button-hover-selected");
        butti.setOpacity(1);
    }

    void offBut(ToggleButton butti) {
        butti.getStyleClass().remove("toggle-button-hover-selected");
        butti.getStyleClass().add("toggle-button");
        butti.setOpacity(0.25);
    }

    void onoffbutton(ToggleButton butti1, ToggleButton butti2) {
        if (butti1.isSelected()) {
            offBut(butti2);
            onBut(butti1);
        } else {
            offBut(butti1);
        }
    }

    void shifrbutton(ToggleButton butti1, ToggleButton butti2, ToggleButton butti3) {
        if (butti1.isSelected()) {
            offBut(butti2);
            offBut(butti3);
            onBut(butti1);
        } else {
            offBut(butti1);
        }
    }

    void keys(boolean v1, boolean v2) {
        keyIn.setVisible(v1);
        keyIn1.setVisible(v2);
        keyIn2.setVisible(v2);
        lab.setVisible(v2);
    }

    String[][] table(ArrayList<String> key1, String[][] tabl1) {
        int k = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                tabl1[i][j] = key1.get(k);
                k++;
                System.out.print(tabl1[i][j]);
            }
            System.out.println();
        }
        return tabl1;
    }
}
