package com.vakans.bot.job.service;

import com.vakans.bot.job.data.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class BossAzService implements WebsiteService{


    private final static String WEBSITE_URL = "https://boss.az/vacancies";
    private final static String COMPANY_ID = ".results-i-company";
    private final static String SALARY_ID = ".results-i-salary";
    private final static String TITLE_ID = ".results-i-title";
    private final static String DESCRIPTION_ID = ".results-i-summary";
    private final static String ELEMENTS_ID = ".results-i";

    @Override
    public List<Vacancy> getNewVacancies() {
        System.out.println("boss.az vacancies");
        return null;
    }



    public List<Vacancy> scrapeWebsite() throws IOException {
        Document doc = Jsoup.connect(WEBSITE_URL).get();

        System.out.println(doc);
        return null;

    }

    public static void main(String[] args) throws IOException {
        final Document doc = Jsoup.connect(WEBSITE_URL).get();
        final Elements newsHeadlines = doc.select(ELEMENTS_ID);

        for (Element element : newsHeadlines) {
            System.out.println(element.select(SALARY_ID).text());
            System.out.println("beydu1");
            System.out.println(element);

            System.out.println(elementToVacancy(element));
            return;
        }
    }

    private static Vacancy elementToVacancy(final Element element){
        final Vacancy vacancy = new Vacancy();
        vacancy.setTitle(element.select(TITLE_ID).text());
        vacancy.setDescription(element.select(DESCRIPTION_ID).text());
        vacancy.setCompany(element.select(COMPANY_ID).text());
        final String[] salaryArray = element.select(SALARY_ID).text().split("-");
        vacancy.setMinimumSalary(Integer.parseInt(salaryArray[0].trim()));
        vacancy.setMaximumSalary(Integer.parseInt(salaryArray[1].trim().split(" ")[0].trim()));
        return vacancy;
    }


}
