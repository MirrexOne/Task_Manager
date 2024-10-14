package hexlet.code.services.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.entities.Label;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mappers.LabelMapper;
import hexlet.code.repositories.LabelRepository;
import hexlet.code.services.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    private final LabelMapper labelMapper;

    @Override
    public LabelDto.Response createLabel(LabelDto.Request labelDto) {
        Label label = labelMapper.map(labelDto);
        return labelMapper.map(labelRepository.save(label));
    }

    @Override
    public LabelDto.Response getLabelById(Long id) {
        return labelMapper.map(findLabelById(id));
    }

    @Override
    public List<LabelDto.Response> getAllLabels() {
        return labelRepository.findAll().stream()
                .map(labelMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDto.Response updateLabel(Long id, LabelDto.Request labelDto) {
        Label label = findLabelById(id);
        labelMapper.update(labelDto, label);
        return labelMapper.map(labelRepository.save(label));
    }

    @Override
    public void deleteLabel(Long id) {
        Label label = findLabelById(id);
        if (!label.getTasks().isEmpty()) {
            throw new IllegalStateException("Невозможно удалить метку, связанную с задачами");
        }
        labelRepository.deleteById(id);
    }

    private Label findLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Метка не найдена"));
    }
}
