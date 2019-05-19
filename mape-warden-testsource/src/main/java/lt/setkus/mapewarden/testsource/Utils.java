package lt.setkus.mapewarden.testsource;

import java.io.IOException;
import java.util.Random;

public final class Utils {
    private Utils() {

    }

    public static int[] getIntegerInput() throws IOException {
        int[] stream = {1, 2, 3};
        Random random = new Random(System.currentTimeMillis());
        if (random.nextInt() % 2 == 0) {
            throw new IOException("Random error");
        }
        return stream;
    }
}
