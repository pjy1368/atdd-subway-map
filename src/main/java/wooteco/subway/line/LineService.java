package wooteco.subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicatedNameException;
import wooteco.subway.line.section.SectionRequest;
import wooteco.subway.line.section.SectionService;
import wooteco.subway.station.StationService;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;
    private final StationService stationService;

    public LineService(final LineDao lineDao, final SectionService sectionService, final StationService stationService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(final LineRequest lineRequest) {
        final String name = lineRequest.getName();
        final String color = lineRequest.getColor();

        validateDuplicatedLineName(name);
        final Line line = lineDao.save(new Line(name, color));

        final Long upStationId = lineRequest.getUpStationId();
        final Long downStationId = lineRequest.getDownStationId();
        final int distance = lineRequest.getDistance();

        sectionService.createSection(line.getId(), new SectionRequest(upStationId, downStationId, distance));
        return LineResponse.from(line);
    }

    private void validateDuplicatedLineName(final String name) {
        lineDao.findByName(name)
            .ifPresent(station -> {
                throw new DuplicatedNameException("중복된 이름의 노선입니다.");
            });
    }

    public List<LineResponse> findLines() {
        return lineDao.findAll().stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
    }

    public LineResponse findLine(final Long id) {
        final Line line = lineDao.findById(id).orElseThrow(() -> {
            throw new DataNotFoundException("해당 Id의 노선이 없습니다.");
        });
        // Section table
        // findSectionsById()를 통해 특정 line_id에 속하는 Section들을 가져온다.
        // (6, 4) (1, 2) (2, 4)를
        // (1, 2) (2, 4) (4, 6)로 조립해야
        // 상행: 1, 하행 : 6 판단 가능.

        // Line JOIN Section을 통해 upStationId, downStationId를 가져올 수 있다.
        // SELECT UP_STATION_ID, DOWN_STATION_ID FROM SECTION LEFT JOIN LINE ON SECTION.LINE_ID = LINE.ID
        // line.addStation(stationService.findById(upStationId), stationService.findById(downStationId))
        return LineResponse.from(line);
    }

    public void updateLine(final Long id, final LineRequest lineRequest) {
        findLine(id);
        lineDao.update(new Line(id, lineRequest.getName(), lineRequest.getColor()));
    }

    public void deleteLine(final Long id) {
        findLine(id);
        lineDao.deleteById(id);
    }
}
