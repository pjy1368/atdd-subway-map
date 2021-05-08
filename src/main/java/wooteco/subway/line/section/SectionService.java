package wooteco.subway.line.section;

import org.springframework.stereotype.Service;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public SectionResponse createSection(final long lineId, final SectionRequest sectionRequest) {
        final Sections sections = findSectionsByLineId(lineId);
        final Long upStationId = sectionRequest.getUpStationId();
        final Long downStationId = sectionRequest.getDownStationId();

        if (sections.containsStation(upStationId) && sections.containsStation(downStationId)) {
            throw new RuntimeException("상행역과 하행역이 이미 노선에 모두 등록되어 있습니다.");
        }
        if (!sections.containsStation(upStationId) && !sections.containsStation(downStationId)) {
            throw new RuntimeException("상행역과 하행역 둘 다 포함되어있지 않습니다.");
        }

        final long existentStationId = sections.matchedStationId(upStationId, downStationId);

        if (sections.isUpEndStation(existentStationId) && existentStationId == downStationId) {
            return SectionResponse.from(sectionDao.save(sectionRequest.toEntity(lineId)));
        }

        if (sections.isDownEndStation(existentStationId) && existentStationId == upStationId) {
            return SectionResponse.from(sectionDao.save(sectionRequest.toEntity(lineId)));
        }

        final Section section = sectionDao.save(sectionRequest.toEntity(lineId));
        return SectionResponse.from(section);
    }

    public Sections findSectionsByLineId(final Long lineId) {
        return new Sections(sectionDao.findByLineId(lineId));
    }
}
