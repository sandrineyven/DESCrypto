public class Main {

    //S-box 1
    private static int S1[] = {5, 2, 1, 6, 3, 4, 7, 0,
            3, 4, 6, 2, 0, 7, 5, 3};//**************** pb sur celui la, 2 fois le 3

    //S-box 2
    private static int S2[] = {4, 0, 6, 5, 7, 1, 3, 2,
            5, 3, 0, 7, 6, 2, 1, 4};

    //Clé de chiffrement
    private static int key = 0b010011001;

    //Nombre d'iterations voulues
    private static int NB_ITERATION = 4;


    public static void main(String[] args) {

        int decryptedMessage = 0;
        int cryptedMessage = 0;

        //MESSAGE EN CLAIR:
        int message = 0b111100110000;
        System.out.println("Message en clair : " + message);


        //Vérification des clés faibles
        //VerifCleFaible(message);

        //Application du DES
        for (int i = 1; i <= NB_ITERATION; i++) {
            cryptedMessage = DESSimpl(message, i);
        }
        System.out.println("Message crypté : " + cryptedMessage);

        //Dechiffrement
        for (int i = 1; i <= NB_ITERATION; i++) {
            decryptedMessage = DESDechiffrement(cryptedMessage, i);
        }
        System.out.println("Message decrypté : " + decryptedMessage);

        //Application du DES avec permutation
        int[] tmp = SeparationMessage12_6(message);
        message = (tmp[1] << 6) + tmp[0];
        System.out.println("Message en clair avec permutation : " + message);

        //Vérification des clés faibles
        //VerifCleFaiblePermut(message);

        //Application du DES
        for (int i = 1; i <= NB_ITERATION; i++) {
            cryptedMessage = DESSimpl(message, i);
        }
        System.out.println("Message crypté avec permutation : " + cryptedMessage);

        //Dechiffrement
        for (int i = 1; i <= NB_ITERATION; i++) {
            decryptedMessage = DESDechiffrement(cryptedMessage, i);
        }
        System.out.println("Message decrypté avec permutation : " + decryptedMessage);
    }

    private static int DESSimpl(int message, int iteration) {
        //message: 12 bits
        //Determination de L et R (6 et 6  bits)
        int[] tmp = SeparationMessage12_6(message);
        int L = tmp[1];
        int R = tmp[0] ^ CalcPartieDroite(tmp[1], iteration);

        int result = (L << 6) + R;
        return result;
    }

    private static int DESDechiffrement(int message, int iteration) {
        //message: 12 bits
        //Determination de L et R (6 et 6  bits)
        int[] tmp = SeparationMessage12_6(message);
        int R = tmp[0];
        int L = tmp[1] ^ CalcPartieDroite(tmp[0], iteration);

        int result = (L << 6) + R;
        return result;
    }

    private static int[] SeparationMessage12_6(int message) {
        int LR[] = new int[2];
        LR[1] = message % 64;
        LR[0] = (message - LR[1]) >> 6;
        return LR;
    }

    private static int[] SeparationMessage8_4(int message) {
        int LR[] = new int[2];
        LR[1] = message % 16;
        LR[0] = (message - LR[1]) >> 4;
        return LR;
    }

    private static int BitAt(int message, int bit) {
        if (message < Math.pow(2, bit)) {
            return 0;
        } else {
            int test = (int) (message / Math.pow(2, bit));
            return test % 2;
        }
    }

    //Calcul de la clé de chiffrement
    private static int KeyGen9(int key, int decalage) {
        decalage = decalage % 9;
        decalage = 9 - decalage;
        int newKey = 0;
        int dec;
        for (int i = 0; i < 8; i++) {
            dec = (decalage - i) % 9;
            if (dec < 0) {
                dec += 9;
            }
            newKey += BitAt(key, dec) << 7 - i;
        }
        return newKey;
    }

    //Fonction expender
    private static int Expender6_8(int message) {
        int tmp = (BitAt(message, 5) << 7) + (BitAt(message, 4) << 6) + (BitAt(message, 2) << 5) + (BitAt(message, 3) << 4) +
                (BitAt(message, 2) << 3) + (BitAt(message, 3) << 2) + (BitAt(message, 1) << 1) + BitAt(message, 0);
        return tmp;
    }

    //Calcul Ri+1 = Li xor f(Ri,Ki)
    private static int CalcPartieDroite(int message, int iteration) {
        //message: 6 bit
        int exp = Expender6_8(message);
        exp = exp ^ KeyGen9(key, iteration);
        int LR[] = SeparationMessage8_4(exp);
        //Utilisation des S-Box
        return (S1[LR[0]] << 3) + S2[LR[1]];
    }

    private static int DESIter(int message, int numberIterations) {
        for (int i = 0; i < numberIterations; i++) {
            message = DESSimpl(message, i);
        }
        return message;
    }

    //Verification des clés faibles
    //Résultat: pas de clé faible
    private static void VerifCleFaible(int message) {
        for (int i = 0; i < 512; i++) {
            key = i;
            if (DESIter(DESIter(message, 4), 4) == message) {
                //N'affiche seulement les clés faibles en console
                System.out.println(i);
            }
        }
    }

    private static int DESIterPermut(int message, int numberIterations) {
        for (int i = 0; i < numberIterations; i++) {
            message = DESSimpl(message, i);
        }

        int tmp[] = SeparationMessage12_6(message);
        return (tmp[1] << 3) + tmp[0];
    }

    //Verification des clés faibles avec permutation
    //Résultat: pas de clé faible
    private static void VerifCleFaiblePermut(int message) {
        for (int i = 0; i < 512; i++) {
            key = i;
            if (DESIterPermut(DESIterPermut(message, 4), 4) == message) {
                System.out.println(i);
            }
        }
    }


}