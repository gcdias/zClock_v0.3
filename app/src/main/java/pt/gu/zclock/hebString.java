package pt.gu.zclock;

import java.util.Vector;

/**
 * Created by GU on 14-02-2015.
 */
public class hebString {

    //region class enumerations

    public enum chilufi {
        Albam,      //
        Atbash,     //
        Achbi,      //
        AyiqBekher, //
        AchasBeta,  //
        Atbach      //
    }

    public enum Mispar {

        //http://www.jewishencyclopedia.com/articles/6571-gematria

        Hechrachi,      //1. Normal: m' mechrach, mispar hechrachi

        MeugalKlali,    //2. Ciclico ou menor: m qatan, mispar me'ugal klali, hagilgul chezrat

        Qidmi,          //3. Inclusivo: mispar qdimi (letra valor triangular)

        Musafi,          //4. Aditorio: mispar musafi quando nº externo de palavras ou letras é adicionado

        MereviaKlali,   //5. Quadratico da palavra: mispar merevia klali: valor da palavra * valor de cada letra = quadrado da palavra

        MereviaPerati,  //6. Quadratico da letra: mispar merevia perati: soma dos quadrados da letras

        Shemi72,        //7. Nominal: mispar shemi: valor do nome da letra
        Shemi63,
        Shemi52,
        Shemi45,

        Mispari,        //8. Numeral: mispar mispari: valor do nome do numero da letra

        MispariHagadol, //9. Numeral Maior: mispari hagadol: numeral com integração: (yod) = yod = esrim;

        Chitzuni,       //10. Externo: mispar chitzuni: (contagem de letras) todas as letras valem 1 não aplicado a YHVH (Asis Rimonim 36b)

        Gadol,          //11. Maior: mispar gadol: contagem das formas finais 500-900 (mispar gadol mntzpkh)

        Kaful,          //12. Multiplo: mispar kaful: (cf.III.D.c) multiplicação das letras

        Chalqi,         //13. Quociente: mispar chalqui: (cf. III.D.d)

        MeaqavKlali,    //14. Cubico da palavra: m' meshalosh klali, mispar me'akav klali: valor cubico da palavra

        MeaqavPerati,   //15. Cubico da letra: mispar me'akav perati: cubo da letra normal (cf. Chayyath, Minchat Yehudi)

        EserKlalot,     //16. Involução primeira decada: eser mispar klalot: cf.III.D.a

        HaklalotKlalot, //17. Involução segunda decada: haklalot mispar klalot:

        ShemiSheni,     //18. Dupla integração: mispar shemi sheni

        Temuri,         //19. Permutação: mispar temuri (II.2.c), quando o valor das letras permutadas

        Revua           //20-22. Quaternionico: mispar revua: da palavra (20), integrada (21) e integração dupla (22)
    }

    public enum gematriaTable {
        MisparHechrachi,  //0-Standard
        MisparGadol,      //1-Sofit
        Milui72,          //2
        Milui63,          //3
        Milui45,          //4
        Milui52,          //5
        MisparSiduri,     //6-Contagem ordinal 1>22
        MisparKatan,      //7-sem zeros iod=1, kaf=2,...
        MisparKidmi,      //8-Standard triangular 1,3,6...(n^2+n)/2
        MisparPerati,     //9-Standard quadratico 1,4,9...n^2
        MisparNeelam      //10
    }

    public enum gematriaMethod {
        Sum,    //0,
        Mul,    //1,
        Div,    //2,
        Sub     //3;
    }

    //endregion

    public final int hebKEEPSPACE= 1;
    public final int hebOTIOT    = 2;
    public final int hebNIQUDIM  = 4;
    public final int hebTAAMIM   = 8;
    public final int hebHEBPUNCT = 16;
    public final int hebPUNCT    = 32;

    private String hebstring;
    private String hebniqqud;
    private String otsequence;

    public hebString(String string){
        this.hebstring  = string;
        this.otsequence = getOtsequence();
    }

    @Override
    public String toString(){
        return hebstring;
    }

