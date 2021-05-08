package wooteco.subway.line.section;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class bear {

    private SectionService sectionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        sectionService = new SectionService(sectionDao);
    }

    @Test
    void test() {
        sectionDao.save(new Section(1L, 3L, 5L, 17));
        sectionService.createSection(1L, new SectionRequest(3L, 4L, 5));
        List<Section> sections = sectionDao.findByLineId(1L);
        System.out.println(sections);
    }

    @Test
    void test2() {
        sectionDao.save(new Section(1L, 3L, 5L, 17));
        sectionService.createSection(1L, new SectionRequest(6L, 5L, 5));
        List<Section> sections = sectionDao.findByLineId(1L);
        System.out.println(sections);
    }
}
