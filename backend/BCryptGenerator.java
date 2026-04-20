import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String adminCombined = "Admin OtakuPassword123!admin@otaku.com";
        String budiCombined = "Kolektor BudiPassword123!budi@gmail.com";
        
        System.out.println("Admin Hash: " + encoder.encode(adminCombined));
        System.out.println("Budi Hash: " + encoder.encode(budiCombined));
    }
}
