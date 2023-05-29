package site.ogobi.ogobi.boundedContext.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto { // 필수 필드에 대해 유효성 검사를 하기 위한 데이터 전달 객체
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max=20)
    private String subject;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
