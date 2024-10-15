package hexlet.code.mappers;

import hexlet.code.dto.LabelDto;
import hexlet.code.entities.Label;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LabelMapper {
    LabelDto.Response toLabelResponse(Label label);

    Label toEntity(LabelDto.Request labelRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Label partialUpdate(LabelDto.Request labelRequest, @MappingTarget Label label);
}
