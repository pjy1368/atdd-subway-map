package wooteco.subway.line.section;

import org.springframework.stereotype.Service;
import wooteco.subway.line.Line;
import wooteco.subway.line.LineRequest;
import wooteco.subway.line.LineResponse;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public SectionResponse createSection(final long lineId, final SectionRequest sectionRequest) {
        final Section section = sectionDao.save(sectionRequest.toEntity(lineId));
        return SectionResponse.from(section);
    }
}
