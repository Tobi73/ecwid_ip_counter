package org.azaitsev;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Примечание от автора:
 *
 * На одном из этапов выполнения задания были предприняты
 * попытки реализовать решение данной задачи с помощью самописного HyperLogLog.
 * К сожалению попытка не увенчалась успехом т.к. после долгих мучений
 * так и не удалось снизить ошибку ниже 4%, что в свою очередь
 * кинуло меня в пучину отчаяния.
 *
 */
public class App {
    /*
    Для запуска через IDE
    стоит прописать путь до файлика с IP-шниками
    в DEFAULT_DEMO_FILE_PATH
     */
    private static final String DEFAULT_DEMO_FILE_PATH = "ip_addresses";

    public static void main(String[] args) {
        String path = args.length >= 1 ? args[0] : DEFAULT_DEMO_FILE_PATH;

        long start = System.currentTimeMillis();

        IPv4Counter counter = new IPv4Counter();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            String ipAddress;
            while ((ipAddress = reader.readLine()) != null) {
                counter.add(ipAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Number of distinct IP addresses: " + counter.count());
        System.out.println("Time spent: " + (System.currentTimeMillis() - start));
    }
}
