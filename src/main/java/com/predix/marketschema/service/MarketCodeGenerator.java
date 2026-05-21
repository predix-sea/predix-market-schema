package com.predix.marketschema.service;

import com.predix.marketschema.repository.MarketCodeSequenceRepository;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MarketCodeGenerator {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final MarketCodeSequenceRepository sequenceRepository;

    @Value("${predix.market-code-prefix:PMKT}")
    private String prefix;

    public MarketCodeGenerator(MarketCodeSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    public String generate() {
        String date = LocalDate.now(ZoneOffset.UTC).format(DATE_FMT);
        long seq = sequenceRepository.nextVal();
        return String.format("%s_%s_%04d", prefix, date, seq);
    }
}
