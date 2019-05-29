public class DES {


    private static Utils Utils = new Utils();

    //S-box 1
    private int S1[] = {5, 2, 1, 6, 3, 4, 7, 0,
            3, 4, 6, 2, 0, 7, 5, 3};//**************** pb sur celui la, 2 fois le 3

    //S-box 2
    private int S2[] = {4, 0, 6, 5, 7, 1, 3, 2,
            5, 3, 0, 7, 6, 2, 1, 4};


    //Clé de chiffrement
    public int key = 0b010011001;

    public int DESSimpl(int message, int iteration) {
        //message: 12 bits
        //Determination de L et R (6 et 6  bits)
        int[] tmp = Utils.SeparationMessage12_6(message);
        int L = tmp[1];
        int R = tmp[0] ^ CalcPartieDroite(tmp[1], iteration);

        int result = (L << 6) + R;
        return result;
    }

    public int DESDechiffrement(int message, int iteration) {
        //message: 12 bits
        //Determination de L et R (6 et 6  bits)
        int[] tmp = Utils.SeparationMessage12_6(message);
        int R = tmp[0];
        int L = tmp[1] ^ CalcPartieDroite(tmp[0], iteration);

        int result = (L << 6) + R;
        return result;
    }

    //Calcul de la clé de chiffrement
    public int KeyGen9(int key, int decalage) {
        decalage = decalage % 9;
        decalage = 9 - decalage;
        int newKey = 0;
        int dec;
        for (int i = 0; i < 8; i++) {
            dec = (decalage - i) % 9;
            if (dec < 0) {
                dec += 9;
            }
            newKey += Utils.BitAt(key, dec) << 7 - i;
        }
        return newKey;
    }

    //Fonction expender
    public int Expender6_8(int message) {
        int tmp = (Utils.BitAt(message, 5) << 7)
                + (Utils.BitAt(message, 4) << 6)
                + (Utils.BitAt(message, 2) << 5)
                + (Utils.BitAt(message, 3) << 4)
                + (Utils.BitAt(message, 2) << 3)
                + (Utils.BitAt(message, 3) << 2)
                + (Utils.BitAt(message, 1) << 1)
                + Utils.BitAt(message, 0);
        return tmp;
    }

    //Calcul Ri+1 = Li xor f(Ri,Ki)
    public int CalcPartieDroite(int message, int iteration) {
        //message: 6 bit
        int exp = Expender6_8(message);
        exp = exp ^ KeyGen9(key, iteration);
        int LR[] = Utils.SeparationMessage8_4(exp);
        //Utilisation des S-Box
        return (S1[LR[0]] << 3) + S2[LR[1]];
    }

    public int DESIter(int message, int numberIterations) {
        for (int i = 0; i < numberIterations; i++) {
            message = DESSimpl(message, i);
        }
        return message;
    }

    public int DESIterPermut(int message, int numberIterations) {
        for (int i = 0; i < numberIterations; i++) {
            message = DESSimpl(message, i);
        }

        int tmp[] = Utils.SeparationMessage12_6(message);
        return (tmp[1] << 3) + tmp[0];
    }

    //Verification des clés faibles
    //Résultat: pas de clé faible
    public void VerifCleFaible(int message) {
        for (int i = 0; i < 512; i++) {
            key = i;
            if (DESIter(DESIter(message, 4), 4) == message) {
                //N'affiche seulement les clés faibles en console
                System.out.println(i);
            }
        }
    }

    //Verification des clés faibles avec permutation
    //Résultat: pas de clé faible
    public void VerifCleFaiblePermut(int message) {
        for (int i = 0; i < 512; i++) {
            key = i;
            if (DESIterPermut(DESIterPermut(message, 4), 4) == message) {
                System.out.println(i);
            }
        }
    }


}
