package my.project.user_note.mapper.client_user_note;

import my.project.user_note.entity.client_user_note.ClientUserNote;
import my.project.user_note.request.client_user_note.CreateClientUserNoteRequestV2;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientUserNoteMapper {
    ClientUserNote toEntity(CreateClientUserNoteRequestV2 request);
}
