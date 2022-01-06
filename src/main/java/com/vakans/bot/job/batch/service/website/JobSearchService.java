package com.vakans.bot.job.batch.service.website;

import com.vakans.bot.job.batch.dao.LastVacancyDao;
import com.vakans.bot.job.batch.data.LastVacancy;
import com.vakans.bot.job.batch.data.Vacancy;
import com.vakans.bot.job.batch.data.constants.WebsiteName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobSearchService implements WebsiteService{

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSearchService.class);

    private LastVacancyDao lastVacancyDao;

    private final static String WEBSITE_URL = "https://www.jobsearch.az/";
    private final static String VACANCIES_URL = "https://jobsearch.az/vacancies";
    private final static String ELEMENTS_ID = ".list__scroller";
    private final static String TITLE_ID = "td:nth-child(1) > a.hotv_text";
    private final static String COMPANY_ID = "td:nth-child(2)";
    private final static String SALARY_ID = ".results-i-salary";
    private final static String DESCRIPTION_ID = ".results-i-summary";
    private final static String LINK_ID = ".results-i-link";

    public JobSearchService(final LastVacancyDao lastVacancyDao){
        this.lastVacancyDao = lastVacancyDao;
    }

    @Override
    public List<Vacancy> getNewVacancies() {
        final List<Vacancy> vacancies = new ArrayList<>();
        try {
            final Document doc = Jsoup.connect(VACANCIES_URL).get();
            System.out.println(doc);
            final Elements elements = doc.select(ELEMENTS_ID);
            elements.remove(0);
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
        vacancy.setCompany(element.select(COMPANY_ID).text());
        vacancy.setVacancyLink(WEBSITE_URL + element.select(TITLE_ID).attr("href"));
        return vacancy;
    }

    @Override
    public WebsiteName getName() {
        return WebsiteName.JOBSEARCH_AZ;
    }
}
