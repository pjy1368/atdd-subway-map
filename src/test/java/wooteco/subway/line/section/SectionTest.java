package wooteco.subway.line.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class SectionTest {

    @Test
    void sort() {
        // 기댓값은 3 2 1 4
        final List<Section> sectionGroup  = new ArrayList<>();
        sectionGroup.add(new Section(1L, 3L, 2L, 10));
        sectionGroup.add(new Section(1L, 4L, 5L, 5));
        sectionGroup.add(new Section(1L, 2L, 1L, 6));
        sectionGroup.add(new Section(1L, 1L, 4L, 8));

        final Map<Long, Integer> map = new HashMap<>();
        for (final Section section : sectionGroup) {
            final long upStationId = section.getUpStationId();
            final long downStationId = section.getDownStationId();
            map.merge(upStationId, 1, (key, oldValue) -> oldValue + 1);
            map.merge(downStationId, 1, (key, oldValue) -> oldValue + 1);
        }

        final List<Long> candidates = new ArrayList();
        map.forEach((id, value) -> {
            if (value == 1) {
                candidates.add(id);
            }
        });

        Section from = sectionGroup.stream()
            .filter(section -> candidates.contains(section.getUpStationId()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 상행역이 없습니다."));

        final List<Section> result = new ArrayList<>();
        result.add(from);

        while(true) {
            long downStationId = from.getDownStationId();
            Optional<Section> nextSection = sectionGroup.stream()
                .filter(section -> section.getUpStationId() == downStationId)
                .findAny();

            if (!nextSection.isPresent()) {
                break;
            }
            from = nextSection.get();
            result.add(from);
        }
        result.forEach(section -> System.out.println(section.getUpStationId() + "-" + section.getDownStationId()));
    }
}
