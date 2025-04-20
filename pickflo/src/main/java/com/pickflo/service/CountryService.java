package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Country;
import com.pickflo.dto.CountryDto;
import com.pickflo.repository.CountryRepository;
import com.pickflo.repository.MovieClient;
import com.pickflo.repository.MovieClient.CountryData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CountryService {

    private final MovieClient movieClient;
    private final CountryRepository countryRepo;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.language}")
    private String language;

    @Transactional
    public void getCountryId() {
        // 전체 국가 정보를 한 번에 가져옵니다.
        List<CountryData> countryDataList = movieClient.getCountry(apiKey, language);

        // 데이터베이스에 저장
        countryDataList.forEach(countryData -> {
            String countryCode = countryData.getIso_3166_1();
            String countryName = countryData.getNative_name();

            if (!countryRepo.existsByCountryCode(countryCode)) {
                Country country = Country.builder()
                        .countryCode(countryCode)
                        .countryName(countryName)
                        .build();

                countryRepo.save(country);
                log.info("Saved country: code={}, name={}", countryCode, countryName);
            } else {
                log.info("Country with code {} already exists in the database.", countryCode);
            }
        });
    }
}
