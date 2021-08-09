import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Sandeep
 */
public class TestDataGenerator {

    /**
     * Generates test 100 records for 20 unique credit cards.
     * Copy the output and save as a file to use as program input
     * @param args - command line args
     */
    public static void main(String[] args) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("transactions"));

        int totalCreditCards = 2000;
        int totalTransctions = 200;
        List<String> ccNumbers = new ArrayList<>();
        for (int i = 0; i < totalCreditCards; i++) {
            ccNumbers.add(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        LocalDateTime startDate = LocalDateTime.parse("2021-08-01T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        for (int i = 0; i < totalTransctions; i++) {
            String ccNumber = ccNumbers.get(new Random().nextInt(totalCreditCards));
            startDate = nextRandomChronologicalDate(startDate);
            BigDecimal amount = BigDecimal.valueOf(
                    ThreadLocalRandom.current().nextDouble(5, 130))
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN);
            String[] item = {ccNumber, String.valueOf(startDate), String.valueOf(amount)};
            writer.writeNext(item, false);
        }

        writer.close();
    }

    /**
     *
     * @param previousDate - Previous date to maintain chronological order
     * @return new random date in future
     */
    private static LocalDateTime nextRandomChronologicalDate(LocalDateTime previousDate) {
        previousDate = previousDate
                .plusHours(new Random().nextInt(5))
                .plusMinutes(new Random().nextInt(60))
                .plusSeconds(new Random().nextInt(60));
        return previousDate;
    }

}
