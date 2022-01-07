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
        final HttpHeaders headers = new HttpHeaders();
        headers.add("x-requested-with", "XMLHttpRequest");
        headers.add("Accept", "application/json");
        final HttpEntity<String> entity = new HttpEntity<>("body", headers);

        final JsonNode rootNode = restTemplate.exchange(GET_VACANCIES_URL, HttpMethod.GET, entity, JsonNode.class).getBody();
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
        return vacancy;
    }

    @Override
    public WebsiteName getName() {
        return WebsiteName.JOBSEARCH_AZ;
    }
}
