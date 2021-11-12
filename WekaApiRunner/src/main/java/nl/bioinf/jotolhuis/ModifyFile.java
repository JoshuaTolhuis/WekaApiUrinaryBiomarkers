package nl.bioinf.jotolhuis;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Class to alter the given script by a user using r
 *
 * @author Joshua Tolhuis
 */

public class ModifyFile {
    protected void modifyData(String unknownFile) throws IOException, InterruptedException {
        System.out.println(unknownFile);
        ProcessBuilder pb = new ProcessBuilder("Rscript", "R_script.R", unknownFile);
        Process p = pb.start();

        //process needs some time to read, sleep to prevent error
        TimeUnit.SECONDS.sleep(5);

        System.out.println("DONE");
    }
}
