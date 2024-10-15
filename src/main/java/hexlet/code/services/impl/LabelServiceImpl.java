package hexlet.code.services.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.entities.Label;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mappers.LabelMapper;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.services.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    private final LabelMapper labelMapper;

    @Override
    public LabelDto.Response createLabel(LabelDto.Request labelDto) {
        Label entity = labelMapper.toEntity(labelDto);
        Label saved = labelRepository.save(entity);
        return labelMapper.toLabelResponse(saved);
    }

    @Override
    public LabelDto.Response getLabelById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        return labelMapper.toLabelResponse(label);
    }

    @Override
    public List<LabelDto.Response> getAllLabels() {
        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toLabelResponse)
                .toList();
    }

    @Override
    public LabelDto.Response updateLabel(Long id, LabelDto.Request labelDto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        Label updated = labelMapper.partialUpdate(labelDto, label);
        Label saved = labelRepository.save(updated);
        return labelMapper.toLabelResponse(saved);
    }

    @Override
    public void deleteLabel(Long id) {
        labelRepository.deleteById(id);
    }
}
