package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.service.BigQueryService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bigquery/")
@AllArgsConstructor
public class BigQueryController {
    private final BigQueryService bigQueryService;

    @GetMapping
    public Map<String, Map<String, List<String>>> getFields() throws ChangeSetPersister.NotFoundException {
        return bigQueryService.getAllFactsSelectableFields();
    }


    @GetMapping("{factName}/download/")
    public ResponseEntity<InputStreamResource> downloadCSV(
            @PathVariable (name = "factName") String factName,
            @RequestParam (name = "fields") List<String> fields,
            @RequestParam (name = "limit", defaultValue = "100") Integer limit,
            @RequestParam (name = "startDate", defaultValue = "2024-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam (name = "endDate", defaultValue = "2024-05-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam (name = "writeHeader", defaultValue = "true") Boolean writeHeader
            ) throws ChangeSetPersister.NotFoundException, IOException, InterruptedException {
        ByteArrayInputStream in = bigQueryService.downloadCSV(factName, fields, limit, startDate, endDate, writeHeader);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=result.csv");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
    @GetMapping("{factName}/")
    public Map<String, List<String>> getFields(@PathVariable(name = "factName") String factName) throws ChangeSetPersister.NotFoundException {
        return bigQueryService.getAllSelectableFields(factName);
    }
}
