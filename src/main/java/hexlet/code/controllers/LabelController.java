package hexlet.code.controllers;

import hexlet.code.dto.LabelDto;
import hexlet.code.services.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @PutMapping("/{id}")
    public ResponseEntity<LabelDto.Response> updateById(
            @PathVariable Long id,
            @RequestBody LabelDto.Request labelRequest) {
        return ResponseEntity.ok(labelService.updateLabel(id, labelRequest));
    }

    @PostMapping
    public ResponseEntity<LabelDto.Response> save(@RequestBody LabelDto.Request labelRequest) {
        return new ResponseEntity<>(labelService.createLabel(labelRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.getLabelById(id));
    }

    @GetMapping
    public ResponseEntity<List<LabelDto.Response>> findAll() {
        List<LabelDto.Response> labels = labelService.getAllLabels();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(labels.size()));
        return new ResponseEntity<>(labels, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