    public String toString(int flags){

        String res = "";
        for (char c : hebstring.toCharArray()) {
            if (
                    ((flags & hebKEEPSPACE) == hebKEEPSPACE && c == 0x0020) ||
                    ((flags & hebOTIOT) == hebOTIOT && c > 0x05ef && c < 0x05eb) ||
                    ((flags & hebNIQUDIM) == hebNIQUDIM && c > 0x05af && c < 0x05c8 && c != 0x05be && c != 0x05c0 && c != 0x05c3 && c != 0x05c6) ||
                    ((flags & hebTAAMIM) == hebTAAMIM && c > 0x0590 && c < 0x05af) ||
                    ((flags & hebKEEPSPACE) == hebKEEPSPACE && c == 0x0020) ||
                    ((flags & hebHEBPUNCT) == hebHEBPUNCT && (c == 0x05be || c == 0x05c0 || c == 0x05c3 || c == 0x05c6 || c == 0x05f3 || c == 0x05f4)) ||
                    ((flags & hebPUNCT) == hebPUNCT && (c > 0x0020 && c < 0x0040))
                    )
                res += c;
        }
        return res;
    }

    private String getOtsequence(){
        String res = "";
        for (char c : hebstring.toCharArray()) {
            if (c > 0x05ef && c < 0x05eb) res += c;
        }
        return res;
    }

    private int[] getFactorArray(){
        int i = otsequence.length();
        Vector<Integer> fa = new Vector<>();
        for (int j=i;j>0;j--){

        }
        return new int[27];
    }
    
    public class Gematria{
        
        private int[] gematriaTable;

        private Mispar _msp;

        private double _gematria;

        private String _word;

        public Gematria(Mispar mispar, String word){
            _msp = mispar;
            setMispar();
            this._word = word;
        }
        
        private int[] setMispar(){
            switch (_msp){

                case Hechrachi:
                //1. Normal: m' mechrach, mispar hechrachi
                case Musafi:
                //4. Aditorio: mispar musafi quando nº externo de palavras ou letras é adicionado
                    return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400 };

                case MeugalKlali:
                    //2. Ciclico ou valor menor: m qatan, mispar me'ugal klali, hagilgul chezrat
                    return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 2, 3, 4, 4, 5, 5, 6, 7, 8, 8, 9, 9, 1, 2, 3, 4 };

                case Qidmi:
                    //3. Inclusivo: mispar qdimi (letra valor triangular)
                    return new int[] { 1,3,6,10,15,21,28,36,45,55,210,210,465,820,820,1275,1275,1830,2485,3240,3240,4095,4095,5050,20100,45150,80200};

                case MereviaKlali:
                   //5. Quadratico da palavra: mispar merevia klali: valor da palavra * valor de cada letra = quadrado da palavra

                case MereviaPerati:
                  //6. Quadratico da letra: mispar merevia perati: soma dos quadrados da letras

