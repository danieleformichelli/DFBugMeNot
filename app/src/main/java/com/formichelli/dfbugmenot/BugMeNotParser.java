package com.formichelli.dfbugmenot;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sends requests to bugmenot.com and parse the reply
 */
public class BugMeNotParser {
    public String htmlError;

    public static List<BugMeNotResult> query(String url) throws IOException {
        if (url == null)
            throw new IllegalArgumentException("url cannot be null");
        if (!url.contains("."))
            throw new IllegalArgumentException("url must contain at least one dot");

        List<BugMeNotResult> list = new ArrayList<>();

        Connection connection = Jsoup.connect("http://bugmenot.com/view/" + url);
        Document document = connection.get();
        Elements articles = document.select("article.account");
        if (articles.size() == 0) {
            if (document.select("p").first().html().startsWith("This site"))
                return null; // barred site
            else
                return list; // no logins
        }

        for (Element article : articles) {
            String username, password;
            Elements usernameAndPassword = article.select("kbd");
            username = usernameAndPassword.get(0).html();
            password = usernameAndPassword.get(1).html();

            double successRate;
            int votes, eta;
            BugMeNotResult.EtaWeight etaWeight;
            Elements stats = article.select("li");
            successRate = Double.parseDouble(stats.get(0).html().split("%")[0]);
            votes = Integer.parseInt(stats.get(1).html().split(" ")[0]);
            String[] etaStringSplit = stats.get(2).html().split(" ");
            eta = Integer.parseInt(etaStringSplit[0]);
            String etaString = etaStringSplit[1];
            switch (etaString) {
                case "years":
                case "year":
                    etaWeight = BugMeNotResult.EtaWeight.YEARS;
                    break;

                case "months":
                case "month":
                    etaWeight = BugMeNotResult.EtaWeight.MONTHS;
                    break;

                case "days":
                case "day":
                    etaWeight = BugMeNotResult.EtaWeight.DAYS;
                    break;

                case "hours":
                case "hour":
                    etaWeight = BugMeNotResult.EtaWeight.HOURS;
                    break;

                default:
                    throw new AssertionError("Unknown etaWeight: " + etaString);
            }

            list.add(new BugMeNotResult(username, password, successRate, votes, eta, etaWeight));
        }

        return list;
    }
}
