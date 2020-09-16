package org.azaitsev;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;


public class IPv4CounterTest {

    @Test
    public void shouldCountDistinctIPs() {
        /* Setup */
        IPv4Counter counter = new IPv4Counter();
        List<String> ips = generateRandomIPs(10_000);

        /* Act */
        for (String ip : ips) {
            counter.add(ip);
        }

        /* Assert */
        assertTrue(counter.count() == new HashSet<>(ips).size());
    }

    @Test
    public void shouldCountDistinctCornerCaseIPs() {
        /* Setup */
        IPv4Counter counter = new IPv4Counter();
        List<String> ips = new ArrayList<>();
        ips.add("255.255.255.255");
        ips.add("255.255.255.254");
        ips.add("255.0.0.0");
        ips.add("0.0.0.0");
        ips.add("1.1.1.1");

        /* Act */
        for (String ip : ips) {
            counter.add(ip);
        }

        /* Assert */
        assertTrue(counter.count() == new HashSet<>(ips).size());
    }

    private List<String> generateRandomIPs(int numOfIPs) {
        List<String> ips = new ArrayList<>();

        Random r = new Random();
        for (int i = 0; i < numOfIPs; i++) {
            /* Понимаю, что выглядит ужасно */
            ips.add(r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256));
        }

        return ips;
    }
}