                case Shemi72:
                    //7. Nominal: mispar shemi: valor do nome da letra
                    gematriaTable = new int[] { 111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                    break;

                case Shemi63:
                    gematriaTable = new int[] { 111, 412, 83, 434, 15, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                    break;

                case Shemi52:
                    gematriaTable = new int[] { 111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                    break;

                case Shemi45:
                    gematriaTable = new int[] { 111, 412, 83, 434, 6, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406 };
                    break;

                case Mispari:
                     //8. Numeral: mispar mispari: valor do nome do numero da letra

                case MispariHagadol: //9. Numeral Maior: mispari hagadol: numeral com integração: (yod) = yod = esrim;


                case Chitzuni:       //10. Externo: mispar chitzuni: (contagem de letras) todas as letras valem 1 não aplicado a YHVH (Asis Rimonim 36b)

                case Gadol:
                    //11. Maior: mispar gadol: contagem das formas finais 500-900 (mispar gadol mntzpkh)
                    return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 500, 20, 30, 600, 40, 700, 50, 60, 70, 800, 80, 900, 90, 100, 200, 300, 400 };

                case Kaful:          //12. Multiplo: mispar kaful: (cf.III.D.c) multiplicação das letras

                case Chalqi:         //13. Quociente: mispar chalqui: (cf. III.D.d)

                case MeaqavKlali:    //14. Cubico da palavra: m' meshalosh klali, mispar me'akav klali: valor cubico da palavra

                case MeaqavPerati:   //15. Cubico da letra: mispar me'akav perati: cubo da letra normal (cf. Chayyath, Minchat Yehudi)

                case EserKlalot:     //16. Involução primeira decada: eser mispar klalot: cf.III.D.a

                case HaklalotKlalot: //17. Involução segunda decada: haklalot mispar klalot:

                case ShemiSheni:     //18. Dupla integração: mispar shemi sheni

                case Temuri:         //19. Permutação: mispar temuri (II.2.c), quando o valor das letras permutadas

                case Revua:          //20-22. Quaternionico: mispar revua: da palavra (20), integrada (21) e integração dupla (22)



                //case Siduri: gematriaTable = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 23, 11, 12, 24, 13, 25, 14, 15, 16, 26, 17, 18, 16, 19, 20, 21, 22 }; break;    //6-Contagem ordinal 1>22
                //case Perati: gematriaTable = new int[] {}; break;    //9-Standard quadratico 1,4,9...n^2
                //case Neelam: gematriaTable = new int[] {}; break;   //10
            }
            return null;
        }

        public void setMisparMethod(Mispar value) {
            _msp = value;
            int[] tbl;
            switch (_msp) {
                case Hechrachi:
                    tbl = new int[]{
                            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400
                    };
                    _gematria = GetGematria(tbl);
                    break;
                case MeugalKlali:
                    tbl = new int[] {
                            1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 5, 2, 3, 6, 4, 7, 5, 6, 7, 8, 8, 9, 9, 1, 2, 3, 4
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
                case Qidmi:
                    tbl = new int[] {
                            1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 210, 210, 465, 820, 820, 1275, 1275, 1830, 2485, 3240, 3240, 4095, 4095, 5050, 20100, 45150, 80200
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
                case Musafi:
                    tbl = new int[] {
                            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400
                    } ;
                    _gematria = GetGematria(tbl);
                    _gematria += OtiotCount();
                    break;
                case MereviaKlali:
                    tbl = new int[] {
                            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 20, 30, 40, 40, 50, 50, 60, 70, 80, 80, 90, 90, 100, 200, 300, 400
                    } ;
                    _gematria = GetGematria(tbl);
                    _gematria *= _gematria;
                    break;
                case MereviaPerati:
                    tbl = new int[] {
                            1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 400, 400, 900, 1600, 1600, 2500, 2500, 3600, 4900, 6400, 6400, 8100, 8100, 10000, 40000, 90000, 160000
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
                case Shemi72:
                    tbl = new int[] {
                            111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
                case Shemi63:
                    tbl = new int[] {
                            111, 412, 83, 434, 15, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
                case Shemi45:
                    tbl = new int[] {
                            111, 412, 83, 434, 6, 13, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
                case Shemi52:
                    tbl = new int[] {
                            111, 412, 83, 434, 10, 12, 67, 418, 419, 20, 100, 100, 74, 80, 80, 106, 106, 120, 130, 81, 81, 104, 104, 186, 510, 360, 406
                    } ;
                    _gematria = GetGematria(tbl);
                    break;
            }
        }

        private int GetGematria(int[]tbl){
            return Get(tbl);
        }

        private int Get(int[] table) {
            int g = 0;
            for (char c : _word.toCharArray()) {
                int i = c - 0x05d0;
                if (i >= 0 && i < 27) g += (char) (table[i] + 0x05cf);
            }
            return g;
        }

        private int OtiotCount() {
            int g = 0;
            for (char c : _word.toCharArray()) {
                int i = c - 0x05d0;
                g += (i >= 0 && i < 27) ? 1 : 0;
            }
            return g;
        }

        private int MilimCount() {
            if (_word == null) return 0;
            //String[] sep = {" ", "-", "־"};
            String[] s = _word.split(" |-|־");
            return s.length;
        }
    }

}