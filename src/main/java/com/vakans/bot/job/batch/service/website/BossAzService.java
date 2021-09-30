package com.vakans.bot.job.batch.service.website;

import com.vakans.bot.job.batch.dao.LastVacancyDao;
import com.vakans.bot.job.batch.dao.LastVacancyDaoImpl;
import com.vakans.bot.job.batch.data.LastVacancy;
import com.vakans.bot.job.batch.data.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BossAzService implements WebsiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BossAzService.class);

    private LastVacancyDao lastVacancyDao;

    private final static String WEBSITE_URL = "https://boss.az";
    private final static String VACANCIES_URL = "https://boss.az/vacancies";
    private final static String COMPANY_ID = ".results-i-company";
    private final static String SALARY_ID = ".results-i-salary";
    private final static String TITLE_ID = ".results-i-title";
    private final static String DESCRIPTION_ID = ".results-i-summary";
    private final static String ELEMENTS_ID = ".results-i";
    private final static String LINK_ID = ".results-i-link";

    public BossAzService(final LastVacancyDao lastVacancyDao){
        this.lastVacancyDao = lastVacancyDao;
    }

    @Override
    public List<Vacancy> getNewVacancies() {
        final List<Vacancy> vacancies = new ArrayList<>();
        try {
            final Document doc = Jsoup.connect(VACANCIES_URL).get();
            final Elements elements = doc.select(ELEMENTS_ID);
            final LastVacancy lastVacancy = lastVacancyDao.getLastVacancyByWebsite(WEBSITE_URL);
            for (Element element : elements) {
                final Vacancy vacancy = elementToVacancy(element);
                if(lastVacancy.getLink().equals(vacancy.getVacancyLink())){
                    LOGGER.info("Last vacancy: {}", lastVacancy);
                    if(vacancies.size() == 0){
                        LOGGER.info("There was no new vacancy!");
                    }else{
                        final String lastVacancyLink = vacancies.get(0).getVacancyLink();
                        LOGGER.info("Updating last vacancy: {}", lastVacancyLink);
                        lastVacancyDao.updateLastVacancyByWebsite(WEBSITE_URL, lastVacancyLink);
                    }
                    break;
                }
                vacancies.add(vacancy);
                LOGGER.info("Added vacancy to vacancies list: {}", vacancy);
            }
        } catch (final IOException exception) {
            LOGGER.error(exception.getLocalizedMessage());
        }
        return vacancies;
    }


    private static Vacancy elementToVacancy(final Element element){
        final Vacancy vacancy = new Vacancy();
        vacancy.setTitle(element.select(TITLE_ID).text());
        vacancy.setDescription(element.select(DESCRIPTION_ID).text());
        vacancy.setCompany(element.select(COMPANY_ID).text());
        vacancy.setVacancyLink(WEBSITE_URL + element.select(LINK_ID).attr("href"));

        final String salaryAsString =  element.select(SALARY_ID).text();
        if(salaryAsString.matches(".*\\d+.*")){
            final String[] salaryArray = element.select(SALARY_ID).text().split("-");
            if(salaryArray.length == 1){
                final int salary = Integer.parseInt(salaryArray[0].trim().split(" ")[0].trim());
                vacancy.setMinimumSalary(salary);
                vacancy.setMaximumSalary(salary);
            }else{
                vacancy.setMinimumSalary(Integer.parseInt(salaryArray[0].trim()));
                vacancy.setMaximumSalary(Integer.parseInt(salaryArray[1].trim().split(" ")[0].trim()));
            }
        }
        return vacancy;
    }


}
