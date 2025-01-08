package kelompok4.praktikumpemrograman2.services;


import kelompok4.praktikumpemrograman2.model.TotalSampah;
import kelompok4.praktikumpemrograman2.model.TotalSampahMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TotalSampahService {
    private static final Logger log = LoggerFactory.getLogger(TotalSampahService.class);
    private final TotalSampahMapper mapper;
    private final SqlSession sqlSession;

    public TotalSampahService(SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(TotalSampahMapper.class);
        this.sqlSession = sqlSession;
        log.info("TotalSampahService initialized with SQL session");
    }

    private List<TotalSampah> executeQuery(String operation, QueryExecutor queryExecutor) {
        log.info("=== TotalSampahService.{}() START ===", operation);
        try {
            List<TotalSampah> result = queryExecutor.execute();
            validateResults(result);
            logResults(operation, result);
            return result;
        } catch (Exception e) {
            log.error("Error in {}: {}", operation, e.getMessage(), e);
            return Collections.emptyList();
        } finally {
            log.info("=== TotalSampahService.{}() END ===", operation);
        }
    }

    public List<TotalSampah> getAll() {
        return executeQuery("getAll", mapper::getAll);
    }

    public List<TotalSampah> filterByDay(String date) {
        if (!isValidDate(date)) {
            log.error("Invalid date format: {}", date);
            return Collections.emptyList();
        }
        return executeQuery("filterByDay", () -> mapper.getTotalSampahByTanggal(date));
    }

    public List<TotalSampah> filterByWeek(String startDate, String endDate) {
        if (!isValidDateRange(startDate, endDate)) {
            log.error("Invalid date range: {} to {}", startDate, endDate);
            return Collections.emptyList();
        }
        return executeQuery("filterByWeek", () -> mapper.getTotalSampahByWeek(startDate, endDate));
    }

    public List<TotalSampah> filterByMonth(String startDate, String endDate) {
        if (!isValidDateRange(startDate, endDate)) {
            log.error("Invalid date range: {} to {}", startDate, endDate);
            return Collections.emptyList();
        }
        return executeQuery("filterByMonth", () -> mapper.getTotalSampahByMonth(startDate, endDate));
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidDateRange(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return !start.isAfter(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void validateResults(List<TotalSampah> results) {
        results.forEach(sampah -> {
            if (sampah.getBeratKg() == null) {
                log.warn("Warning: beratKg is null for sampah id: {}", sampah.getId());
            }
            if (sampah.getJenisSampah() == null) {
                log.warn("Warning: jenisSampah is null for sampah id: {}", sampah.getId());
            }
            if (sampah.getNamaJenis() == null) {
                log.warn("Warning: namaJenis is null for sampah id: {}, jenis_sampah: {}",
                        sampah.getId(), sampah.getJenisSampah());
            }
        });
    }

    private void logResults(String operation, List<TotalSampah> results) {
        log.info("Retrieved {} records from {}", results.size(), operation);
        results.forEach(item -> log.debug("Record: {}", item));
    }

    @FunctionalInterface
    private interface QueryExecutor {
        List<TotalSampah> execute();
    }
}