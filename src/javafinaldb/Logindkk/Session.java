package javafinaldb.Logindkk;

public class Session {
    public static int id; // Pastikan namanya 'id' agar sesuai dengan TransaksiForm
    public static String username;
    public static String role;

    public static void logout() {
        id = 0;
        username = null;
        role = null;
    }
}