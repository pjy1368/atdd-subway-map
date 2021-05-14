package wooteco.subway.line;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import wooteco.subway.line.exception.SectionSizeException;
import wooteco.subway.line.section.Section;
import wooteco.subway.line.section.Sections;
import wooteco.subway.station.Station;
import wooteco.subway.station.Stations;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Sections sections;
    private final Stations stations;

    public Line(String name, String color) {
        this(null, name, color, Collections.emptyList(), Collections.emptyMap());
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Collections.emptyList(), Collections.emptyMap());
    }

    public Line(Long id, String name, String color, List<Section> sectionGroup, Map<Long, Station> stationGroup) {
        this(id, name, color, new Sections(sectionGroup), new Stations(stationGroup));
    }

    public Line(final Long id, final String name, final String color, final Sections sections,
        final Stations stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.stations = stations;
    }

    public void validateStationsToAddSection(final Long upStationId, final Long downStationId) {
        sections.validateBothExistentStation(upStationId, downStationId);
        sections.validateNoneExistentStation(upStationId, downStationId);
    }

    public boolean isAddableTerminalStation(final Long upStationId, final Long downStationId) {
        return isStartStation(downStationId) || isLastStation(upStationId);
    }

    public boolean isTerminalStation(final Long stationId) {
        return isStartStation(stationId) || isLastStation(stationId);
    }

    private boolean isStartStation(final Long downStationId) {
        return sections.isFirstStationId(downStationId);
    }

    private boolean isLastStation(final Long upStationId) {
        return sections.isLastStationId(upStationId);
    }

    public Section findUpdatedTarget(final Long upStationId, final Long downStationId, final int distance) {
        final Section target = sections.findSameForm(upStationId, downStationId);
        target.validateSmaller(distance);
        return target;
    }

    public Section findSectionHasUpStation(long existentStationId) {
        return sections.findSectionHasUpStation(existentStationId);
    }

    public Section findSectionHasDownStation(long existentStationId) {
        return sections.findSectionHasDownStation(existentStationId);
    }

    public void validateSizeToDeleteSection() {
        if (sections.size() <= 1) {
            throw new SectionSizeException("해당 구간을 지울 수 없습니다.");
        }
    }

    public Section findTerminalSection(Long stationId) {
        if (isStartStation(stationId)) {
            return sections.getFirstSection();
        }
        if (isLastStation(stationId)) {
            return sections.getLastSection();
        }
        throw new NoSuchElementException("종점이 아닙니다.");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return sections;
    }

    public Stations getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects
            .equals(color, line.color) && Objects.equals(sections, line.sections) && Objects
            .equals(stations, line.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, sections, stations);
    }
}
