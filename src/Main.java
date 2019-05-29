@SuppressWarnings("MagicConstant")
public class Main {


    //Nombre d'iterations voulues
    private static int NB_ITERATION = 4;

    private static DES des = new DES();
    private static Utils Utils = new Utils();

    public static void main(String[] args) {

        int decryptedMessage = 0;
        int cryptedMessage = 0;

        //MESSAGE EN CLAIR (12 bits):
        int message = 0b111010110000;

        System.out.println("Message en clair : " + message);


        //Vérification des clés faibles
        des.VerifCleFaible(message);

        //Application du DES
        for (int i = 1; i <= NB_ITERATION; i++) {
            cryptedMessage = des.DESSimpl(message, i);
        }
        System.out.println("Message crypté : " + cryptedMessage);

        //Dechiffrement
        for (int i = 1; i <= NB_ITERATION; i++) {
            decryptedMessage = des.DESDechiffrement(cryptedMessage, i);
        }
        System.out.println("Message decrypté : " + decryptedMessage);

        //Application du DES avec permutation
        int[] tmp = Utils.SeparationMessage12_6(message);
        message = (tmp[1] << 6) + tmp[0];
        System.out.println("Message en clair avec permutation : " + message);

        //Vérification des clés faibles
        des.VerifCleFaiblePermut(message);

        //Application du DES
        for (int i = 1; i <= NB_ITERATION; i++) {
            cryptedMessage = des.DESSimpl(message, i);
        }
        System.out.println("Message crypté avec permutation : " + cryptedMessage);

        //Dechiffrement
        for (int i = 1; i <= NB_ITERATION; i++) {
            decryptedMessage = des.DESDechiffrement(cryptedMessage, i);
        }
        System.out.println("Message decrypté avec permutation : " + decryptedMessage);
    }
}