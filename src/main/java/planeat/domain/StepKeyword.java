package planeat.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StepKeyword {

    @Enumerated(EnumType.STRING)
    private StepType stepType;

    private String keyword1;
    private String keyword2;
    private String keyword3;

    public List<String> getKeywords() {
        List<String> list = new ArrayList<>();
        if (keyword1 != null && !keyword1.isBlank()) list.add(keyword1);
        if (keyword2 != null && !keyword2.isBlank()) list.add(keyword2);
        if (keyword3 != null && !keyword3.isBlank()) list.add(keyword3);
        return list;
    }
}
