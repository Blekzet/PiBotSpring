package ru.blekzet.pibot.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JoyreactorParser {
    private final Random random;
    private final String joyreactorUrl;

    public JoyreactorParser(String joyreactorUrl){
        this.joyreactorUrl = joyreactorUrl;
        random = new Random();
    }

    private Document seizeRandomPage(){
        Document joyreactorHtmlDoc = null;

        try {
            int totalPages;
            joyreactorHtmlDoc = Jsoup.connect(joyreactorUrl).get();
            Elements pagesTags = joyreactorHtmlDoc.getElementsByClass("current");
            if(pagesTags.get(0).text().equals("Назад")) {
                totalPages = Integer.parseInt(pagesTags.get(1).text());
            } else {
                totalPages = Integer.parseInt(pagesTags.get(0).text());
            }
            joyreactorHtmlDoc = Jsoup.connect(joyreactorUrl + "/" + random.nextInt(totalPages)).get();
        } catch (IOException|NullPointerException e) {
            e.printStackTrace();
        }

        return joyreactorHtmlDoc;
    }

    public List<String> getRandomPictures(){
        Elements postList = seizeRandomPage().getElementsByClass("postContainer");
        List<List<String>> pictureList = new ArrayList<>(10);

        int listIndex = 0;for (Element element: postList.select(".post_content")){
            pictureList.add(new ArrayList<>(1));
            for(Element child: element.select("img")) {
                pictureList.get(listIndex).add(child.attr("src"));
            }
            listIndex++;
        }

        return pictureList.get(random.nextInt(pictureList.size()-1));
    }
}
