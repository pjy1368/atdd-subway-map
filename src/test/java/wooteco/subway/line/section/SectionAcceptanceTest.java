package wooteco.subway.line.section;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("구간을 생성한다.")
    @Test
    void createSection() {
        Map<String, Object> params1 = new HashMap<>();
        params1.put("name", "2호선");
        params1.put("color", "grey darken-1");
        params1.put("upStationId", 1);
        params1.put("downStationId", 2);
        params1.put("distance", 2);
        params1.put("extraFare", 500);

        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        Map<String, Object> params2 = new HashMap<>();
        params2.put("downStationId", 4);
        params2.put("upStationId", 2);
        params2.put("distance", 10);

        // when
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(response1.header("Location") + "/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response2.header("Location")).isNotBlank();
        assertThat(response2.body().as(SectionResponse.class).getId())
            .isEqualTo(Long.parseLong(response2.header("Location").split("/")[4]));
        assertThat(response2.body().as(SectionResponse.class).getDownStationId()).isEqualTo(4L);
        assertThat(response2.body().as(SectionResponse.class).getUpStationId()).isEqualTo(2L);
        assertThat(response2.body().as(SectionResponse.class).getDistance()).isEqualTo(10);
    }
}
