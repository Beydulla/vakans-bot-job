package com.vakans.bot.job.batch.service.website;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.vakans.bot.job.batch.dao.LastVacancyDao;
import com.vakans.bot.job.batch.data.JobSearchDTO;
import com.vakans.bot.job.batch.data.LastVacancy;
import com.vakans.bot.job.batch.data.Vacancy;
import com.vakans.bot.job.batch.data.constants.WebsiteName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobSearchService implements WebsiteService{

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSearchService.class);

    @Autowired
    private LastVacancyDao lastVacancyDao;
    @Autowired
    private RestTemplate restTemplate;

    private final static String WEBSITE_URL = "https://jobsearch.az";
    private final static String VACANCIES_URL = "https://jobsearch.az/vacancies";
    private final static String GET_VACANCIES_URL = "https://jobsearch.az/api-az/vacancies-az?hl=az&q=&posted_date=&seniority=&categories=&industries=&order_by=";
    private final static String VACANCIES_API_URL = "https://jobsearch.az/api-az/vacancies-az";

    @Override
    public List<Vacancy> getNewVacancies() {
        final List<Vacancy> vacancies = new ArrayList<>();
        final List<JobSearchDTO> jobSearchDTOList = getDTOArrayFromJobSearch();
        final LastVacancy lastVacancy = lastVacancyDao.getLastVacancyByWebsite(WEBSITE_URL);
        for (final JobSearchDTO jobSearchDTO : jobSearchDTOList) {
            final Vacancy vacancy = toVacancy(jobSearchDTO);
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
        return vacancies;

    }

    private List<JobSearchDTO> getDTOArrayFromJobSearch() {
        final JsonNode rootNode = sendGetRequest(GET_VACANCIES_URL);
        final JsonNode itemsNode = rootNode.get("items");
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectReader reader = mapper.readerFor(new TypeReference<List<JobSearchDTO>>() {});
        final List<JobSearchDTO> list = new ArrayList<>();
        try {
            list.addAll(reader.readValue(itemsNode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Vacancy toVacancy(final JobSearchDTO jobSearchDTO){
        final Vacancy vacancy = new Vacancy();
        vacancy.setTitle(jobSearchDTO.getTitle());
        vacancy.setVacancyLink(VACANCIES_URL + "/" + jobSearchDTO.getSlug());
        vacancy.setCompany(jobSearchDTO.getCompany().get("title"));
        pause(15_000);
        vacancy.setDescription(getJobDescription(jobSearchDTO));
        return vacancy;
    }

    private String getJobDescription(final JobSearchDTO jobSearchDTO){
        final JsonNode rootNode = sendGetRequest(VACANCIES_API_URL + "/" + jobSearchDTO.getSlug());
        return rootNode.get("text").textValue();
    }

    private JsonNode sendGetRequest(final String url){
        final HttpHeaders headers = new HttpHeaders();
        headers.add("x-requested-with", "XMLHttpRequest");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
        final HttpEntity<String> entity = new HttpEntity<>("body", headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    public void pause(int ms){
        try {
            LOGGER.info("paused Thread {} milliseconds", ms);
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WebsiteName getName() {
        return WebsiteName.JOBSEARCH_AZ;
    }
}
