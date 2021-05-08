package wooteco.subway.line.section;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionDao sectionDao;

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음")
    @Test
    void createSectionFail_alreadyExistent() {
        final Long lineId = 1L;
        final Long upStationId = 3L;
        final Long downStationId = 2L;
        final Section section = new Section(lineId, upStationId, downStationId, 10);
        given(sectionDao.findByLineId(lineId)).willReturn(Collections.singletonList(section));

        final SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        assertThatThrownBy(() -> sectionService.createSection(lineId, sectionRequest))
            .hasMessage("상행역과 하행역이 이미 노선에 모두 등록되어 있습니다.")
            .isInstanceOf(RuntimeException.class);

        verify(sectionDao, times(1)).findByLineId(lineId);
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    @Test
    void createSectionFail_notExistent() {
        final Long lineId = 1L;
        final Long upStationId = 3L;
        final Long downStationId = 2L;
        final Section section = new Section(lineId, upStationId, downStationId, 10);
        given(sectionDao.findByLineId(lineId)).willReturn(Collections.singletonList(section));

        final SectionRequest sectionRequest = new SectionRequest(5L, 6L, 10);

        assertThatThrownBy(() -> sectionService.createSection(lineId, sectionRequest))
            .hasMessage("상행역과 하행역 둘 다 포함되어있지 않습니다.")
            .isInstanceOf(RuntimeException.class);

        verify(sectionDao, times(1)).findByLineId(lineId);
    }
}
