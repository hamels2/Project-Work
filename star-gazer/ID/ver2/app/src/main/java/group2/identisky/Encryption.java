package group2.identisky;

public class Encryption {

    final static int key = 2;

    public static void main(String[] args){
        String original = "skyVisibility";
        String encrypted = encrypt(original);
        String decrypted = decrypt(encrypted);
        System.out.println(original+" "+encrypted+" "+decrypted);
    }

    public static char shiftChar(char original, int amount) {
        int k = (int)amount;
        int o = (int)original;
        char encrypted = (char)(o+k);
        return encrypted;
    }

    public static String encrypt(String str){
        char c;
        String result = "";
        for (int i = 0; i < str.length(); i++){
            c = shiftChar(str.charAt(i), key);
            result += c;
        }
        return result;
    }

    public static String decrypt(String str){
        char c;
        String result = "";
        for (int i = 0; i < str.length(); i++){
            c = shiftChar(str.charAt(i), -key);
            result += c;
        }
        return result;
    }
}
