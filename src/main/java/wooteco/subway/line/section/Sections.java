package wooteco.subway.line.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Sections {
    private final List<Section> sectionGroup;

    public Sections(final List<Section> sectionGroup) {
        this.sectionGroup = new ArrayList<>(sectionGroup);
        sort();
    }

    public List<Long> distinctStationIds() {
        final Set<Long> ids = new HashSet<>();
        for (Section section : sectionGroup) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return new ArrayList<>(ids);
    }

    private void sort() {
        if (sectionGroup.size() < 2) {
            return;
        }

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
    }

    public List<Section> toList() {
        return sectionGroup;
    }
}
