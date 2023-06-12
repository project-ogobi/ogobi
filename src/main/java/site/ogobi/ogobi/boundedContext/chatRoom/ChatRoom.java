package site.ogobi.ogobi.boundedContext.chatRoom;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import site.ogobi.ogobi.base.baseEntity.BaseEntity;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    private String name;
    private String content;
}
