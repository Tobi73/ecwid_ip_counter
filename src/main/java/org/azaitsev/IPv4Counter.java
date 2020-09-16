package org.azaitsev;

import java.util.BitSet;

/**
 * Данный класс отвечает за подсчет количества
 * уникальных IPv4-адресов (далее просто 'IP-адреса').
 */
public class IPv4Counter {
    private final BitSet lowerPresenceSet;
    private final BitSet higherPresenceSet;

    /**
     * В конструкторе происходит создание двух масок присутствия
     * с размером, достаточным для размещения всех возможных IP-адресов.
     */
    public IPv4Counter() {
        this.lowerPresenceSet = new BitSet(Integer.MAX_VALUE);
        this.higherPresenceSet = new BitSet(Integer.MAX_VALUE);
    }

    /**
     * Добавление нового IP-адреса
     * <p>
     * Принцип работы:
     * 1. Переданный IP-адрес переводится в уникальное числовое представление.
     * Подробнее в документации к методу getIPAddressAsLong.
     * 2. Полученное число используется как индекс бита в одной из масок присутствия.
     * 3. Бит, найденный по индексу, становится отмеченным (присваивается значение 1).
     * Тем самым мы 'запоминаем' пришедший IP-адрес по его числовому представлению для последующего
     * подсчета уникальных адресов в методе count.
     *
     * @param IPAddress IPv4-адрес
     */
    public void add(String IPAddress) {
        /* Индекс бита в маске присутствия */
        int presenceBitIndex;

        /* Перевод адреса в уникальное числовое представление */
        long IPAsLong = getIPAddressAsLong(IPAddress);

        /*
        К сожалению, полученный индекс
        может не поместиться в integer Java (signed integer).
        Это может привести к тому, что мы не сможем отметить
        соответствующий бит в маске присутствия т.к. индекс
        будет выходить за пределы маски присутствия (Integer.MAX_VALUE).

        Для этого используем вторую маску присутствия, в которой
        будут отмечаться присутствие тех IP-адресов, числовое
        представление которых больше Integer.MAX_VALUE.
         */
        if (IPAsLong < Integer.MAX_VALUE) {
            presenceBitIndex = (int) IPAsLong;
            lowerPresenceSet.set(presenceBitIndex);
        } else {
            presenceBitIndex = (int) (IPAsLong - Integer.MAX_VALUE);

            /*
            Обработка корнер-кейса с адресом '255.255.255.255'
            числовое представление которого выходит за пределы второй маски присутствия.
            */
            higherPresenceSet.set(presenceBitIndex == 0 ? presenceBitIndex : presenceBitIndex - 1);
        }
    }

    /**
     * Подсчет количества уникальных IP-адресов
     * путем определения количества 'отмеченных'
     * битов в обеих масках присутствия.
     *
     * @return Количество уникальных IP-адресов
     */
    public int count() {
        return this.lowerPresenceSet.cardinality() + this.higherPresenceSet.cardinality();
    }

    /**
     * Сброс состояния масок присутствия.
     */
    public void clear() {
        this.lowerPresenceSet.clear();
        this.higherPresenceSet.clear();
    }

    /**
     * Получение уникального числового представления IP-адреса:
     * <p>
     * (first octet * 256³) + (second octet * 256²) + (third octet * 256) + (fourth octet)
     * <p>
     * Пример:
     * IP-адрес - 232.100.201.90
     * <p>
     * First Octet - 232
     * Second Octet - 100
     * Third Octet - 201
     * Fourth Octet - 90
     * <p>
     * (first octet * 256³) + (second octet * 256²) + (third octet * 256) + (fourth octet)
     * = (first octet * 16777216) + (second octet * 65536) + (third octet * 256) + (fourth octet)
     * = (232 * 16777216) + (100 * 65536) + (201 * 256) + (90)
     * = 3898919258
     *
     * @param IPAddress IP-адрес
     * @return уникальное числовое представление IP-адреса
     */
    private long getIPAddressAsLong(String IPAddress) {
        long IPAddressAsLong = 0;
        String[] octets = IPAddress.split("\\.");

        for (int octetIndex = 0; octetIndex < 4; octetIndex++) {
            int intOctet = Integer.parseInt(octets[octetIndex]);

            IPAddressAsLong += (intOctet * Math.pow(256, 3 - octetIndex));
        }

        return IPAddressAsLong;
    }
}
