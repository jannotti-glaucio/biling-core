package tech.jannotti.billing.core.commons.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    private Map<String, SecureRandom> randomMap = new HashMap<String, SecureRandom>();

    private final ReentrantLock lock = new ReentrantLock();

    public String generateRandomHexToken(String type, int size) {
        SecureRandom random = getRandom(type);

        byte[] binaryToken = new byte[size / 2];
        random.nextBytes(binaryToken);

        String hexToken = Hex.encodeHexString(binaryToken);
        return hexToken;
    }

    public String generateRandomBase64Token(String type, int size) {
        SecureRandom random = getRandom(type);

        // Calcula o tamanho necessario do array de bytes para gerar um token no tamanho esperado
        int binarySize = 2 * (int) Math.ceil(size / 3.0);

        byte[] binaryToken = new byte[binarySize];
        random.nextBytes(binaryToken);

        String hexToken = Base64.encodeBase64String(binaryToken);
        return hexToken;
    }

    private SecureRandom getRandom(String type) {
        SecureRandom random = randomMap.get(type);

        if (random == null) {
            try {
                lock.lock();

                random = randomMap.get(type);
                if (random != null) {
                    return random;
                }

                try {
                    random = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException e) {
                    throw new SecurityException("Erro criando novo secureRandom", e);
                }

                random.nextBytes(new byte[20]);
                randomMap.put(type, random);

            } finally {
                lock.unlock();
            }
        }
        return random;
    }

}