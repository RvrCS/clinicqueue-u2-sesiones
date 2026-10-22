package lab.clinicqueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Emite y valida JSON Web Tokens firmados con HS256 (header.payload.firma).
 */
@Service
public class JwtService {

    private static final String SECRET = "clinic-secret";

    private final ObjectMapper json = new ObjectMapper();
    private final Base64.Encoder b64 = Base64.getUrlEncoder().withoutPadding();
    private final Base64.Decoder b64d = Base64.getUrlDecoder();

    public String issue(Patient p) {
        try {
            Map<String, Object> header = Map.of("alg", "HS256", "typ", "JWT");

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sub", p.username());
            payload.put("name", p.name());
            payload.put("role", p.role());
            payload.put("phone", p.phone());
            payload.put("reason", p.reason());
            payload.put("ticket", p.ticket());

            String h = b64.encodeToString(json.writeValueAsBytes(header));
            String pl = b64.encodeToString(json.writeValueAsBytes(payload));
            String signingInput = h + "." + pl;
            String sig = b64.encodeToString(hmacSha256(signingInput));
            return signingInput + "." + sig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Valida la firma y devuelve el payload. Lanza si la firma no coincide. */
    @SuppressWarnings("unchecked")
    public Map<String, Object> verify(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new IllegalArgumentException("formato invalido");
            String expected = b64.encodeToString(hmacSha256(parts[0] + "." + parts[1]));
            if (!expected.equals(parts[2])) throw new IllegalArgumentException("firma invalida");
            return json.readValue(b64d.decode(parts[1]), Map.class);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] hmacSha256(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
