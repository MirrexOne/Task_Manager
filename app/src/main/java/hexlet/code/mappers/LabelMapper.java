package hexlet.code.mappers;

import hexlet.code.dto.LabelDto;
import hexlet.code.entities.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LabelMapper {

    Label map(LabelDto.Request dto);

    LabelDto.Response map(Label entity);

    void update(LabelDto.Request dto, @MappingTarget Label entity);
}
