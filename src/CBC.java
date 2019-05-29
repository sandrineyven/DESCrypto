import java.math.BigInteger;

public class CBC {

    private static DES des = new DES();
    private static Utils Utils = new Utils();

    private static String binary = "0";



    public static void main(String[] args) {

        String message = "SALONZ";
        System.out.println("Message en clair : " + message);
        binary += new BigInteger(message.getBytes()).toString(2);
        System.out.println("As binary: " + binary + " size: " + binary.length());

        //Découpage en 4 blocs
        String[] blocks = new String[4];
        int b = 0;
        for(int i = 0; i< binary.length(); i += 12){
            blocks[b] = binary.substring(i,i+12);
            System.out.println("Block " + b + " : " + blocks[b]);
            b++;
        }


    }
}
