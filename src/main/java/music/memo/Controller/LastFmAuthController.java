package music.memo.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/public/lastFm")
public class LastFmAuthController {

    @Value("${lastfm.api.key}")
    private String apiKey;

    @Value("${lastfm.api.secret}")
    private String apiSecret;

    @Value("${lastfm.api.callback}")
    private String callbackUrl;

    /**
     * LastFm 로그인
     */
    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        String authUrl = "http://www.last.fm/api/auth/?api_key=" + apiKey + "&cb=" + callbackUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * LastFm 토큰 받아오기
     */
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("token") String token) {
        String apiSig = generateApiSignature(token);
        String sessionUrl = "https://ws.audioscrobbler.com/2.0/?method=auth.getSession" +
                "&api_key=" + apiKey +
                "&token=" + token +
                "&api_sig=" + apiSig +
                "&format=json";

        ResponseEntity<String> response = restTemplate.getForEntity(sessionUrl, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    private String generateApiSignature(String token) {
        String data = "api_key" + apiKey + "methodauth.getSessiontoken" + token + apiSecret;
        return md5Hash(data);
    }

    // MD5 해싱 함수
    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            return String.format("%032x", no);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
