package hexlet.code.services;

import hexlet.code.dto.LabelDto;
import java.util.List;

public interface LabelService {

    LabelDto.Response createLabel(LabelDto.Request labelDto);

    LabelDto.Response getLabelById(Long id);

    List<LabelDto.Response> getAllLabels();

    LabelDto.Response updateLabel(Long id, LabelDto.Request labelDto);

    void deleteLabel(Long id);
}
