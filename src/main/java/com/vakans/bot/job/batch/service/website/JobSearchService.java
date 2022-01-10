package com.vakans.bot.job.batch.service.website;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.vakans.bot.job.batch.dao.GeneralDao;
import com.vakans.bot.job.batch.data.JobSearchDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobSearchService implements WebsiteService{

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSearchService.class);

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private RestTemplate restTemplate;

    private final static String VACANCIES_URL = "https://jobsearch.az/vacancies";
    private final static String GET_VACANCIES_URL = "https://jobsearch.az/api-az/vacancies-az?hl=az&q=&posted_date=&seniority=&categories=&industries=&order_by=";
    private final static String VACANCIES_API_URL = "https://jobsearch.az/api-az/vacancies-az";

    @Override
    public List<Vacancy> getNewVacancies() {
        final List<Vacancy> vacancies = new ArrayList<>();
        final List<JobSearchDTO> jobSearchDTOList = getNewDTOArrayFromJobSearch();
        for (final JobSearchDTO jobSearchDTO : jobSearchDTOList) {
            final Vacancy vacancy = toVacancy(jobSearchDTO);
            vacancies.add(vacancy);
            generalDao.insertNewVacancyLink(vacancy.getVacancyLink(), this.getName());
            LOGGER.info("Added vacancy to vacancies list: {}", vacancy);
        }
        return vacancies;

    }

    private void deleteDuplicateVacancies(final List<JobSearchDTO> jobSearchDTOList){
        LOGGER.info("Deleting duplicate vacancies. Vacancy count before deletion: {}", jobSearchDTOList.size());
        final List<String> vacancyLinkList = generalDao.getLatestVacanciesByWebsite(this.getName());
        jobSearchDTOList.removeIf(jobSearchDTO -> vacancyLinkList.contains(getVacancyLink(jobSearchDTO)));
        jobSearchDTOList.removeIf(dto -> dto.getCreatedAt().substring(0, 10).equals(LocalDate.now().minusDays(1).toString()));
        LOGGER.info("Deleted duplicate vacancies. Vacancy count after deletion: {}", jobSearchDTOList.size());
    }

    private List<JobSearchDTO> getNewDTOArrayFromJobSearch() {
        final JsonNode rootNode = sendGetRequest(GET_VACANCIES_URL);
        LOGGER.info("Data fetched successfully.");
        final JsonNode itemsNode = rootNode.get("items");
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectReader reader = mapper.readerFor(new TypeReference<List<JobSearchDTO>>() {});
        final List<JobSearchDTO> jobSearchDTOList = new ArrayList<>();
        try {
            jobSearchDTOList.addAll(reader.readValue(itemsNode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteDuplicateVacancies(jobSearchDTOList);
        return jobSearchDTOList;
    }

    private Vacancy toVacancy(final JobSearchDTO jobSearchDTO){
        final Vacancy vacancy = new Vacancy();
        vacancy.setTitle(jobSearchDTO.getTitle());
        vacancy.setVacancyLink(getVacancyLink(jobSearchDTO));
        vacancy.setCompany(jobSearchDTO.getCompany().get("title"));
        pause(15_000);
        vacancy.setDescription(getJobDescription(jobSearchDTO));
        return vacancy;
    }

    private String getVacancyLink(final JobSearchDTO jobSearchDTO){
        return VACANCIES_URL + "/" + jobSearchDTO.getSlug();
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
