package wooteco.subway.line.section;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> sectionRowMapper = (resultSet, rowNum) -> new Section(
        resultSet.getLong("id"),
        resultSet.getLong("line_id"),
        resultSet.getLong("up_station_id"),
        resultSet.getLong("down_station_id"),
        resultSet.getInt("distance")
    );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Section save(final Section section) {
        final String sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) VALUES (?, ?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, section.getLineId());
            preparedStatement.setLong(2, section.getUpStationId());
            preparedStatement.setLong(3, section.getDownStationId());
            preparedStatement.setInt(4, section.getDistance());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Section(
            id, section.getLineId(), section.getUpStationId(), section.getDownStationId(), section.getDistance()
        );
    }

    public List<Section> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }
}
