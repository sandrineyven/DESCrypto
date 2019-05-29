public class Utils {

    public int[] SeparationMessage12_6(int message) {
        int LR[] = new int[2];
        LR[1] = message % 64;
        LR[0] = (message - LR[1]) >> 6;
        return LR;
    }

    public int[] SeparationMessage8_4(int message) {
        int LR[] = new int[2];
        LR[1] = message % 16;
        LR[0] = (message - LR[1]) >> 4;
        return LR;
    }

    public int BitAt(int message, int bit) {
        if (message < Math.pow(2, bit)) {
            return 0;
        } else {
            int test = (int) (message / Math.pow(2, bit));
            return test % 2;
        }
    }
}
