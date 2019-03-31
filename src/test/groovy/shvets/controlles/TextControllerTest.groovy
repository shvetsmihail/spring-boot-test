package shvets.controlles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import shvets.services.TextService
import spock.lang.Specification

import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@WebMvcTest
class TextControllerTest extends Specification {
    private static final BASE_PATH = "/api/v.1.0/text/"

    @Autowired
    private MockMvc mvc

    @MockBean
    private TextService service

    def "GetReversedText OK"() {
        given:
        when(service.getReversedWords("hello")).thenReturn("olleh")

        when:
        def response = mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("hello"))
                .andReturn().response

        then:
        response.status == 200
        MediaType.parseMediaType(response.contentType).equalsTypeAndSubtype(MediaType.TEXT_PLAIN)
        response.contentAsString == "olleh"
    }

    def "GetReversedText when body is empty"() {
        expect:
        mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(""))
                .andExpect(status().isBadRequest())
    }

    def "GetReversedText when body is null"() {
        expect:
        mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isBadRequest())
    }

    def "GetReversedText when TextService throw an exception"() {
        given:
        when(service.getReversedWords("hello")).thenThrow(new RuntimeException("oops"))

        expect:
        mvc.perform(post(BASE_PATH + "reversed")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("hello"))
                .andExpect(status().isInternalServerError())
    }

    def "GetTextCountResult"() {
    }
}
